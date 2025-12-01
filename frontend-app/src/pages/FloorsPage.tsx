import React, { useState, useMemo } from 'react';
import {
  Typography,
  Container,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  Alert,
  Button,
  IconButton,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
  TablePagination,
  Paper,
  InputAdornment,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
  Chip,
  LinearProgress,
} from '@mui/material';
import {
  useFloorsByProperty,
  useCreateFloor,
  useDeleteFloor,
  useUpdateFloor,
  useFloorOccupancyStats,
  Floor,
} from '../api/floorService';
import { useProperties } from '../api/propertyService';
import { useNavigate } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import EditIcon from '@mui/icons-material/Edit';
import { useSnackbar } from '../contexts/SnackbarProvider';

const FloorCard: React.FC<{ floor: Floor, onEdit: (floor: Floor) => void, onDelete: (floor: Floor) => void }> = ({ floor, onEdit, onDelete }) => {
  const navigate = useNavigate();
  const { data: stats, isLoading } = useFloorOccupancyStats(floor.id);

  return (
    <Card sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography variant="h6" gutterBottom>{floor.name}</Typography>
        {isLoading ? <CircularProgress size={20} /> : stats && (
          <Box>
            <Typography variant="body2" color="text.secondary">Occupancy: {stats.occupancyRate.toFixed(1)}%</Typography>
            <LinearProgress variant="determinate" value={stats.occupancyRate} sx={{ my: 1 }} />
            <Chip label={`Units: ${stats.totalUnits}`} size="small" />
          </Box>
        )}
      </CardContent>
      <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button size="small" variant="contained" onClick={() => navigate(`/floors/${floor.id}`)}>
          View More
        </Button>
        <Box>
          <IconButton size="small" onClick={() => onEdit(floor)}>
            <EditIcon />
          </IconButton>
          <IconButton size="small" onClick={() => onDelete(floor)} color="error">
            <DeleteIcon />
          </IconButton>
        </Box>
      </Box>
    </Card>
  );
};

const FloorsPage = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);
  const [searchQuery, setSearchQuery] = useState('');
  const { showSnackbar } = useSnackbar();
  const [selectedPropertyId, setSelectedPropertyId] = useState<number | ''>('');

  const { data: propertiesData, isLoading: isLoadingProperties } = useProperties(0, 1000);

  const {
    data: floorsData,
    isLoading: isLoadingFloors,
    isError: isFloorsError,
    error: floorsError,
  } = useFloorsByProperty(selectedPropertyId as number, page, rowsPerPage);

  const createFloor = useCreateFloor();
  const updateFloor = useUpdateFloor();
  const deleteFloor = useDeleteFloor();

  const [open, setOpen] = useState(false);
  const [selectedFloor, setSelectedFloor] = useState<Floor | null>(null);
  const [newFloor, setNewFloor] = useState({
    name: '',
    propertyId: '' as any,
  });
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [floorToDelete, setFloorToDelete] = useState<Floor | null>(null);

  const isLoading = isLoadingFloors || isLoadingProperties;
  const isError = isFloorsError;
  const error = floorsError;

  const handlePropertyChange = (event: SelectChangeEvent<number | ''>) => {
    setSelectedPropertyId(event.target.value as number | '');
    setPage(0);
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

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(event.target.value);
    setPage(0);
  };

  const handleClickOpen = (floor: Floor | null) => {
    setSelectedFloor(floor);
    setNewFloor({ name: floor ? floor.name : '', propertyId: floor ? floor.propertyId : selectedPropertyId });
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    if (!newFloor.name || !newFloor.propertyId) {
      showSnackbar('Floor name and property are required', 'warning');
      return;
    }
    if (selectedFloor) {
      updateFloor.mutate({ ...selectedFloor, name: newFloor.name, propertyId: newFloor.propertyId }, {
        onSuccess: () => {
          showSnackbar('Floor updated successfully', 'success');
          handleClose();
        },
        onError: (error: Error) => {
          showSnackbar(`Error updating floor: ${error.message}`, 'error');
        },
      });
    } else {
      createFloor.mutate({ name: newFloor.name, propertyId: newFloor.propertyId }, {
        onSuccess: () => {
          showSnackbar('Floor created successfully', 'success');
          handleClose();
        },
        onError: (error: Error) => {
          showSnackbar(`Error creating floor: ${error.message}`, 'error');
        },
      });
    }
  };

  const handleDelete = (floor: Floor) => {
    setFloorToDelete(floor);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (floorToDelete) {
      deleteFloor.mutate(floorToDelete, {
        onSuccess: () => {
          showSnackbar('Floor deleted successfully', 'success');
          setDeleteConfirmationOpen(false);
        },
        onError: (error: Error) => {
          showSnackbar(`Error deleting floor: ${error.message}`, 'error');
        },
      });
    }
  };

  return (
    <Container maxWidth="lg">
      <Paper elevation={0} sx={{ p: 3, mb: 3, borderRadius: 4, backgroundColor: 'rgba(255, 255, 255, 0.7)', backdropFilter: 'blur(10px)' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            Floors
          </Typography>
        </Box>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={4}>
            <FormControl fullWidth>
              <InputLabel>Select Property</InputLabel>
              <Select
                value={selectedPropertyId}
                onChange={handlePropertyChange}
                label="Select Property"
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {propertiesData?.content.map((property) => (
                  <MenuItem key={property.id} value={property.id}>
                    {property.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={5}>
            <TextField
              variant="outlined"
              placeholder="Search by name..."
              value={searchQuery}
              onChange={handleSearchChange}
              fullWidth
              disabled={!selectedPropertyId}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} sm={3} sx={{ textAlign: 'right' }}>
            <Button variant="contained" onClick={() => handleClickOpen(null)} disabled={!selectedPropertyId}>
              Create Floor
            </Button>
          </Grid>
        </Grid>
      </Paper>

      {isLoading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <CircularProgress />
        </Box>
      )}
      {isError && selectedPropertyId && (
        <Alert severity="error">Error loading floors: {error?.message}</Alert>
      )}
      {!selectedPropertyId && (
        <Alert severity="info">Please select a property to view its floors.</Alert>
      )}
      {!isLoading && !isError && floorsData && floorsData.content.length > 0 ? (
        <>
          <Grid container spacing={3}>
            {floorsData.content.map((floor) => (
              <Grid key={floor.id} item xs={12} sm={6} md={4}>
                <FloorCard floor={floor} onEdit={handleClickOpen} onDelete={handleDelete} />
              </Grid>
            ))}
          </Grid>
          <TablePagination
            component="div"
            count={floorsData.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            rowsPerPageOptions={[9, 18, 27]}
          />
        </>
      ) : (
        !isLoading && selectedPropertyId && <Typography sx={{ mt: 4, textAlign: 'center' }}>No floors found for this property.</Typography>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{selectedFloor ? 'Edit Floor' : 'Create Floor'}</DialogTitle>
        <DialogContent>
          {(createFloor.isError || updateFloor.isError) && (
            <Alert severity="error">
              {createFloor.error?.message || updateFloor.error?.message || 'An error occurred'}
            </Alert>
          )}
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Floor Name"
            type="text"
            fullWidth
            variant="outlined"
            value={newFloor.name}
            onChange={(e) =>
              setNewFloor({ ...newFloor, name: e.target.value })
            }
          />
          <FormControl fullWidth margin="dense" variant="outlined" disabled>
            <InputLabel>Property</InputLabel>
            <Select
              name="propertyId"
              label="Property"
              value={newFloor.propertyId}
            >
              {propertiesData?.content.map((property) => (
                <MenuItem key={property.id} value={property.id}>
                  {property.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={createFloor.isPending || updateFloor.isPending}>
            {createFloor.isPending || updateFloor.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={deleteConfirmationOpen}
        onClose={() => setDeleteConfirmationOpen(false)}
      >
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this floor? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>
            Cancel
          </Button>
          <Button onClick={confirmDelete} color="error" variant="contained" disabled={deleteFloor.isPending}>
            {deleteFloor.isPending ? <CircularProgress size={24} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default FloorsPage;
