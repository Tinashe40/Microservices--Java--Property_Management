import React, { useState } from 'react';
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
  Chip,
  LinearProgress,
} from '@mui/material';
import {
  useFloorsByProperty,
  useCreateFloor,
  useUpdateFloor,
  useDeleteFloor,
  useFloorOccupancyStats,
  useRefreshFloorOccupancy,
  Floor,
} from '../api/floorService';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';

interface FloorCardProps {
  floor: Floor;
  onEdit: (floor: Floor) => void;
  onDelete: (floor: Floor) => void;
}

const FloorCard: React.FC<FloorCardProps> = ({ floor, onEdit, onDelete }) => {
  const { data: stats, isLoading, isError, error } = useFloorOccupancyStats(floor.id);
  const refreshOccupancy = useRefreshFloorOccupancy();

  const handleRefresh = () => {
    refreshOccupancy.mutate(floor.id);
  };

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography variant="h6" gutterBottom>{floor.name}</Typography>
        {isLoading && <CircularProgress size={20} />}
        {isError && <Alert severity="error" sx={{ mt: 1 }}>{error.message}</Alert>}
        {stats && (
          <Box sx={{ mt: 2 }}>
            <Typography variant="body2">Occupancy: {stats.occupancyRate.toFixed(1)}%</Typography>
            <LinearProgress variant="determinate" value={stats.occupancyRate} sx={{ my: 1 }} />
            <Chip label={`Total Units: ${stats.totalUnits}`} size="small" sx={{ mr: 1 }} />
            <Chip label={`Occupied: ${stats.occupiedUnits}`} size="small" color="primary" />
          </Box>
        )}
      </CardContent>
      <CardActions>
        <IconButton size="small" onClick={() => onEdit(floor)}><EditIcon /></IconButton>
        <IconButton size="small" onClick={() => onDelete(floor)}><DeleteIcon /></IconButton>
        <IconButton size="small" onClick={handleRefresh} disabled={refreshOccupancy.isPending}>
          <RefreshIcon />
        </IconButton>
      </CardActions>
    </Card>
  );
};

interface FloorManagerProps {
  propertyId: number;
}

const FloorManager: React.FC<FloorManagerProps> = ({ propertyId }) => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(6);
  const { data, isLoading, isError, error } = useFloorsByProperty(propertyId, page, rowsPerPage);
  const createFloor = useCreateFloor();
  const updateFloor = useUpdateFloor();
  const deleteFloor = useDeleteFloor();

  const [open, setOpen] = useState(false);
  const [selectedFloor, setSelectedFloor] = useState<Floor | null>(null);
  const [name, setName] = useState('');
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [floorToDelete, setFloorToDelete] = useState<Floor | null>(null);

  const handleClickOpen = (floor: Floor | null) => {
    setSelectedFloor(floor);
    setName(floor ? floor.name : '');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedFloor(null);
    setName('');
    createFloor.reset();
    updateFloor.reset();
  };

  const handleSave = () => {
    if (selectedFloor) {
      updateFloor.mutate({ ...selectedFloor, name }, {
        onSuccess: handleClose,
      });
    } else {
      if (!propertyId) {
        alert('Property ID is missing. Cannot create floor.');
        return;
      }
      createFloor.mutate({ name, propertyId }, {
        onSuccess: handleClose,
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
        onSuccess: () => setDeleteConfirmationOpen(false),
      });
    }
  };

  if (isLoading) {
    return <CircularProgress />;
  }

  if (isError) {
    return <Alert severity="error">Error loading floors: {error?.message}</Alert>;
  }

  return (
    <Box>
      <Button variant="contained" onClick={() => handleClickOpen(null)} sx={{ mb: 3 }}>
        Add New Floor
      </Button>
      <Grid container spacing={3}>
        {data?.content.map((floor: Floor) => (
          <Grid item key={floor.id} xs={12} sm={6} md={4}>
            <FloorCard floor={floor} onEdit={handleClickOpen} onDelete={handleDelete} />
          </Grid>
        ))}
      </Grid>
      
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>{selectedFloor ? 'Edit Floor' : 'Create New Floor'}</DialogTitle>
        <DialogContent>
          {(createFloor.isError || updateFloor.isError) && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {createFloor.error?.message || updateFloor.error?.message}
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
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={createFloor.isPending || updateFloor.isPending}>
            {createFloor.isPending || updateFloor.isPending ? <CircularProgress size={24} /> : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={deleteConfirmationOpen} onClose={() => setDeleteConfirmationOpen(false)}>
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Are you sure you want to delete this floor? This cannot be undone.</Typography>
          {deleteFloor.isError && <Alert severity="error" sx={{ mt: 2 }}>{deleteFloor.error.message}</Alert>}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>Cancel</Button>
          <Button onClick={confirmDelete} color="error" variant="contained" disabled={deleteFloor.isPending}>
            {deleteFloor.isPending ? <CircularProgress size={24} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default FloorManager;