import React, { useState } from 'react';
import {
  Typography,
  Container,
  CircularProgress,
  Alert,
  TablePagination,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from '@mui/material';
import { usePermissions, useCreatePermission } from '../api/permissionsService';

const PermissionsPage = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const { data, isLoading, isError, error } = usePermissions(page, rowsPerPage);
  const createPermission = useCreatePermission();

  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');

  const handleClickOpen = () => {
    setName('');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    createPermission.reset();
  };

  const handleSave = () => {
    createPermission.mutate({ name }, {
      onSuccess: handleClose,
    });
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
        <Typography>Loading permissions...</Typography>
      </Container>
    );
  }

  if (isError) {
    return (
      <Container>
        <Alert severity="error">
          Error loading permissions: {error?.message}
        </Alert>
      </Container>
    );
  }

  return (
    <Container>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Permissions
        </Typography>
        <Button variant="contained" onClick={handleClickOpen}>
          Create Permission
        </Button>
      </Box>
      {data && data.content.length > 0 ? (
        <>
          <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Permission Name</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.content.map((permission) => (
                  <TableRow key={permission.id}>
                    <TableCell component="th" scope="row">
                      {permission.name}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={data.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </>
      ) : (
        <Typography>No permissions found.</Typography>
      )}
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>Create New Permission</DialogTitle>
        <DialogContent>
          {createPermission.isError && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {createPermission.error?.message}
            </Alert>
          )}
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Permission Name"
            type="text"
            fullWidth
            variant="outlined"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={createPermission.isPending}>
            {createPermission.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default PermissionsPage;
