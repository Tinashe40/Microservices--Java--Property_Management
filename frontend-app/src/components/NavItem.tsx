import React from 'react';
import { NavLink } from 'react-router-dom';
import { ListItemButton, ListItemIcon, ListItemText } from '@mui/material';

interface NavItemProps {
  to: string;
  icon: React.ReactElement;
  text: string;
  sx?: object;
}

const NavItem: React.FC<NavItemProps> = ({ to, icon, text, sx }) => {
  const navLinkStyle = {
    textDecoration: 'none',
    color: 'inherit',
  };

  return (
    <NavLink to={to} style={navLinkStyle}>
      {({ isActive }) => (
        <ListItemButton selected={isActive} sx={sx}>
          <ListItemIcon>{icon}</ListItemIcon>
          <ListItemText primary={text} />
        </ListItemButton>
      )}
    </NavLink>
  );
};

export default NavItem;
