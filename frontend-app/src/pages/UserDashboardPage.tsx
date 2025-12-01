import React, { useState } from 'react';
import {
  Typography,
  Container,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  Alert,
  Button,
  Box,
  TablePagination,
  Chip,
} from '@mui/material';
import { usePropertiesByManager, usePropertyStatsByManager } from '../api/propertyService';
import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { People, Business, HomeWork, AttachMoney, TrendingUp, MonetizationOn } from '@mui/icons-material';

const StatCard = ({ title, value, icon, loading, color }: { title: string; value: string | number; icon: React.ReactElement; loading: boolean, color: string }) => (
    <Card sx={{ display: 'flex', alignItems: 'center', p: 2, backgroundColor: 'white', borderRadius: 4, boxShadow: 3 }}>
        <Box sx={{
            bgcolor: color,
            borderRadius: '50%',
            p: 2,
            mr: 2,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white'
        }}>
            {icon}
        </Box>
        <Box>
            <Typography variant="h6" component="div" sx={{ fontWeight: 'bold' }}>
                {loading ? <CircularProgress size={24} color="inherit" /> : value}
            </Typography>
            <Typography variant="body2" color="text.secondary">{title}</Typography>
        </Box>
    </Card>
);

const UserDashboardPage = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);
  const { user } = useAuth();

  const {
    data: propertiesData,
    isLoading: isLoadingProperties,
    isError: isPropertiesError,
    error: propertiesError,
  } = usePropertiesByManager(user?.id ?? 0, page, rowsPerPage);
  
  const { data: managerStats, isLoading: isLoadingManagerStats } = usePropertyStatsByManager(user?.id ?? 0);

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  if (isLoadingProperties) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (isPropertiesError) {
    return (
      <Alert severity="error">Error loading properties: {propertiesError?.message}</Alert>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          My Dashboard
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Welcome back, {user?.firstName}! Here's a summary of your properties.
        </Typography>
      </Box>
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Properties"
            value={propertiesData?.totalElements ?? '...'}
            icon={<Business />}
            loading={isLoadingProperties}
            color="#1E88E5"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Units"
            value={managerStats?.totalUnits ?? '...'}
            icon={<HomeWork />}
            loading={isLoadingManagerStats}
            color="#6D4C41"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Occupancy Rate"
            value={`${managerStats?.occupancyRate?.toFixed(2) ?? '...'}%`}
            icon={<TrendingUp />}
            loading={isLoadingManagerStats}
            color="#43A047"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Actual Income"
            value={`${managerStats?.totalRentalIncome?.toLocaleString() ?? '...'}`}
            icon={<AttachMoney />}
            loading={isLoadingManagerStats}
            color="#FDD835"
          />
        </Grid>
      </Grid>
      
      <Typography variant="h5" component="h2" gutterBottom sx={{ fontWeight: 'bold' }}>
        My Properties
      </Typography>
      {propertiesData && propertiesData.content.length > 0 ? (
        <>
          <Grid container spacing={3}>
            {propertiesData.content.map((property) => (
              <Grid key={property.id} item xs={12} sm={6} md={4}>
                <Card sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography variant="h6" gutterBottom>{property.name}</Typography>
                    <Typography color="text.secondary" sx={{ mb: 1 }}>
                      {property.address}
                    </Typography>
                    <Chip label={property.propertyType} size="small" />
                  </CardContent>
                  <Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
                    <Button
                      size="small"
                      variant="outlined"
                      onClick={() => navigate(`/properties/${property.id}`)}
                    >
                      View
                    </Button>
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
          <TablePagination
            component="div"
            count={propertiesData.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            rowsPerPageOptions={[9, 18, 27]}
          />
        </>
      ) : (
        <Typography sx={{ mt: 4, textAlign: 'center' }}>No properties assigned to you.</Typography>
      )}
    </Container>
  );
};

export default UserDashboardPage;