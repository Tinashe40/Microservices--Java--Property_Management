import React, { useState, useEffect } from 'react';
import {
  Typography,
  Container,
  CircularProgress,
  Card,
  CardContent,
  Alert,
  Avatar,
  Box,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
} from '@mui/material';
import { useCurrentUser } from '../api/authService';
import { useUpdateUser, useChangePassword } from '../api/userService';
import { useSnackbar } from '../contexts/SnackbarProvider';

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

const ProfilePage = () => {
  const { data: currentUser, isLoading, isError, error } = useCurrentUser();
  const { showSnackbar } = useSnackbar();

  const updateUser = useUpdateUser();
  const changePassword = useChangePassword();

  const [editOpen, setEditOpen] = useState(false);
  const [passwordOpen, setPasswordOpen] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phoneNumber: '',
  });
  const [passwordData, setPasswordData] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  useEffect(() => {
    if (currentUser) {
      setFormData({
        firstName: currentUser.firstName,
        lastName: currentUser.lastName,
        phoneNumber: currentUser.phoneNumber || '',
      });
    }
  }, [currentUser]);

  const handleEditOpen = () => setEditOpen(true);
  const handleEditClose = () => setEditOpen(false);

  const handlePasswordOpen = () => setPasswordOpen(true);
  const handlePasswordClose = () => {
    setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
    setPasswordOpen(false);
  };

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPasswordData({ ...passwordData, [e.target.name]: e.target.value });
  };

  const handleUpdateProfile = () => {
    if (!formData.firstName || !formData.lastName) {
      showSnackbar('First name and last name are required', 'warning');
      return;
    }
    if (currentUser) {
      const updatedUser = { ...currentUser, ...formData };
      updateUser.mutate(updatedUser, {
        onSuccess: () => {
          showSnackbar('Profile updated successfully', 'success');
          handleEditClose();
        },
        onError: (error: Error) => {
          showSnackbar(`Error updating profile: ${error.message}`, 'error');
        },
      });
    }
  };

  const handleChangePassword = () => {
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      showSnackbar("Passwords don't match", 'error');
      return;
    }
    if (!passwordData.oldPassword || !passwordData.newPassword) {
      showSnackbar('All password fields are required', 'warning');
      return;
    }
    if (!passwordRegex.test(passwordData.newPassword)) {
      showSnackbar('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.', 'warning');
      return;
    }
    changePassword.mutate(passwordData, {
      onSuccess: () => {
        showSnackbar('Password changed successfully', 'success');
        handlePasswordClose();
      },
      onError: (error: Error) => {
        showSnackbar(`Error changing password: ${error.message}`, 'error');
      },
    });
  };

  if (isLoading) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
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
          <Avatar sx={{ width: 80, height: 80, mr: 3, bgcolor: 'primary.main' }}>
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
              <Typography>
                <strong>Username:</strong> {currentUser?.username}
              </Typography>
              <Typography>
                <strong>Email:</strong> {currentUser?.email}
              </Typography>
              <Typography>
                <strong>Phone Number:</strong> {currentUser?.phoneNumber || 'N/A'}
              </Typography>
              <Typography>
                <strong>Roles:</strong> {currentUser?.roles.join(', ')}
              </Typography>
            </Box>
            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button variant="contained" onClick={handleEditOpen}>
                Edit Profile
              </Button>
              <Button variant="outlined" onClick={handlePasswordOpen}>
                Change Password
              </Button>
            </Box>
          </CardContent>
        </Card>
      </Paper>

      {/* Edit Profile Dialog */}
      <Dialog open={editOpen} onClose={handleEditClose}>
        <DialogTitle>Edit Profile</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="firstName"
            label="First Name"
            type="text"
            fullWidth
            variant="standard"
            value={formData.firstName}
            onChange={handleFormChange}
            required
          />
          <TextField
            margin="dense"
            name="lastName"
            label="Last Name"
            type="text"
            fullWidth
            variant="standard"
            value={formData.lastName}
            onChange={handleFormChange}
            required
          />
          <TextField
            margin="dense"
            name="phoneNumber"
            label="Phone Number"
            type="text"
            fullWidth
            variant="standard"
            value={formData.phoneNumber}
            onChange={handleFormChange}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleEditClose}>Cancel</Button>
          <Button onClick={handleUpdateProfile} variant="contained" disabled={updateUser.isPending}>
            {updateUser.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Change Password Dialog */}
      <Dialog open={passwordOpen} onClose={handlePasswordClose}>
        <DialogTitle>Change Password</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="oldPassword"
            label="Old Password"
            type="password"
            fullWidth
            variant="standard"
            value={passwordData.oldPassword}
            onChange={handlePasswordChange}
            required
          />
          <TextField
            margin="dense"
            name="newPassword"
            label="New Password"
            type="password"
            fullWidth
            variant="standard"
            value={passwordData.newPassword}
            onChange={handlePasswordChange}
            required
          />
          <TextField
            margin="dense"
            name="confirmPassword"
            label="Confirm New Password"
            type="password"
            fullWidth
            variant="standard"
            value={passwordData.confirmPassword}
            onChange={handlePasswordChange}
            required
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handlePasswordClose}>Cancel</Button>
          <Button onClick={handleChangePassword} variant="contained" disabled={changePassword.isPending}>
            {changePassword.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default ProfilePage;
