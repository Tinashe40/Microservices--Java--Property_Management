import React, { useState, useEffect } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { useSignIn } from '../api/authService';
import { useAuth } from '../hooks/useAuth';
import { Container, TextField, Button, Typography, Box, Card, CardContent, Alert } from '@mui/material';
import BusinessIcon from '@mui/icons-material/Business';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { mutate: signIn, isError, error, isPending } = useSignIn();
  const { login } = useAuth();
  const [showSuccessAlert, setShowSuccessAlert] = useState(false);

  useEffect(() => {
    if (searchParams.get('registered') === 'true') {
      setShowSuccessAlert(true);
      // Optionally remove the query param after showing the alert
      // navigate(location.pathname, { replace: true });
    }
  }, [searchParams, navigate]);

  const handleLogin = () => {
    signIn({ usernameOrEmail: email, password }, {
      onSuccess: (data) => {
        login(data.token.accessToken, data.user);
        navigate('/dashboard');
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
        background: 'linear-gradient(to right, #673ab7, #E91E63)',
      }}
    >
      <Container maxWidth="xs">
        <Card sx={{
          p: 2,
          borderRadius: 2,
        }}>
          <CardContent sx={{ p: 4 }}>
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', mb: 2 }}>
              <BusinessIcon sx={{ fontSize: 40, color: 'primary.main' }} />
              <Typography component="h1" variant="h5" sx={{ ml: 1, fontWeight: 'bold' }}>
                PropManager
              </Typography>
            </Box>
            <Typography component="h2" variant="body1" sx={{ textAlign: 'center', mb: 3, color: 'text.secondary' }}>
              Sign in to your account
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
                disabled={isPending}
              >
                {isPending ? 'Signing In...' : 'Sign In'}
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

