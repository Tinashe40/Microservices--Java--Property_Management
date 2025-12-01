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
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Paper,
  InputAdornment,
  Chip,
} from '@mui/material';
import {
  useProperties,
  useCreateProperty,
  useDeleteProperty,
  useSearchProperties,
  usePropertiesCount,
  PropertyType,
  usePropertiesByManager,
  PropertyFilterDTO,
} from '../api/propertyService';
import { useCurrentUser } from '../api/authService';
import { useNavigate } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import BusinessIcon from '@mui/icons-material/Business';

const PropertiesPage = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);
  const [searchQuery, setSearchQuery] = useState('');
  const { data: currentUser } = useCurrentUser();

  const isAdmin = useMemo(() => currentUser?.roles.includes('ADMIN') || currentUser?.roles.includes('SUPER_ADMIN'), [currentUser]);

  const [searchFilter, setSearchFilter] = useState<PropertyFilterDTO>({});

  const { data: propertiesCount } = usePropertiesCount();
  const {
    data: searchData,
    isLoading: isSearching,
    isError: isSearchError,
  } = useSearchProperties(searchFilter, page, rowsPerPage);
  
  const {
    data: propertiesData,
    isLoading: isLoadingProperties,
    isError: isPropertiesError,
    error: propertiesError,
  } = useProperties(page, rowsPerPage);

  const {
    data: managerPropertiesData,
    isLoading: isLoadingManagerProperties,
    isError: isManagerPropertiesError,
    error: managerPropertiesError,
  } = usePropertiesByManager(currentUser?.id ?? 0, page, rowsPerPage);


  const createProperty = useCreateProperty();
  const deleteProperty = useDeleteProperty();

  const [open, setOpen] = useState(false);
  const [newProperty, setNewProperty] = useState({
    name: '',
    address: '',
    propertyType: PropertyType.RESIDENTIAL,
  });
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [propertyToDelete, setPropertyToDelete] = useState<number | null>(null);

  const data = useMemo(() => {
    if (Object.keys(searchFilter).length > 0) return searchData;
    return isAdmin ? propertiesData : managerPropertiesData;
  }, [searchFilter, searchData, isAdmin, propertiesData, managerPropertiesData]);

  const isLoading = useMemo(() => isSearching || isLoadingProperties || isLoadingManagerProperties, [isSearching, isLoadingProperties, isLoadingManagerProperties]);
  const isError = useMemo(() => isSearchError || isPropertiesError || isManagerPropertiesError, [isSearchError, isPropertiesError, isManagerPropertiesError]);
  const error = useMemo(() => propertiesError || managerPropertiesError, [propertiesError, managerPropertiesError]);

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
    const { value } = event.target;
    setSearchQuery(value);
    setSearchFilter({ name: value });
    setPage(0);
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    const propertyToSave = {
      ...newProperty,
      managedBy: isAdmin ? undefined : currentUser?.id,
    };
    // @ts-ignore
    createProperty.mutate(propertyToSave, {
      onSuccess: () => handleClose(),
    });
  };

  const handleDelete = (id: number) => {
    setPropertyToDelete(id);
    setDeleteConfirmationOpen(true);
  };

  const confirmDelete = () => {
    if (propertyToDelete) {
      deleteProperty.mutate(propertyToDelete, {
        onSuccess: () => setDeleteConfirmationOpen(false),
      });
    }
  };

  return (
    <Container maxWidth="lg">
      <Paper elevation={0} sx={{ p: 3, mb: 3, borderRadius: 2, bgcolor: 'rgba(255, 255, 255, 0.7)', backdropFilter: 'blur(10px)' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            Properties
          </Typography>
          <Chip icon={<BusinessIcon />} label={`${propertiesCount ?? '...'} Total Properties`} />
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <TextField
            variant="outlined"
            placeholder="Search by name or address..."
            value={searchQuery}
            onChange={handleSearchChange}
            sx={{ width: '40%' }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
          <Button variant="contained" onClick={handleClickOpen}>
            Create Property
          </Button>
        </Box>
      </Paper>

      {isLoading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <CircularProgress />
        </Box>
      )}
      {isError && (
        <Alert severity="error">Error loading properties: {error?.message}</Alert>
      )}
      {!isLoading && !isError && data && data.content.length > 0 ? (
        <>
          <Grid container spacing={3}>
            {data.content.map((property) => (
              <Grid key={property.id} item xs={12} sm={6} md={4}>
                <Card sx={{ 
                  display: 'flex', 
                  flexDirection: 'column', 
                  height: '100%',
                  '&:hover': {
                    boxShadow: 6,
                  }
                }}>
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography variant="h6" gutterBottom>{property.name}</Typography>
                    <Typography color="text.secondary" sx={{ mb: 1 }}>
                      {property.address}
                    </Typography>
                    <Chip label={property.propertyType} size="small" color="primary" variant="outlined" />
                  </CardContent>
                  <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Button
                      size="small"
                      variant="contained"
                      onClick={() => navigate(`/properties/${property.id}`)}
                    >
                      View Details
                    </Button>
                    <IconButton
                      aria-label="delete"
                      onClick={() => handleDelete(property.id)}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
          <TablePagination
            component="div"
            count={data.totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            rowsPerPageOptions={[9, 18, 27]}
            sx={{ mt: 3 }}
          />
        </>
      ) : (
        !isLoading && <Typography sx={{ mt: 4, textAlign: 'center' }}>No properties found.</Typography>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Create Property</DialogTitle>
        <DialogContent>
          {createProperty.isError && (
            <Alert severity="error">
              {createProperty.error?.message || 'An error occurred'}
            </Alert>
          )}
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Property Name"
            type="text"
            fullWidth
            variant="outlined"
            onChange={(e) =>
              setNewProperty({ ...newProperty, name: e.target.value })
            }
          />
          <TextField
            margin="dense"
            id="address"
            label="Address"
            type="text"
            fullWidth
            variant="outlined"
            onChange={(e) =>
              setNewProperty({ ...newProperty, address: e.target.value })
            }
          />
          <FormControl fullWidth margin="dense" variant="outlined">
            <InputLabel id="property-type-label">Property Type</InputLabel>
            <Select
              labelId="property-type-label"
              id="propertyType"
              value={newProperty.propertyType}
              label="Property Type"
              onChange={(e) =>
                setNewProperty({
                  ...newProperty,
                  propertyType: e.target.value as PropertyType,
                })
              }
            >
              {Object.values(PropertyType).map((type) => (
                <MenuItem key={type} value={type}>
                  {type}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave} variant="contained">Save</Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={deleteConfirmationOpen}
        onClose={() => setDeleteConfirmationOpen(false)}
      >
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this property? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)}>
            Cancel
          </Button>
          <Button onClick={confirmDelete} color="error" variant="contained">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default PropertiesPage;