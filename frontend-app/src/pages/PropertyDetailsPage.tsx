import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  CircularProgress,
  Alert,
  Box,
  Card,
  CardContent,
  Grid,
  Tabs,
  Tab,
  Button,
  Paper,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Chip,
  Divider,
} from '@mui/material';
import {
  usePropertyById,
  usePropertyStats,
} from '../api/propertyService';
import {
    useCalculateRentalIncome,
    useCountUnitsByProperty,
} from '../api/unitService';
import FloorManager from '../components/FloorManager';
import UnitManager from '../components/UnitManager';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import BusinessIcon from '@mui/icons-material/Business';
import LocationCityIcon from '@mui/icons-material/LocationCity';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ApartmentIcon from '@mui/icons-material/Apartment';
import MeetingRoomIcon from '@mui/icons-material/MeetingRoom';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import FunctionsIcon from '@mui/icons-material/Functions';

const StatItem = ({ icon, primary, secondary }: { icon: React.ReactElement, primary: string, secondary: string | number }) => (
  <ListItem>
    <ListItemIcon sx={{ minWidth: 40 }}>{icon}</ListItemIcon>
    <ListItemText primary={primary} secondary={secondary} />
  </ListItem>
);

const PropertyDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const propertyId = Number(id);

  if (isNaN(propertyId) || propertyId <= 0) {
    return (
      <Container>
        <Alert severity="error">Invalid Property ID in URL.</Alert>
      </Container>
    );
  }

  const {
    data: property,
    isLoading: isLoadingProperty,
    isError: isErrorProperty,
    error: errorProperty,
  } = usePropertyById(propertyId);
  const { data: stats, isLoading: isLoadingStats } = usePropertyStats(propertyId);
  const { data: rentalIncome } = useCalculateRentalIncome(propertyId);
  const { data: unitsCount } = useCountUnitsByProperty(propertyId);

  const [tabValue, setTabValue] = useState(0);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  if (isLoadingProperty) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  if (isErrorProperty) {
    return (
      <Container>
        <Alert severity="error">
          Error loading property details: {errorProperty?.message}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/properties')}
        sx={{ mb: 2 }}
      >
        Back to Properties
      </Button>
      <Paper elevation={0} sx={{ p: 3, mb: 3, borderRadius: 2, bgcolor: 'rgba(255, 255, 255, 0.7)', backdropFilter: 'blur(10px)' }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {property?.name}
        </Typography>
        <Chip label={property?.propertyType} color="primary" />
      </Paper>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Card sx={{ height: '100%' }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>Property Details</Typography>
              <List>
                <StatItem icon={<LocationCityIcon />} primary="Address" secondary={property?.address ?? 'N/A'} />
                {property?.managedByDetails && (
                  <StatItem icon={<AccountCircleIcon />} primary="Managed By" secondary={property.managedByDetails.username} />
                )}
              </List>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={8}>
          <Card sx={{ height: '100%' }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>Property Statistics</Typography>
              {isLoadingStats ? <CircularProgress /> : stats ? (
                <List dense>
                  <Grid container spacing={1}>
                    <Grid item xs={6} sm={4}><StatItem icon={<ApartmentIcon />} primary="Total Floors" secondary={stats.totalFloors} /></Grid>
                    <Grid item xs={6} sm={4}><StatItem icon={<BusinessIcon />} primary="Total Units" secondary={unitsCount ?? '...'} /></Grid>
                    <Grid item xs={6} sm={4}><StatItem icon={<MeetingRoomIcon />} primary="Occupied Units" secondary={Math.round((unitsCount ?? stats.totalUnits) * (stats.occupancyRate / 100))} /></Grid>
                    <Grid item xs={6} sm={4}><StatItem icon={<TrendingUpIcon />} primary="Occupancy Rate" secondary={`${stats.occupancyRate.toFixed(2)}%`} /></Grid>
                    <Grid item xs={6} sm={4}><StatItem icon={<AttachMoneyIcon />} primary="Actual Income" secondary={`$${stats.totalRentalIncome.toFixed(2)}`} /></Grid>
                    <Grid item xs={6} sm={4}><StatItem icon={<FunctionsIcon />} primary="Potential Income" secondary={`$${rentalIncome?.toFixed(2) ?? '...'}`} /></Grid>
                  </Grid>
                </List>
              ) : (
                <Typography>No statistics available.</Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Paper sx={{ width: '100%', mt: 3, borderRadius: 2, overflow: 'hidden' }}>
        <Tabs value={tabValue} onChange={handleTabChange} centered variant="fullWidth" indicatorColor="primary" textColor="primary">
          <Tab label="Floor Management" />
          <Tab label="Unit Management" />
        </Tabs>
        <Divider />
        <Box sx={{ p: 3 }}>
          {tabValue === 0 && <FloorManager propertyId={propertyId} />}
          {tabValue === 1 && <UnitManager propertyId={propertyId} />}
        </Box>
      </Paper>
    </Container>
  );
};

export default PropertyDetailsPage;
