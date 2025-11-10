import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';

export interface Permission {
  id: string;
  name: string;
}

export const fetchPermissions = async (): Promise<Permission[]> => {
  return fetchWithAuth('/api/permissions');
};

export const usePermissions = () => {
  return useQuery<Permission[], Error>({ queryKey: ['permissions'], queryFn: fetchPermissions });
};

export const createPermission = async (newPermission: Omit<Permission, 'id'>): Promise<Permission> => {
  return fetchWithAuth('/api/permissions', {
    method: 'POST',
    body: JSON.stringify(newPermission),
  });
};

export const useCreatePermission = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createPermission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['permissions'] });
    },
  });
};

export const updatePermission = async (updatedPermission: Permission): Promise<Permission> => {
  return fetchWithAuth(`/api/permissions/${updatedPermission.id}`, {
    method: 'PUT',
    body: JSON.stringify(updatedPermission),
  });
};

export const useUpdatePermission = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updatePermission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['permissions'] });
    },
  });
};

export const deletePermission = async (permissionId: string): Promise<void> => {
  return fetchWithAuth(`/api/permissions/${permissionId}`, {
    method: 'DELETE',
  });
};

export const useDeletePermission = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deletePermission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['permissions'] });
    },
  });
};
