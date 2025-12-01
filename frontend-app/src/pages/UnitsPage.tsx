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
  Chip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
} from '@mui/material';
import {
  useUnitsByProperty,
  useUnitsByFloor,
  useCreateUnit,
  useDeleteUnit,
  useUpdateUnit,
  Unit,
  OccupancyStatus,
} from '../api/unitService';
import { useFloorsByProperty } from '../api/floorService';
import { useProperties } from '../api/propertyService';
import { useNavigate } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import EditIcon from '@mui/icons-material/Edit';
import { useSnackbar } from '../contexts/SnackbarProvider';

const getStatusColor = (status: OccupancyStatus) => {
  switch (status) {
    case OccupancyStatus.AVAILABLE: return 'success';
    case OccupancyStatus.OCCUPIED: return 'error';
    case OccupancyStatus.RESERVED: return 'warning';
    case OccupancyStatus.UNDER_MAINTENANCE: return 'info';
    default: return 'default';
  }
};

const UnitCard: React.FC<{ unit: Unit, onEdit: (unit: Unit) => void, onDelete: (unit: Unit) => void }> = ({ unit, onEdit, onDelete }) => {
  const navigate = useNavigate();

  return (
    <Card sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <CardContent sx={{ flexGrow: 1 }}>
        <Box display="flex" justifyContent="space-between" alignItems="flex-start">
          <Typography variant="h6" gutterBottom>{unit.name}</Typography>
          <Chip label={unit.occupancyStatus} color={getStatusColor(unit.occupancyStatus)} size="small" />
        </Box>
        <Typography color="text.secondary">
          {unit.tenant ? `Tenant: ${unit.tenant}` : 'No Tenant'}
        </Typography>
        <Typography color="text.secondary">
          Rent: ${unit.monthlyRent?.toFixed(2) ?? 'N/A'}
        </Typography>
      </CardContent>
      <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button size="small" variant="contained" onClick={() => navigate(`/units/${unit.id}`)}>
          View More
        </Button>
        <Box>
          <IconButton size="small" onClick={() => onEdit(unit)}>
            <EditIcon />
          </IconButton>
          <IconButton size="small" onClick={() => onDelete(unit)} color="error">
            <DeleteIcon />
          </IconButton>
        </Box>
      </Box>
    </Card>
  );
};

const UnitsPage = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);
  const [searchQuery, setSearchQuery] = useState('');
  const { showSnackbar } = useSnackbar();
  const [selectedPropertyId, setSelectedPropertyId] = useState<number | ''>('');
  const [selectedFloorId, setSelectedFloorId] = useState<number | ''>('');

  const { data: propertiesData, isLoading: isLoadingProperties } = useProperties(0, 1000);
  const { data: floorsData, isLoading: isLoadingFloors } = useFloorsByProperty(selectedPropertyId as number, 0, 1000);

  const {
    data: unitsByFloorData,
    isLoading: isLoadingUnitsByFloor,
    isError: isUnitsByFloorError,
    error: unitsByFloorError,
  } = useUnitsByFloor(selectedFloorId as number, page, rowsPerPage);

  const {
    data: unitsByPropertyData,
    isLoading: isLoadingUnitsByProperty,
    isError: isUnitsByPropertyError,
    error: unitsByPropertyError,
  } = useUnitsByProperty(selectedPropertyId as number, page, rowsPerPage);

  const unitsData = selectedFloorId ? unitsByFloorData : unitsByPropertyData;
  const isLoadingUnits = selectedFloorId ? isLoadingUnitsByFloor : isLoadingUnitsByProperty;
  const isUnitsError = selectedFloorId ? isUnitsByFloorError : isUnitsByPropertyError;
  const unitsError = selectedFloorId ? unitsByFloorError : unitsByPropertyError;

  const createUnit = useCreateUnit();
  const updateUnit = useUpdateUnit();
  const deleteUnit = useDeleteUnit();

  const [open, setOpen] = useState(false);
  const [selectedUnit, setSelectedUnit] = useState<Unit | null>(null);
  const [newUnit, setNewUnit] = useState<Omit<Unit, 'id'>>({
    name: '',
    propertyId: 0,
    floorId: 0,
    occupancyStatus: OccupancyStatus.AVAILABLE,
    tenant: '',
    monthlyRent: 0,
  });
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [unitToDelete, setUnitToDelete] = useState<Unit | null>(null);

  const isLoading = isLoadingUnits || isLoadingProperties || isLoadingFloors;
  const isError = isUnitsError;
  const error = unitsError;

  const handlePropertyChange = (event: SelectChangeEvent<number | ''>) => {
    setSelectedPropertyId(event.target.value as number | '');
    setSelectedFloorId('');
    setPage(0);
  };

  const handleFloorChange = (event: SelectChangeEvent<number | ''>) => {
    setSelectedFloorId(event.target.value as number | '');
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

  const handleClickOpen = (unit: Unit | null) => {
    setSelectedUnit(unit);
    setNewUnit(unit ? { ...unit } : {
      name: '',
      propertyId: selectedPropertyId || 0,
      floorId: selectedFloorId || 0,
      occupancyStatus: OccupancyStatus.AVAILABLE,
      tenant: '',
      monthlyRent: 0,
    });
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | { name?: string; value: unknown }> | SelectChangeEvent<any>) => {
    setNewUnit({ ...newUnit, [e.target.name!]: e.target.value });
  };

  const handleSave = () => {
    if (selectedUnit) {
      updateUnit.mutate({ ...selectedUnit, ...newUnit }, {
        onSuccess: () => {
          showSnackbar('Unit updated successfully', 'success');
          handleClose();
        },
        onError: (error: Error) => {
          showSnackbar(`Error updating unit: ${error.message}`, 'error');
        }
      });
    } else {
      createUnit.mutate(newUnit, {
        onSuccess: () => {
          showSnackbar('Unit created successfully', 'success');
          handleClose();
        },
        onError: (error: Error) => {
          showSnackbar(`Error creating unit: ${error.message}`, 'error');
        }
      });
    }
  };

  const handleDelete = (unit: Unit) => {
    setUnitToDelete(unit);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (unitToDelete) {
      deleteUnit.mutate(unitToDelete, {
        onSuccess: () => {
          showSnackbar('Unit deleted successfully', 'success');
          setDeleteConfirmationOpen(false);
        },
        onError: (error: Error) => {
          showSnackbar(`Error deleting unit: ${error.message}`, 'error');
        }
      });
    }
  };

  return (
    <Container maxWidth="lg">
      <Paper elevation={0} sx={{ p: 3, mb: 3, borderRadius: 4, backgroundColor: 'rgba(255, 255, 255, 0.7)', backdropFilter: 'blur(10px)' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            Units
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
          <Grid item xs={12} sm={4}>
            <FormControl fullWidth disabled={!selectedPropertyId}>
              <InputLabel>Select Floor</InputLabel>
              <Select
                value={selectedFloorId}
                onChange={handleFloorChange}
                label="Select Floor"
              >
                <MenuItem value="">
                  <em>All Floors</em>
                </MenuItem>
                {floorsData?.content.map((floor) => (
                  <MenuItem key={floor.id} value={floor.id}>
                    {floor.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={4} sx={{ textAlign: 'right' }}>
            <Button variant="contained" onClick={() => handleClickOpen(null)} disabled={!selectedPropertyId}>
              Create Unit
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
        <Alert severity="error">Error loading units: {error?.message}</Alert>
      )}
      {!selectedPropertyId && (
        <Alert severity="info">Please select a property to view its units.</Alert>
      )}
      {!isLoading && !isError && unitsData && unitsData.content.length > 0 ? (
        <>
          <Grid container spacing={3}>
            {unitsData.content.map((unit) => (
              <Grid key={unit.id} item xs={12} sm={6} md={4}>
                <UnitCard unit={unit} onEdit={handleClickOpen} onDelete={handleDelete} />
              </Grid>
            ))}
          </Grid>
          <TablePagination
            component="div"
            count={unitsData.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            rowsPerPageOptions={[9, 18, 27]}
          />
        </>
      ) : (
        !isLoading && selectedPropertyId && <Typography sx={{ mt: 4, textAlign: 'center' }}>No units found.</Typography>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{selectedUnit ? 'Edit Unit' : 'Create Unit'}</DialogTitle>
        <DialogContent>
          {(createUnit.isError || updateUnit.isError) && (
            <Alert severity="error">
              {createUnit.error?.message || updateUnit.error?.message || 'An error occurred'}
            </Alert>
          )}
          <TextField autoFocus margin="dense" name="name" label="Unit Name" type="text" fullWidth variant="outlined" value={newUnit.name} onChange={handleInputChange} />
          <FormControl fullWidth margin="dense" disabled>
            <InputLabel>Property</InputLabel>
            <Select name="propertyId" value={newUnit.propertyId} onChange={handleInputChange} label="Property">
              {propertiesData?.content.map(property => (
                <MenuItem key={property.id} value={property.id}>{property.name}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="dense" disabled={!!selectedUnit}>
            <InputLabel>Floor</InputLabel>
            <Select name="floorId" value={newUnit.floorId} onChange={handleInputChange} label="Floor">
              {floorsData?.content.map(floor => (
                <MenuItem key={floor.id} value={floor.id}>{floor.name}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="dense">
            <InputLabel>Occupancy Status</InputLabel>
            <Select name="occupancyStatus" value={newUnit.occupancyStatus} onChange={handleInputChange} label="Occupancy Status">
              {Object.values(OccupancyStatus).map(status => (
                <MenuItem key={status} value={status}>{status}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField margin="dense" name="tenant" label="Tenant" type="text" fullWidth variant="outlined" value={newUnit.tenant} onChange={handleInputChange} />
          <TextField margin="dense" name="monthlyRent" label="Monthly Rent" type="number" fullWidth variant="outlined" value={newUnit.monthlyRent} onChange={handleInputChange} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={createUnit.isPending || updateUnit.isPending}>
            {createUnit.isPending || updateUnit.isPending ? <CircularProgress size={24} /> : 'Save'}
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
            Are you sure you want to delete this unit? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>
            Cancel
          </Button>
          <Button onClick={confirmDelete} color="error" variant="contained" disabled={deleteUnit.isPending}>
            {deleteUnit.isPending ? <CircularProgress size={24} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default UnitsPage;
