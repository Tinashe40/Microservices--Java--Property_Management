import React, { useState, useEffect } from 'react';
import {
  Typography,
  Container,
  CircularProgress,
  Alert,
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  TablePagination,
  Box,
  Tabs,
  Tab,
  FormGroup,
  FormControlLabel,
  Checkbox,
  Switch,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
} from '@mui/material';
import {
  useUsers,
  useCreateUser,
  useDeleteUser,
  useAssignRolesToUser,
  useDeactivateUser,
  useResetPassword,
  useActivateUser,
} from '../api/userService';
import { useRoles } from '../api/rolesService';
import DeleteIcon from '@mui/icons-material/Delete';
import { UserDto } from '../api/authService';
import { useSnackbar } from '../contexts/SnackbarProvider';

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

const UsersPage = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const { data: usersPage, isLoading, isError, error } = useUsers(page, rowsPerPage);
  const { data: rolesPage } = useRoles(0, 1000);
  const { showSnackbar } = useSnackbar();

  const createUser = useCreateUser();
  const deleteUser = useDeleteUser();
  const assignRoles = useAssignRolesToUser();
  const deactivateUser = useDeactivateUser();
  const activateUser = useActivateUser();
  const resetPassword = useResetPassword();

  const [createOpen, setCreateOpen] = useState(false);
  const [manageOpen, setManageOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserDto | null>(null);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    roles: ['USER'],
  });
  const [userToDelete, setUserToDelete] = useState<number | null>(null);
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [tabValue, setTabValue] = useState(0);
  const [selectedRoles, setSelectedRoles] = useState<string[]>([]);
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  useEffect(() => {
    if (selectedUser) {
      setSelectedRoles(selectedUser.roles || []);
    }
  }, [selectedUser]);

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleCreateOpen = () => {
    setNewUser({
      username: '',
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      phoneNumber: '',
      roles: ['USER'],
    });
    setCreateOpen(true);
  };
  const handleCreateClose = () => setCreateOpen(false);

  const handleManageOpen = (user: UserDto) => {
    setSelectedUser(user);
    setManageOpen(true);
  };
  const handleManageClose = () => {
    setManageOpen(false);
    setSelectedUser(null);
    setTabValue(0);
  };

  const handleNewUserChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewUser({ ...newUser, [e.target.name]: e.target.value });
  };

  const handleCreateUser = () => {
    if (!newUser.username || !newUser.email || !newUser.password || !newUser.firstName || !newUser.lastName) {
      showSnackbar('Please fill all required fields', 'warning');
      return;
    }
    if (!passwordRegex.test(newUser.password)) {
      showSnackbar('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.', 'warning');
      return;
    }
    createUser.mutate(newUser, {
      onSuccess: () => {
        showSnackbar('User created successfully', 'success');
        handleCreateClose();
      },
      onError: (error: Error) => {
        showSnackbar(`Error creating user: ${error.message}`, 'error');
      },
    });
  };

  const handleDelete = (id: number) => {
    setUserToDelete(id);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (userToDelete) {
      deleteUser.mutate(userToDelete, {
        onSuccess: () => {
          showSnackbar('User deleted successfully', 'success');
          setUserToDelete(null);
          setDeleteConfirmationOpen(false);
        },
        onError: (error: Error) => {
          showSnackbar(`Error deleting user: ${error.message}`, 'error');
        },
      });
    }
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleRoleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const roleName = event.target.name;
    if (event.target.checked) {
      setSelectedRoles((prev) => [...prev, roleName]);
    } else {
      setSelectedRoles((prev) => prev.filter((r) => r !== roleName));
    }
  };

  const handleAssignRoles = () => {
    if (selectedUser) {
      assignRoles.mutate(
        { userId: selectedUser.id, roles: selectedRoles },
        {
          onSuccess: () => {
            showSnackbar('Roles assigned successfully', 'success');
          },
          onError: (error: Error) => {
            showSnackbar(`Error assigning roles: ${error.message}`, 'error');
          },
        }
      );
    }
  };

  const handleDeactivate = () => {
    if (selectedUser) {
      deactivateUser.mutate(selectedUser.id, {
        onSuccess: () => {
          showSnackbar('User deactivated successfully', 'success');
        },
        onError: (error: Error) => {
          showSnackbar(`Error deactivating user: ${error.message}`, 'error');
        },
      });
    }
  };

  const handleActivate = () => {
    if (selectedUser) {
      activateUser.mutate(selectedUser.id, {
        onSuccess: () => {
          showSnackbar('User activated successfully', 'success');
        },
        onError: (error: Error) => {
          showSnackbar(`Error activating user: ${error.message}`, 'error');
        },
      });
    }
  };

  const handleResetPassword = () => {
    if (newPassword !== confirmPassword) {
      showSnackbar('Passwords do not match', 'error');
      return;
    }
    if (!passwordRegex.test(newPassword)) {
      showSnackbar('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.', 'warning');
      return;
    }
    if (selectedUser) {
      resetPassword.mutate(
        {
          userId: selectedUser.id,
          request: { newPassword, confirmPassword },
        },
        {
          onSuccess: () => {
            showSnackbar('Password reset successfully', 'success');
          },
          onError: (error: Error) => {
            showSnackbar(`Error resetting password: ${error.message}`, 'error');
          },
        }
      );
    }
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
        <Alert severity="error">Error loading users: {error?.message}</Alert>
      </Container>
    );
  }

  return (
    <Container>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Users
        </Typography>
        <Button variant="contained" onClick={handleCreateOpen}>
          Create User
        </Button>
      </Box>
      {usersPage && usersPage.content.length > 0 ? (
        <>
          <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Username</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Roles</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {usersPage.content.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell component="th" scope="row">
                      {user.username}
                    </TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.roles.join(', ')}</TableCell>
                    <TableCell>
                      <Chip label={user.enabled ? 'Active' : 'Inactive'} color={user.enabled ? 'success' : 'error'} size="small" />
                    </TableCell>
                    <TableCell align="right">
                      <Button size="small" onClick={() => handleManageOpen(user)} sx={{ mr: 1 }}>
                        Manage
                      </Button>
                      <IconButton
                        aria-label="delete"
                        onClick={() => handleDelete(user.id)}
                        color="error"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={usersPage.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </>
      ) : (
        <Typography>No users found.</Typography>
      )}

      {/* Create User Dialog */}
      <Dialog open={createOpen} onClose={handleCreateClose}>
        <DialogTitle>Create New User</DialogTitle>
        <DialogContent>
          <TextField name="username" label="Username" fullWidth margin="dense" onChange={handleNewUserChange} required />
          <TextField name="email" label="Email" type="email" fullWidth margin="dense" onChange={handleNewUserChange} required />
          <TextField name="password" label="Password" type="password" fullWidth margin="dense" onChange={handleNewUserChange} required />
          <TextField name="firstName" label="First Name" fullWidth margin="dense" onChange={handleNewUserChange} required />
          <TextField name="lastName" label="Last Name" fullWidth margin="dense" onChange={handleNewUserChange} required />
          <TextField name="phoneNumber" label="Phone Number" fullWidth margin="dense" onChange={handleNewUserChange} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCreateClose}>Cancel</Button>
          <Button onClick={handleCreateUser} variant="contained" disabled={createUser.isPending}>
            {createUser.isPending ? <CircularProgress size={24} /> : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Manage User Dialog */}
      <Dialog open={manageOpen} onClose={handleManageClose} fullWidth maxWidth="md">
        <DialogTitle>Manage User: {selectedUser?.username}</DialogTitle>
        <DialogContent>
          <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={tabValue} onChange={handleTabChange}>
              <Tab label="Assign Roles" />
              <Tab label="Status" />
              <Tab label="Reset Password" />
            </Tabs>
          </Box>
          {tabValue === 0 && (
            <Box sx={{ p: 3 }}>
              <Typography variant="h6">Roles</Typography>
              <FormGroup>
                {rolesPage?.content.map((role) => (
                  <FormControlLabel
                    key={role.id}
                    control={
                      <Checkbox
                        checked={selectedRoles.includes(role.name)}
                        onChange={handleRoleChange}
                        name={role.name}
                      />
                    }
                    label={role.name}
                  />
                ))}
              </FormGroup>
              <Button onClick={handleAssignRoles} variant="contained" sx={{ mt: 2 }} disabled={assignRoles.isPending}>
                {assignRoles.isPending ? <CircularProgress size={24} /> : 'Save Roles'}
              </Button>
            </Box>
          )}
          {tabValue === 1 && (
            <Box sx={{ p: 3 }}>
              <Typography variant="h6">User Status</Typography>
              <Typography>
                Current status: {selectedUser?.enabled ? <Chip label="Active" color="success" size="small" /> : <Chip label="Inactive" color="error" size="small" />}
              </Typography>
              <Box sx={{ mt: 2 }}>
                <Button onClick={handleDeactivate} variant="contained" color="warning" sx={{ mr: 2 }} disabled={deactivateUser.isPending || !selectedUser?.enabled}>
                  {deactivateUser.isPending ? <CircularProgress size={24} /> : 'Deactivate User'}
                </Button>
                <Button onClick={handleActivate} variant="contained" color="success" disabled={activateUser.isPending || selectedUser?.enabled}>
                  {activateUser.isPending ? <CircularProgress size={24} /> : 'Activate User'}
                </Button>
              </Box>
            </Box>
          )}
          {tabValue === 2 && (
            <Box sx={{ p: 3 }}>
              <Typography variant="h6">Reset Password</Typography>
              <TextField
                label="New Password"
                type="password"
                fullWidth
                margin="dense"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
              />
              <TextField
                label="Confirm New Password"
                type="password"
                fullWidth
                margin="dense"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
              <Button onClick={handleResetPassword} variant="contained" sx={{ mt: 2 }} disabled={resetPassword.isPending}>
                {resetPassword.isPending ? <CircularProgress size={24} /> : 'Reset Password'}
              </Button>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleManageClose}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog open={deleteConfirmationOpen} onClose={() => setDeleteConfirmationOpen(false)}>
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Are you sure you want to delete this user? This action cannot be undone.</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>Cancel</Button>
          <Button onClick={confirmDelete} color="error" variant="contained" disabled={deleteUser.isPending}>
            {deleteUser.isPending ? <CircularProgress size={24} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default UsersPage;