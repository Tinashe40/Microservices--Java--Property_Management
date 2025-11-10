import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export interface User {
  id: string;
  username: string;
  email: string;
  // Add other user fields as needed
}

export const fetchUsers = async (): Promise<User[]> => {
  const response = await fetch(`${API_BASE_URL}/api/users`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useUsers = () => {
  return useQuery<User[], Error>({ queryKey: ['users'], queryFn: fetchUsers });
};

export const fetchUserById = async (id: string): Promise<User> => {
  const response = await fetch(`${API_BASE_URL}/api/users/${id}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useUserById = (id: string) => {
  return useQuery<User, Error>({ queryKey: ['user', id], queryFn: () => fetchUserById(id) });
};

export const fetchUserByUsername = async (username: string): Promise<User> => {
  const response = await fetch(`${API_BASE_URL}/api/users/by-username?username=${username}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useUserByUsername = (username: string) => {
  return useQuery<User, Error>({ queryKey: ['user', username], queryFn: () => fetchUserByUsername(username) });
};

export const fetchUsersByIds = async (ids: string[]): Promise<User[]> => {
  const response = await fetch(`${API_BASE_URL}/api/users/by-ids`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  });
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useUsersByIds = (ids: string[]) => {
  return useQuery<User[], Error>({ queryKey: ['users', ids], queryFn: () => fetchUsersByIds(ids) });
};

export const createUser = async (newUser: Omit<User, 'id'>): Promise<User> => {
  const response = await fetch(`${API_BASE_URL}/api/users`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(newUser),
  });
  if (!response.ok) {
    throw new Error('Failed to create user');
  }
  return response.json();
};

export const useCreateUser = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const updateUser = async (updatedUser: User): Promise<User> => {
  const response = await fetch(`${API_BASE_URL}/api/users/${updatedUser.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(updatedUser),
  });
  if (!response.ok) {
    throw new Error('Failed to update user');
  }
  return response.json();
};

export const useUpdateUser = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updateUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const deleteUser = async (userId: string): Promise<void> => {
  const response = await fetch(`${API_BASE_URL}/api/users/${userId}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Failed to delete user');
  }
};

export const useDeleteUser = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const assignRolesToUser = async ({ userId, roles }: { userId: string, roles: string[] }): Promise<User> => {
  const response = await fetch(`${API_BASE_URL}/api/users/${userId}/roles`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(roles),
  });
  if (!response.ok) {
    throw new Error('Failed to assign roles');
  }
  return response.json();
};

export const useAssignRolesToUser = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: assignRolesToUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};
