import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { UserDto, fetchCurrentUser } from '../api/authService';

interface AuthContextType {
  user: UserDto | null;
  token: string | null;
  login: (token: string, user: UserDto) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const queryClient = useQueryClient();
  const [user, setUser] = useState<UserDto | null>(() => {
    const storedUser = sessionStorage.getItem('user');
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [token, setToken] = useState<string | null>(() => sessionStorage.getItem('token'));

  const { data: currentUser, isLoading, error } = useQuery<UserDto, Error>({
    queryKey: ['currentUser'],
    queryFn: fetchCurrentUser,
    enabled: !!token && !user,
    retry: false,
  });

  useEffect(() => {
    if (currentUser) {
      setUser(currentUser);
      sessionStorage.setItem('user', JSON.stringify(currentUser));
    }
  }, [currentUser]);

  useEffect(() => {
    if (error) {
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('user');
      setToken(null);
      setUser(null);
    }
  }, [error]);

  const login = (newToken: string, user: UserDto) => {
    sessionStorage.setItem('token', newToken);
    sessionStorage.setItem('user', JSON.stringify(user));
    setToken(newToken);
    setUser(user);
    queryClient.setQueryData(['currentUser'], user);
  };

  const logout = () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    setToken(null);
    setUser(null);
    queryClient.removeQueries({ queryKey: ['currentUser'] });
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
