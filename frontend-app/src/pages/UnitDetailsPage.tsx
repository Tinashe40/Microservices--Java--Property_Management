import React from 'react';
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
  Button,
  Paper,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Chip,
} from '@mui/material';
import { useUnitById } from '../api/unitService';
import { usePropertyById } from '../api/propertyService';
import { useFloorById } from '../api/floorService';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import MeetingRoomIcon from '@mui/icons-material/MeetingRoom';
import BusinessIcon from '@mui/icons-material/Business';
import ApartmentIcon from '@mui/icons-material/Apartment';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import PersonIcon from '@mui/icons-material/Person';

const DetailItem = ({ icon, primary, secondary }: { icon: React.ReactElement, primary: string, secondary: string | number }) => (
  <ListItem>
    <ListItemIcon sx={{ minWidth: 40 }}>{icon}</ListItemIcon>
    <ListItemText primary={primary} secondary={secondary} />
  </ListItem>
);

const UnitDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const unitId = Number(id);

  if (isNaN(unitId) || unitId <= 0) {
    return (
      <Container>
        <Alert severity="error">Invalid Unit ID in URL.</Alert>
      </Container>
    );
  }

  const {
    data: unit,
    isLoading: isLoadingUnit,
    isError: isErrorUnit,
    error: errorUnit,
  } = useUnitById(unitId);

  const { data: property } = usePropertyById(unit?.propertyId as number);
  const { data: floor } = useFloorById(unit?.floorId as number);

  if (isLoadingUnit) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  if (isErrorUnit) {
    return (
      <Container>
        <Alert severity="error">
          Error loading unit details: {errorUnit?.message}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/units')}
        sx={{ mb: 2 }}
      >
        Back to Units
      </Button>
      <Paper elevation={3} sx={{ p: 3, mb: 3, borderRadius: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            {unit?.name}
          </Typography>
          <Chip label={unit?.occupancyStatus} color="primary" sx={{ ml: 2 }} />
        </Box>
      </Paper>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Unit Details</Typography>
              <List>
                <Grid container>
                  <Grid item xs={12} md={6}>
                    <DetailItem icon={<BusinessIcon />} primary="Property" secondary={property?.name ?? 'N/A'} />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <DetailItem icon={<ApartmentIcon />} primary="Floor" secondary={floor?.name ?? 'N/A'} />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <DetailItem icon={<AttachMoneyIcon />} primary="Monthly Rent" secondary={`$${unit?.monthlyRent?.toFixed(2) ?? 'N/A'}`} />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <DetailItem icon={<PersonIcon />} primary="Tenant" secondary={unit?.tenant || 'N/A'} />
                  </Grid>
                </Grid>
              </List>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default UnitDetailsPage;
