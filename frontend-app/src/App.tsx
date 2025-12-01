import React from 'react';
import { useRoutes } from 'react-router-dom';
import { CssBaseline } from '@mui/material';
import ErrorBoundary from './components/ErrorBoundary';
import { routes } from './routes';

function App() {
  const routing = useRoutes(routes);

  return (
    <ErrorBoundary>
      <CssBaseline />
      {routing}
    </ErrorBoundary>
  );
}

export default App;

