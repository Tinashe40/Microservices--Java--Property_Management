import React from 'react';
import { Typography, Container, CircularProgress, Card, CardContent, Alert, Avatar, Box, Paper } from '@mui/material';
import { useCurrentUser } from '../api/authService';

const ProfilePage = () => {
  const { data: currentUser, isLoading, isError, error } = useCurrentUser();

  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
        <Typography>Loading profile...</Typography>
      </Container>
    );
  }

  if (isError) {
    return (
      <Container>
        <Alert severity="error">Error loading profile: {error?.message}</Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <Avatar sx={{ width: 80, height: 80, mr: 3 }}>
            {currentUser?.firstName.charAt(0).toUpperCase()}
          </Avatar>
          <Typography variant="h4" component="h1">
            {currentUser?.firstName} {currentUser?.lastName}
          </Typography>
        </Box>
        <Card>
          <CardContent>
            <Typography variant="h6">Profile Details</Typography>
            <Box sx={{ mt: 2 }}>
              <Typography><strong>Username:</strong> {currentUser?.username}</Typography>
              <Typography><strong>Email:</strong> {currentUser?.email}</Typography>
              <Typography><strong>Roles:</strong> {currentUser?.roles.join(', ')}</Typography>
            </Box>
          </CardContent>
        </Card>
      </Paper>
    </Container>
  );
};

export default ProfilePage;
