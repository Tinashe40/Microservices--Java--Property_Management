import React, { useState } from 'react';
import { Typography, Container, CircularProgress, Grid, Card, CardContent, Alert, Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from '@mui/material';
import { usePermissions, useCreatePermission, useUpdatePermission, useDeletePermission } from '../api/permissionsService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const PermissionsPage = () => {
  const { data, isLoading, isError, error } = usePermissions();
  const createPermission = useCreatePermission();
  const updatePermission = useUpdatePermission();
  const deletePermission = useDeletePermission();

  const [open, setOpen] = useState(false);
  const [selectedPermission, setSelectedPermission] = useState<any>(null);
  const [name, setName] = useState('');
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [permissionToDelete, setPermissionToDelete] = useState<string | null>(null);

  const handleClickOpen = (permission: any) => {
    setSelectedPermission(permission);
    setName(permission ? permission.name : '');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedPermission(null);
    setName('');
  };

  const handleSave = () => {
    if (selectedPermission) {
      updatePermission.mutate({ ...selectedPermission, name });
    } else {
      createPermission.mutate({ name });
    }
    handleClose();
  };

  const handleDelete = (id: string) => {
    setPermissionToDelete(id);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (permissionToDelete) {
      deletePermission.mutate(permissionToDelete);
      setPermissionToDelete(null);
    }
    setDeleteConfirmationOpen(false);
  };

  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
        <Typography>Loading permissions...</Typography>
      </Container>
    );
  }

  if (isError) {
    return (
      <Container>
        <Alert severity="error">Error loading permissions: {error?.message}</Alert>
      </Container>
    );
  }

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Permissions
      </Typography>
      <Button variant="contained" onClick={() => handleClickOpen(null)} sx={{ mb: 2 }}>
        Create Permission
      </Button>
      {data && data.length > 0 ? (
        <Grid container spacing={3}>
          {data.map((permission) => (
            <Grid key={permission.id} item xs={12} sm={6} md={4}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{permission.name}</Typography>
                  <IconButton edge="end" aria-label="edit" onClick={() => handleClickOpen(permission)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton edge="end" aria-label="delete" onClick={() => handleDelete(permission.id)}>
                    <DeleteIcon />
                  </IconButton>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : (
        <Typography>No permissions found.</Typography>
      )}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{selectedPermission ? 'Edit Permission' : 'Create Permission'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Permission Name"
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
          <Typography>Are you sure you want to delete this permission?</Typography>
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

export default PermissionsPage;
