import React from 'react';
import { Typography, Container, Grid, Card, CardContent, CardActionArea } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';

const HomePage = () => {
  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Welcome to the Property Management Dashboard
      </Typography>
      <Typography variant="body1" sx={{ mb: 4 }}>
        Use the navigation on the left to explore properties and user information.
      </Typography>
      <Grid container spacing={3}>
        <Grid xs={12} sm={6}>
          <Card>
            <CardActionArea component={RouterLink} to="/properties">
              <CardContent sx={{ display: 'flex', alignItems: 'center' }}>
                <BusinessIcon sx={{ fontSize: 40, mr: 2 }} />
                <Typography variant="h6">Properties</Typography>
              </CardContent>
            </CardActionArea>
          </Card>
        </Grid>
        <Grid xs={12} sm={6}>
          <Card>
            <CardActionArea component={RouterLink} to="/users">
              <CardContent sx={{ display: 'flex', alignItems: 'center' }}>
                <PeopleIcon sx={{ fontSize: 40, mr: 2 }} />
                <Typography variant="h6">Users</Typography>
              </CardContent>
            </CardActionArea>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default HomePage;

