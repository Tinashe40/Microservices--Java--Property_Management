import React, { useState, useEffect } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { useSignIn } from '../api/authService';
import { Container, TextField, Button, Typography, Box, Card, CardContent, Alert } from '@mui/material';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { mutate: signIn, isError, error } = useSignIn();
  const [showSuccessAlert, setShowSuccessAlert] = useState(false);

  useEffect(() => {
    if (searchParams.get('registered') === 'true') {
      setShowSuccessAlert(true);
      // Optionally remove the query param after showing the alert
      // navigate(location.pathname, { replace: true });
    }
  }, [searchParams]);

  const handleLogin = () => {
    signIn({ usernameOrEmail: email, password }, {
      onSuccess: (data) => {
        sessionStorage.setItem('token', data.token.accessToken);
        navigate('/');
      },
    });
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(45deg, #7e57c2 30%, #26a69a 90%)',
      }}
    >
      <Container maxWidth="xs">
        <Card>
          <CardContent sx={{ p: 4 }}>
            <Typography component="h1" variant="h5" sx={{ textAlign: 'center', mb: 3 }}>
              Sign in
            </Typography>
            {showSuccessAlert && (
              <Alert severity="success" sx={{ mb: 2 }}>
                Registration successful! Please sign in.
              </Alert>
            )}
            <Box component="form" noValidate sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <Button
                type="button"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={handleLogin}
              >
                Sign In
              </Button>
              {isError && (
                <Typography color="error" sx={{ mt: 2 }}>
                  {error instanceof Error ? error.message : 'An unknown error occurred'}
                </Typography>
              )}
              <Box sx={{ textAlign: 'center', mt: 2 }}>
                <Link to="/register" style={{ textDecoration: 'none' }}>
                  Don't have an account? Sign Up
                </Link>
              </Box>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default LoginPage;
