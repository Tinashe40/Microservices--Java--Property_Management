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
  Checkbox,
  FormControl,
  FormGroup,
  FormControlLabel,
  FormLabel,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Box,
  Grid,
} from '@mui/material';
import {
  useRoles,
  useCreateRole,
  useUpdateRole,
  useDeleteRole,
  Role,
} from '../api/rolesService';
import { usePermissions } from '../api/permissionsService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const RolesPage = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const {
    data: rolesPage,
    isLoading,
    isError,
    error,
  } = useRoles(page, rowsPerPage);
  const { data: permissionsPage } = usePermissions(0, 1000); // Fetch all permissions

  const createRole = useCreateRole();
  const updateRole = useUpdateRole();
  const deleteRole = useDeleteRole();

  const [open, setOpen] = useState(false);
  const [selectedRole, setSelectedRole] = useState<Role | null>(null);
  const [name, setName] = useState('');
  const [selectedPermissions, setSelectedPermissions] = useState<string[]>([]);
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [roleToDelete, setRoleToDelete] = useState<number | null>(null);

  useEffect(() => {
    if (selectedRole) {
      setName(selectedRole.name);
      setSelectedPermissions(selectedRole.permissions || []);
    } else {
      setName('');
      setSelectedPermissions([]);
    }
  }, [selectedRole]);

  const handleClickOpen = (role: Role | null) => {
    setSelectedRole(role);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedRole(null);
  };

  const handleSave = () => {
    if (selectedRole) {
      updateRole.mutate({
        ...selectedRole,
        name,
        permissions: selectedPermissions,
      });
    } else {
      // @ts-ignore
      createRole.mutate({ name, permissions: selectedPermissions });
    }
    handleClose();
  };

  const handleDelete = (id: number) => {
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

  const handlePermissionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const permissionName = event.target.name;
    if (event.target.checked) {
      setSelectedPermissions((prev) => [...prev, permissionName]);
    } else {
      setSelectedPermissions((prev) =>
        prev.filter((p) => p !== permissionName)
      );
    }
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
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
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Roles
        </Typography>
        <Button
          variant="contained"
          onClick={() => handleClickOpen(null)}
        >
          Create Role
        </Button>
      </Box>
      {rolesPage && rolesPage.content.length > 0 ? (
        <>
          <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Role Name</TableCell>
                  <TableCell>Permissions</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {rolesPage.content.map((role) => (
                  <TableRow key={role.id}>
                    <TableCell component="th" scope="row">
                      {role.name}
                    </TableCell>
                    <TableCell>{role.permissions.length}</TableCell>
                    <TableCell align="right">
                      <IconButton
                        aria-label="edit"
                        onClick={() => handleClickOpen(role)}
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton
                        aria-label="delete"
                        onClick={() => handleDelete(role.id)}
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
            count={rolesPage.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </>
      ) : (
        <Typography>No roles found.</Typography>
      )}
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
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
          <FormControl component="fieldset" sx={{ mt: 3 }}>
            <FormLabel component="legend">Permissions</FormLabel>
            <FormGroup>
              <Grid container>
                {permissionsPage?.content.map((permission) => (
                  <Grid item xs={6} key={permission.id}>
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={selectedPermissions.includes(permission.name)}
                          onChange={handlePermissionChange}
                          name={permission.name}
                        />
                      }
                      label={permission.name}
                    />
                  </Grid>
                ))}
              </Grid>
            </FormGroup>
          </FormControl>
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
          <Button onClick={() => setDeleteConfirmationOpen(false)}>
            Cancel
          </Button>
          <Button onClick={confirmDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default RolesPage;
