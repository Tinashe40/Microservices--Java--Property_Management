import React, { useState } from 'react';
import { Typography, Container, CircularProgress, Grid, Card, CardContent, Alert, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from '@mui/material';
import { useRoles, useCreateRole, useUpdateRole, useDeleteRole } from '../api/rolesService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const RolesPage = () => {
  const { data, isLoading, isError, error } = useRoles();
  const createRole = useCreateRole();
  const updateRole = useUpdateRole();
  const deleteRole = useDeleteRole();

  const [open, setOpen] = useState(false);
  const [selectedRole, setSelectedRole] = useState<any>(null);
  const [name, setName] = useState('');
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [roleToDelete, setRoleToDelete] = useState<string | null>(null);

  const handleClickOpen = (role: any) => {
    setSelectedRole(role);
    setName(role ? role.name : '');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedRole(null);
    setName('');
  };

  const handleSave = () => {
    if (selectedRole) {
      updateRole.mutate({ ...selectedRole, name });
    } else {
      createRole.mutate({ name });
    }
    handleClose();
  };

  const handleDelete = (id: string) => {
    setRoleToDelete(id);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (roleToDelete) {
      deleteRole.mutate(roleToDelete);
      setRoleToDelete(null);
    }
    setDeleteConfirmationOpen(false);
  };

  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
        <Typography>Loading roles...</Typography>
      </Container>
    );
  }

  if (isError) {
    return (
      <Container>
        <Alert severity="error">Error loading roles: {error?.message}</Alert>
      </Container>
    );
  }

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Roles
      </Typography>
      <Button variant="contained" onClick={() => handleClickOpen(null)} sx={{ mb: 2 }}>
        Create Role
      </Button>
      {data && data.length > 0 ? (
        <Grid container spacing={3}>
          {data.map((role) => (
            <Grid key={role.id} item xs={12} sm={6} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{role.name}</Typography>
                  <IconButton edge="end" aria-label="edit" onClick={() => handleClickOpen(role)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton edge="end" aria-label="delete" onClick={() => handleDelete(role.id)}>
                    <DeleteIcon />
                  </IconButton>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : (
        <Typography>No roles found.</Typography>
      )}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{selectedRole ? 'Edit Role' : 'Create Role'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Role Name"
            type="text"
            fullWidth
            variant="standard"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave}>Save</Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={deleteConfirmationOpen}
        onClose={() => setDeleteConfirmationOpen(false)}
      >
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Are you sure you want to delete this role?</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>Cancel</Button>
          <Button onClick={confirmDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default RolesPage;
