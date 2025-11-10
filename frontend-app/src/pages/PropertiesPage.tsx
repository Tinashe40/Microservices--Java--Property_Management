import React from 'react';
import { Typography, Container, CircularProgress, Grid, Card, CardContent, Alert } from '@mui/material';
import { useProperties } from '../api/propertyService';

const PropertiesPage = () => {
  const { data, isLoading, isError, error } = useProperties();

  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
        <Typography>Loading properties...</Typography>
      </Container>
    );
  }

  if (isError) {
    return (
      <Container>
        <Alert severity="error">Error loading properties: {error?.message}</Alert>
      </Container>
    );
  }

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Properties
      </Typography>
      {data && data.length > 0 ? (
        <Grid container spacing={3}>
          {data.map((property) => (
            <Grid key={property.id} xs={12} sm={6} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{property.name}</Typography>
                  <Typography color="text.secondary">{property.address}</Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : (
        <Typography>No properties found.</Typography>
      )}
    </Container>
  );
};

export default PropertiesPage;