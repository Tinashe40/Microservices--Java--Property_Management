import { createTheme } from '@mui/material/styles';

const newTheme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#673ab7', // A deep, elegant purple
    },
    secondary: {
      main: '#E91E63', // A vibrant, energetic pink
    },
    background: {
      default: '#f5f5f5', // A very light, neutral gray
      paper: '#ffffff',   // Pure white for paper elements
    },
    text: {
      primary: '#212121', // A dark gray for primary text for better readability
      secondary: '#757575', // A medium gray for secondary text
    },
    divider: '#e0e0e0',
    error: {
      main: '#f44336',
    },
    success: {
      main: '#4caf50',
    },
    warning: {
      main: '#ff9800',
    },
    info: {
      main: '#2196f3',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 700,
      color: '#212121',
      fontSize: '2rem',
      lineHeight: 1.3,
    },
    h5: {
      fontWeight: 600,
      color: '#212121',
    },
    h6: {
      fontWeight: 600,
      color: '#212121',
    },
    subtitle1: {
      color: '#757575',
      fontSize: '1rem',
    },
    body1: {
      fontSize: '1rem',
    },
    body2: {
      fontSize: '0.875rem',
    },
  },
  components: {
    MuiDrawer: {
      styleOverrides: {
        paper: {
          backgroundColor: '#ffffff',
          borderRight: '1px solid #e0e0e0',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 4px 12px rgba(0,0,0,0.08)',
          transition: 'transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out',
          '&:hover': {
            transform: 'translateY(-4px)',
            boxShadow: '0 8px 24px rgba(0,0,0,0.12)',
          },
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          textTransform: 'none',
          fontWeight: 600,
          padding: '10px 20px',
        },
        containedPrimary: {
          color: '#ffffff',
        },
      },
    },
    MuiListItemButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          margin: '4px 12px',
          '&.Mui-selected': {
            backgroundColor: 'rgba(103, 58, 183, 0.1)',
            color: '#673ab7',
            '& .MuiListItemIcon-root': {
              color: '#673ab7',
            },
            '&:hover': {
              backgroundColor: 'rgba(103, 58, 183, 0.15)',
            },
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#ffffff',
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
          color: '#212121',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
          },
        },
      },
    },
    MuiTable: {
      styleOverrides: {
        root: {
          borderCollapse: 'separate',
          borderSpacing: '0 8px',
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          '& .MuiTableCell-root': {
            backgroundColor: '#f5f5f5',
            fontWeight: 600,
            borderBottom: 'none',
          },
        },
      },
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          '& .MuiTableCell-root': {
            borderBottom: '1px solid #e0e0e0',
          },
          '&:last-child .MuiTableCell-root': {
            borderBottom: 'none',
          },
        },
      },
    },
  },
});

export default newTheme;
