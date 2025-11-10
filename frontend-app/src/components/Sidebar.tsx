import React from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  Collapse,
  Box,
} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useCurrentUser } from '../api/authService';

interface SidebarProps {
  drawerWidth: number;
}

const Sidebar: React.FC<SidebarProps> = ({ drawerWidth }) => {
  const [openProperties, setOpenProperties] = React.useState(false);
  const [openUsers, setOpenUsers] = React.useState(false);
  const { data: currentUser } = useCurrentUser();
  const navigate = useNavigate();

  const handlePropertiesClick = () => {
    setOpenProperties(!openProperties);
  };

  const handleUsersClick = () => {
    setOpenUsers(!openUsers);
  };

  const userHasRole = (role: string) => {
    return currentUser?.roles.includes(role);
  };

  const handleLogout = () => {
    sessionStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <Drawer
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
        },
      }}
      variant="permanent"
      anchor="left"
    >
      <Toolbar>
        <Typography variant="h6" noWrap component="div">
          Property Admin
        </Typography>
      </Toolbar>
      <List>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/">
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItemButton>
        </ListItem>

        <ListItemButton onClick={handlePropertiesClick}>
          <ListItemIcon>
            <BusinessIcon />
          </ListItemIcon>
          <ListItemText primary="Properties Management" />
          {openProperties ? <ExpandLess /> : <ExpandMore />}
        </ListItemButton>
        <Collapse in={openProperties} timeout="auto" unmountOnExit>
          <List component="div" disablePadding>
            <ListItemButton sx={{ pl: 4 }} component={RouterLink} to="/properties">
              <ListItemText primary="All Properties" />
            </ListItemButton>
            {/* Add more sub-menu items for properties if needed */}
          </List>
        </Collapse>

        {userHasRole('ADMIN') && (
          <>
            <ListItemButton onClick={handleUsersClick}>
              <ListItemIcon>
                <PeopleIcon />
              </ListItemIcon>
              <ListItemText primary="User Management" />
              {openUsers ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>
            <Collapse in={openUsers} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <ListItemButton sx={{ pl: 4 }} component={RouterLink} to="/users">
                  <ListItemText primary="All Users" />
                </ListItemButton>
                <ListItemButton sx={{ pl: 4 }} component={RouterLink} to="/roles">
                  <ListItemText primary="All Roles" />
                </ListItemButton>
                <ListItemButton sx={{ pl: 4 }} component={RouterLink} to="/permissions">
                  <ListItemText primary="All Permissions" />
                </ListItemButton>
                {/* Add more sub-menu items for users if needed */}
              </List>
            </Collapse>
          </>
        )}
      </List>
      <Box sx={{ flexGrow: 1 }} />
      <ListItem disablePadding>
        <ListItemButton component={RouterLink} to="/profile">
          <ListItemIcon>
            <AccountCircleIcon />
          </ListItemIcon>
          <ListItemText primary="Profile" />
        </ListItemButton>
      </ListItem>
      <ListItem disablePadding>
        <ListItemButton onClick={handleLogout}>
          <ListItemIcon>
            <ExitToAppIcon />
          </ListItemIcon>
          <ListItemText primary="Logout" />
        </ListItemButton>
      </ListItem>
    </Drawer>
  );
};

export default Sidebar;
