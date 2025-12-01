import React, { useState } from 'react';
import { Container, Grid, Card, CardContent, Typography, CircularProgress, Alert, Box, List, ListItem, ListItemIcon, ListItemText, Button, Divider, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import { useSystemWideStats, useProperties } from '../api/propertyService';
import { useUsersCount } from '../api/userService';
import { People, Business, HomeWork, AttachMoney, TrendingUp, MonetizationOn, Apartment, MeetingRoom } from '@mui/icons-material';
import { useFloorsByProperty } from '../api/floorService';
import { useUnitsByProperty } from '../api/unitService';
import { Link as RouterLink } from 'react-router-dom';

const StatCard = ({ title, value, icon, loading, color }: { title: string; value: string | number; icon: React.ReactElement; loading: boolean, color: string }) => (
    <Card sx={{ display: 'flex', alignItems: 'center', p: 2, borderRadius: 2, height: '100%' }}>
        <Box sx={{
            bgcolor: color,
            borderRadius: '50%',
            p: 1.5,
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

const RecentFloors: React.FC<{ propertyId: number }> = ({ propertyId }) => {
    const { data, isLoading, isError } = useFloorsByProperty(propertyId, 0, 5);

    return (
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>Recent Floors</Typography>
                {isLoading && <CircularProgress />}
                {isError && <Alert severity="error">Could not load floors.</Alert>}
                {!isLoading && !isError && data?.content.length === 0 && <Typography>No floors found for this property.</Typography>}
                <List dense>
                    {data?.content.map(floor => (
                        <ListItem key={floor.id} disableGutters>
                            <ListItemIcon sx={{minWidth: 40}}>
                                <Apartment/>
                            </ListItemIcon>
                            <ListItemText primary={floor.name} secondary={`Property ID: ${floor.propertyId}`} />
                        </ListItem>
                    ))}
                </List>
            </CardContent>
            <Divider/>
            <Box sx={{ p: 1, display: 'flex', justifyContent: 'flex-end' }}>
                <Button component={RouterLink} to="/floors" size="small">View All</Button>
            </Box>
        </Card>
    );
}

const RecentUnits: React.FC<{ propertyId: number }> = ({ propertyId }) => {
    const { data, isLoading, isError } = useUnitsByProperty(propertyId, 0, 5);

    return (
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>Recent Units</Typography>
                {isLoading && <CircularProgress />}
                {isError && <Alert severity="error">Could not load units.</Alert>}
                {!isLoading && !isError && data?.content.length === 0 && <Typography>No units found for this property.</Typography>}
                <List dense>
                    {data?.content.map(unit => (
                        <ListItem key={unit.id} disableGutters>
                            <ListItemIcon sx={{minWidth: 40}}>
                                <MeetingRoom/>
                            </ListItemIcon>
                            <ListItemText primary={unit.name} secondary={`Status: ${unit.occupancyStatus}`} />
                        </ListItem>
                    ))}
                </List>
            </CardContent>
            <Divider/>
            <Box sx={{ p: 1, display: 'flex', justifyContent: 'flex-end' }}>
                <Button component={RouterLink} to="/units" size="small">View All</Button>
            </Box>
        </Card>
    );
}


const AdminDashboardPage = () => {
  const { data: systemStats, isLoading: isLoadingSystemStats, isError: isErrorSystemStats } = useSystemWideStats();
  const { data: usersCount, isLoading: isLoadingUsersCount, isError: isErrorUsersCount } = useUsersCount();
  const { data: propertiesData, isLoading: isLoadingProperties } = useProperties(0, 1000);
  const [selectedPropertyId, setSelectedPropertyId] = useState<number | ''>('');

  const handlePropertyChange = (event: SelectChangeEvent<number | ''>) => {
    setSelectedPropertyId(event.target.value as number | '');
  };

  if (isErrorSystemStats || isErrorUsersCount) {
    return <Alert severity="error">Error loading dashboard data.</Alert>;
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          Admin Dashboard
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Welcome back! Here's a summary of your system's activity.
        </Typography>
      </Box>
      <Grid container spacing={3}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Properties"
            value={systemStats?.totalProperties ?? '...'}
            icon={<Business />}
            loading={isLoadingSystemStats}
            color="primary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Units"
            value={systemStats?.totalUnits ?? '...'}
            icon={<HomeWork />}
            loading={isLoadingSystemStats}
            color="secondary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Users"
            value={usersCount ?? '...'}
            icon={<People />}
            loading={isLoadingUsersCount}
            color="success.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Occupancy Rate"
            value={`${systemStats?.overallOccupancyRate?.toFixed(2) ?? '...'}%`}
            icon={<TrendingUp />}
            loading={isLoadingSystemStats}
            color="info.main"
          />
        </Grid>
        <Grid item xs={12}>
            <Card>
                <CardContent>
                    <Typography variant="h6" gutterBottom>Financial Summary</Typography>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <StatCard
                                title="Total Actual Income"
                                value={`$${systemStats?.totalActualIncome?.toLocaleString() ?? '...'}`}
                                icon={<AttachMoney />}
                                loading={isLoadingSystemStats}
                                color="warning.main"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StatCard
                                title="Total Potential Income"
                                value={`$${systemStats?.totalPotentialIncome?.toLocaleString() ?? '...'}`}
                                icon={<MonetizationOn />}
                                loading={isLoadingSystemStats}
                                color="error.main"
                            />
                        </Grid>
                    </Grid>
                </CardContent>
            </Card>
        </Grid>
        <Grid item xs={12}>
          <FormControl fullWidth>
            <InputLabel>Select Property to see Recent Activity</InputLabel>
            <Select
              value={selectedPropertyId}
              onChange={handlePropertyChange}
              label="Select Property to see Recent Activity"
            >
              <MenuItem value="">
                <em>None</em>
              </MenuItem>
              {propertiesData?.content.map((property) => (
                <MenuItem key={property.id} value={property.id}>
                  {property.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        {selectedPropertyId && (
          <>
            <Grid item xs={12} md={6}>
              <RecentFloors propertyId={selectedPropertyId} />
            </Grid>
            <Grid item xs={12} md={6}>
              <RecentUnits propertyId={selectedPropertyId} />
            </Grid>
          </>
        )}
      </Grid>
    </Container>
  );
};

export default AdminDashboardPage;
