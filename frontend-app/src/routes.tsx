import React from 'react';
import { RouteObject, Navigate } from 'react-router-dom';
import DashboardPage from './pages/DashboardPage';
import PropertiesPage from './pages/PropertiesPage';
import PropertyDetailsPage from './pages/PropertyDetailsPage';
import FloorsPage from './pages/FloorsPage';
import FloorDetailsPage from './pages/FloorDetailsPage';
import UnitsPage from './pages/UnitsPage';
import UnitDetailsPage from './pages/UnitDetailsPage';
import UsersPage from './pages/UsersPage';
import RolesPage from './pages/RolesPage';
import PermissionsPage from './pages/PermissionsPage';
import ProfilePage from './pages/ProfilePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import NotFoundPage from './pages/NotFoundPage';
import PrivateRoute from './components/PrivateRoute';
import Layout from './components/Layout';

export const routes: RouteObject[] = [
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/register',
    element: <RegisterPage />,
  },
  {
    element: <PrivateRoute />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: '/',
            element: <Navigate to="/dashboard" replace />,
          },
          {
            path: '/dashboard',
            element: <DashboardPage />,
          },
          {
            path: '/properties',
            element: <PropertiesPage />,
          },
          {
            path: '/properties/:id',
            element: <PropertyDetailsPage />,
          },
          {
            path: '/floors',
            element: <FloorsPage />,
          },
          {
            path: '/floors/:id',
            element: <FloorDetailsPage />,
          },
          {
            path: '/units',
            element: <UnitsPage />,
          },
          {
            path: '/units/:id',
            element: <UnitDetailsPage />,
          },
          {
            path: '/users',
            element: <UsersPage />,
          },
          {
            path: '/roles',
            element: <RolesPage />,
          },
          {
            path: '/permissions',
            element: <PermissionsPage />,
          },
          {
            path: '/profile',
            element: <ProfilePage />,
          },
        ],
      },
    ],
  },
  {
    path: '*',
    element: <NotFoundPage />,
  },
];
