import React, { useState, useEffect, useMemo } from 'react';
import {
  Typography,
  CircularProgress,
  Alert,
  Button,
  IconButton,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
  Grid,
  Card,
  CardContent,
  CardActions,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  SelectChangeEvent,
  Chip,
  Menu,
  InputAdornment,
} from '@mui/material';
import {
  useUnitsByFloor,
  useCreateUnit,
  useUpdateUnit,
  useDeleteUnit,
  useSearchUnits,
  useUpdateUnitOccupancy,
  Unit,
  OccupancyStatus,
} from '../api/unitService';
import { useAllFloorsByProperty as useFloors, Floor } from '../api/floorService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import SearchIcon from '@mui/icons-material/Search';

const getStatusColor = (status: OccupancyStatus) => {
  switch (status) {
    case OccupancyStatus.AVAILABLE: return 'success';
    case OccupancyStatus.OCCUPIED: return 'error';
    case OccupancyStatus.RESERVED: return 'warning';
    case OccupancyStatus.UNDER_MAINTENANCE: return 'info';
    default: return 'default';
  }
};

interface UnitCardProps {
  unit: Unit;
  onEdit: (unit: Unit) => void;
  onDelete: (unit: Unit) => void;
}

const UnitCard: React.FC<UnitCardProps> = ({ unit, onEdit, onDelete }) => {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const updateOccupancy = useUpdateUnitOccupancy();

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleStatusChange = (occupancyStatus: OccupancyStatus) => {
    updateOccupancy.mutate({ unitId: unit.id, occupancyStatus });
    handleMenuClose();
  };

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flexGrow: 1 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Typography variant="h6">{unit.name}</Typography>
          <Chip label={unit.occupancyStatus} color={getStatusColor(unit.occupancyStatus)} size="small" />
        </Box>
        <Typography color="text.secondary">
          {unit.tenant ? `Tenant: ${unit.tenant}` : 'No Tenant'}
        </Typography>
        <Typography color="text.secondary">
          Rent: ${unit.monthlyRent?.toFixed(2) ?? 'N/A'}
        </Typography>
      </CardContent>
      <CardActions>
        <IconButton size="small" onClick={() => onEdit(unit)}><EditIcon /></IconButton>
        <IconButton size="small" onClick={() => onDelete(unit)}><DeleteIcon /></IconButton>
        <IconButton size="small" onClick={handleMenuClick}><MoreVertIcon /></IconButton>
        <Menu
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleMenuClose}
        >
          {Object.values(OccupancyStatus).map(status => (
            <MenuItem key={status} onClick={() => handleStatusChange(status)}>
              Set as {status}
            </MenuItem>
          ))}
        </Menu>
      </CardActions>
    </Card>
  );
};

interface UnitManagerProps {
  propertyId: number;
}

const UnitManager: React.FC<UnitManagerProps> = ({ propertyId }) => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(6);
  const [searchQuery, setSearchQuery] = useState('');

  const { data: floorsData } = useFloors(propertyId);
  const floorId = floorsData?.[0]?.id;
  const { data: searchData, isLoading: isSearching } = useSearchUnits(searchQuery, page, rowsPerPage);
  const { data: unitsData, isLoading: isLoadingUnits, isError, error } = useUnitsByFloor(floorId ?? 0, page, rowsPerPage);
  
  const data = useMemo(() => searchQuery ? searchData : unitsData, [searchQuery, searchData, unitsData]);
  const isLoading = useMemo(() => isSearching || isLoadingUnits, [isSearching, isLoadingUnits]);

  const createUnit = useCreateUnit();
  const updateUnit = useUpdateUnit();
  const deleteUnit = useDeleteUnit();

  const [open, setOpen] = useState(false);
  const [selectedUnit, setSelectedUnit] = useState<Unit | null>(null);
  const [formData, setFormData] = useState<Omit<Unit, 'id'>>({
    name: '',
    floorId: 0,
    propertyId: propertyId,
    occupancyStatus: OccupancyStatus.AVAILABLE,
    tenant: '',
    monthlyRent: 0,
  });
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [unitToDelete, setUnitToDelete] = useState<Unit | null>(null);

  useEffect(() => {
    if (selectedUnit) {
      setFormData({
        name: selectedUnit.name,
        floorId: selectedUnit.floorId,
        propertyId: selectedUnit.propertyId,
        occupancyStatus: selectedUnit.occupancyStatus,
        tenant: selectedUnit.tenant,
        monthlyRent: selectedUnit.monthlyRent,
      });
    } else {
      setFormData({
        name: '',
        floorId: floorsData?.[0]?.id || 0,
        propertyId: propertyId,
        occupancyStatus: OccupancyStatus.AVAILABLE,
        tenant: '',
        monthlyRent: 0,
      });
    }
  }, [selectedUnit, floorsData, propertyId]);

  const handleClickOpen = (unit: Unit | null) => {
    setSelectedUnit(unit);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedUnit(null);
    createUnit.reset();
    updateUnit.reset();
  };

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<any>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    if (selectedUnit) {
      updateUnit.mutate({ ...selectedUnit, ...formData }, { onSuccess: handleClose });
    } else {
      createUnit.mutate(formData, { onSuccess: handleClose });
    }
  };

  const handleDelete = (unit: Unit) => {
    setUnitToDelete(unit);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (unitToDelete) {
      deleteUnit.mutate(unitToDelete, { onSuccess: () => setDeleteConfirmationOpen(false) });
    }
  };

  if (isLoading) {
    return <CircularProgress />;
  }

  if (isError) {
    return <Alert severity="error">Error loading units: {error?.message}</Alert>;
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <TextField
          variant="outlined"
          placeholder="Search by name or tenant..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          sx={{ width: '40%' }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
        />
        <Button variant="contained" onClick={() => handleClickOpen(null)}>
          Add New Unit
        </Button>
      </Box>
      <Grid container spacing={3}>
        {data?.content.map((unit: Unit) => (
          <Grid item key={unit.id} xs={12} sm={6} md={4}>
            <UnitCard unit={unit} onEdit={handleClickOpen} onDelete={handleDelete} />
          </Grid>
        ))}
      </Grid>
      
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>{selectedUnit ? 'Edit Unit' : 'Create New Unit'}</DialogTitle>
        <DialogContent>
          {(createUnit.isError || updateUnit.isError) && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {createUnit.error?.message || updateUnit.error?.message}
            </Alert>
          )}
          <TextField autoFocus margin="dense" name="name" label="Unit Name" type="text" fullWidth variant="outlined" value={formData.name} onChange={handleFormChange} />
          <FormControl fullWidth margin="dense" variant="outlined">
            <InputLabel>Floor</InputLabel>
            <Select name="floorId" label="Floor" value={formData.floorId} onChange={handleFormChange}>
              {floorsData?.map((floor: Floor) => (
                <MenuItem key={floor.id} value={floor.id}>
                  {floor.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="dense" variant="outlined">
            <InputLabel>Occupancy Status</InputLabel>
            <Select name="occupancyStatus" label="Occupancy Status" value={formData.occupancyStatus} onChange={handleFormChange}>
              {Object.values(OccupancyStatus).map((status) => (
                <MenuItem key={status} value={status}>
                  {status}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField margin="dense" name="tenant" label="Tenant" type="text" fullWidth variant="outlined" value={formData.tenant || ''} onChange={handleFormChange} />
          <TextField margin="dense" name="monthlyRent" label="Monthly Rent" type="number" fullWidth variant="outlined" value={formData.monthlyRent || ''} onChange={handleFormChange} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={createUnit.isPending || updateUnit.isPending}>
            {createUnit.isPending || updateUnit.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={deleteConfirmationOpen} onClose={() => setDeleteConfirmationOpen(false)}>
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Are you sure you want to delete this unit?</Typography>
          {deleteUnit.isError && <Alert severity="error" sx={{ mt: 2 }}>{deleteUnit.error.message}</Alert>}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>Cancel</Button>
          <Button onClick={confirmDelete} color="error" variant="contained" disabled={deleteUnit.isPending}>
            {deleteUnit.isPending ? <CircularProgress size={24} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default UnitManager;