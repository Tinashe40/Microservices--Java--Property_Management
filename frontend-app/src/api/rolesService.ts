import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';

export interface Role {
  id: string;
  name: string;
}

export const fetchRoles = async (): Promise<Role[]> => {
  return fetchWithAuth('/api/roles');
};

export const useRoles = () => {
  return useQuery<Role[], Error>({ queryKey: ['roles'], queryFn: fetchRoles });
};

export const createRole = async (newRole: Omit<Role, 'id'>): Promise<Role> => {
  return fetchWithAuth('/api/roles', {
    method: 'POST',
    body: JSON.stringify(newRole),
  });
};

export const useCreateRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] });
    },
  });
};

export const updateRole = async (updatedRole: Role): Promise<Role> => {
  return fetchWithAuth(`/api/roles/${updatedRole.id}`, {
    method: 'PUT',
    body: JSON.stringify(updatedRole),
  });
};

export const useUpdateRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updateRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] });
    },
  });
};

export const deleteRole = async (roleId: string): Promise<void> => {
  return fetchWithAuth(`/api/roles/${roleId}`, {
    method: 'DELETE',
  });
};

export const useDeleteRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] });
    },
  });
};
