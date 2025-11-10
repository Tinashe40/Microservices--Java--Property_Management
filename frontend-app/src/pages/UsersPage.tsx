import React, { useState } from 'react';
import { Typography, Container, CircularProgress, Grid, Card, CardContent, Alert, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from '@mui/material';
import { useUsers, useCreateUser, useUpdateUser, useDeleteUser } from '../api/userService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const UsersPage = () => {
  const { data, isLoading, isError, error } = useUsers();
  const createUser = useCreateUser();
  const updateUser = useUpdateUser();
  const deleteUser = useDeleteUser();

  const [open, setOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<any>(null);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');

  const handleClickOpen = (user: any) => {
    setSelectedUser(user);
    setUsername(user ? user.username : '');
    setEmail(user ? user.email : '');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedUser(null);
    setUsername('');
    setEmail('');
  };

  const handleSave = () => {
    if (selectedUser) {
      updateUser.mutate({ ...selectedUser, username, email });
    } else {
      createUser.mutate({ username, email });
    }
    handleClose();
  };

  const handleDelete = (id: string) => {
    deleteUser.mutate(id);
  };

  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
        <Typography>Loading users...</Typography>
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
      <Typography variant="h4" component="h1" gutterBottom>
        Users
      </Typography>
      <Button variant="contained" onClick={() => handleClickOpen(null)} sx={{ mb: 2 }}>
        Create User
      </Button>
      {data && data.length > 0 ? (
        <Grid container spacing={3}>
          {data.map((user) => (
            <Grid key={user.id} xs={12} sm={6} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{user.username}</Typography>
                  <Typography color="text.secondary">{user.email}</Typography>
                  <IconButton edge="end" aria-label="edit" onClick={() => handleClickOpen(user)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton edge="end" aria-label="delete" onClick={() => handleDelete(user.id)}>
                    <DeleteIcon />
                  </IconButton>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : (
        <Typography>No users found.</Typography>
      )}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{selectedUser ? 'Edit User' : 'Create User'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            id="username"
            label="Username"
            type="text"
            fullWidth
            variant="standard"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            margin="dense"
            id="email"
            label="Email Address"
            type="email"
            fullWidth
            variant="standard"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave}>Save</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default UsersPage;