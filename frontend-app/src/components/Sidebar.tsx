import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  Collapse,
  Box,
  Divider,
  Skeleton,
} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import SecurityIcon from '@mui/icons-material/Security';
import ListAltIcon from '@mui/icons-material/ListAlt';
import ApartmentIcon from '@mui/icons-material/Apartment';
import MeetingRoomIcon from '@mui/icons-material/MeetingRoom';
import { useAuth } from '../hooks/useAuth';
import { UserDTO } from '../api/authService';
import NavItem from './NavItem';

interface NavItemConfig {
  text: string;
  icon: React.ReactElement;
  path?: string;
  requiredRoles?: string[];
  subItems?: NavItemConfig[];
}

const navConfig: NavItemConfig[] = [
  { text: 'Home', icon: <HomeIcon />, path: '/dashboard' },
  {
    text: 'Properties',
    icon: <BusinessIcon />,
    subItems: [
      { text: 'All Properties', icon: <ListAltIcon />, path: '/properties' },
      { text: 'All Floors', icon: <ApartmentIcon />, path: '/floors' },
      { text: 'All Units', icon: <MeetingRoomIcon />, path: '/units' },
    ],
  },
  {
    text: 'User Management',
    icon: <PeopleIcon />,
    requiredRoles: ['ADMIN', 'SUPER_ADMIN'],
    subItems: [
      { text: 'Users', icon: <PeopleIcon />, path: '/users' },
      { text: 'Roles', icon: <SecurityIcon />, path: '/roles' },
      { text: 'Permissions', icon: <VpnKeyIcon />, path: '/permissions' },
    ],
  },
];

const hasPermission = (user: UserDTO | null, requiredRoles?: string[]): boolean => {
  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }
  if (!user) {
    return false;
  }
  return requiredRoles.some(role => user.roles.includes(role));
};

interface SidebarProps {
  drawerWidth: number;
}

const Sidebar: React.FC<SidebarProps> = ({ drawerWidth }) => {
  const [open, setOpen] = React.useState<Record<string, boolean>>({});
  const { user, isLoading, logout } = useAuth();
  const navigate = useNavigate();

  const handleClick = (item: string) => {
    setOpen(prev => ({ ...prev, [item]: !prev[item] }));
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const renderNavItems = (items: NavItemConfig[]) => {
    return items.filter(item => hasPermission(user, item.requiredRoles)).map((item) => {
      if (item.subItems) {
        return (
          <React.Fragment key={item.text}>
            <ListItemButton onClick={() => handleClick(item.text)}>
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
              {open[item.text] ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>
            <Collapse in={open[item.text]} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                {renderNavItems(item.subItems)}
              </List>
            </Collapse>
          </React.Fragment>
        );
      }
      return (
        <NavItem
          key={item.path}
          to={item.path!}
          icon={item.icon}
          text={item.text}
          sx={item.path?.split('/').length === 2 ? {} : { pl: 4 }}
        />
      );
    });
  };

  if (isLoading) {
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
        <Toolbar />
        <Divider />
        <List>
          {[...Array(5)].map((_, index) => (
            <ListItemButton key={index}>
              <ListItemIcon>
                <Skeleton variant="circular" width={24} height={24} />
              </ListItemIcon>
              <ListItemText>
                <Skeleton width="80%" />
              </ListItemText>
            </ListItemButton>
          ))}
        </List>
      </Drawer>
    );
  }

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
        <BusinessIcon sx={{ mr: 1 }} />
        <Typography variant="h6" noWrap component="div" sx={{ fontWeight: 700 }}>
          PropManager
        </Typography>
      </Toolbar>
      <Divider />
      <List>{renderNavItems(navConfig)}</List>
      <Box sx={{ flexGrow: 1 }} />
      <Divider />
      <List>
        <NavItem to="/profile" icon={<AccountCircleIcon />} text="Profile" />
        <ListItemButton onClick={handleLogout}>
          <ListItemIcon>
            <ExitToAppIcon />
          </ListItemIcon>
          <ListItemText primary="Logout" />
        </ListItemButton>
      </List>
    </Drawer>
  );
};

export default Sidebar;

