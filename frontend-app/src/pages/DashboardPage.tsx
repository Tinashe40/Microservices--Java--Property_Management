import React from 'react';
import { CircularProgress, Box } from '@mui/material';
import { useAuth } from '../hooks/useAuth';
import AdminDashboardPage from './AdminDashboardPage';
import UserDashboardPage from './UserDashboardPage';

const DashboardPage = () => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  const isAdmin = user?.roles.includes('ADMIN') || user?.roles.includes('SUPER_ADMIN');

  return isAdmin ? <AdminDashboardPage /> : <UserDashboardPage />;
};

export default DashboardPage;