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
  Divider,
} from '@mui/material';
import { useFloorById } from '../api/floorService';
import { useUnitsByFloor } from '../api/unitService';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ApartmentIcon from '@mui/icons-material/Apartment';
import MeetingRoomIcon from '@mui/icons-material/MeetingRoom';

const FloorDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const floorId = Number(id);

  if (isNaN(floorId) || floorId <= 0) {
    return (
      <Container>
        <Alert severity="error">Invalid Floor ID in URL.</Alert>
      </Container>
    );
  }

  const {
    data: floor,
    isLoading: isLoadingFloor,
    isError: isErrorFloor,
    error: errorFloor,
  } = useFloorById(floorId);

  const {
    data: unitsData,
    isLoading: isLoadingUnits,
  } = useUnitsByFloor(floorId, 0, 100); // Assuming max 100 units per floor for simplicity

  if (isLoadingFloor) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  if (isErrorFloor) {
    return (
      <Container>
        <Alert severity="error">
          Error loading floor details: {errorFloor?.message}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/floors')}
        sx={{ mb: 2 }}
      >
        Back to Floors
      </Button>
      <Paper elevation={3} sx={{ p: 3, mb: 3, borderRadius: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {floor?.name}
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Property ID: {floor?.propertyId}
        </Typography>
      </Paper>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Units on this Floor</Typography>
              {isLoadingUnits ? <CircularProgress /> : (
                <List>
                  {unitsData?.content.map(unit => (
                    <ListItem key={unit.id} secondaryAction={
                      <Button variant="outlined" size="small" onClick={() => navigate(`/units/${unit.id}`)}>
                        View Unit
                      </Button>
                    }>
                      <ListItemIcon>
                        <MeetingRoomIcon />
                      </ListItemIcon>
                      <ListItemText primary={unit.name} secondary={`Status: ${unit.occupancyStatus}`} />
                    </ListItem>
                  ))}
                </List>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default FloorDetailsPage;
