import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { Page } from './userService';

export interface Role {
  id: number;
  name: string;
  permissions: string[];
}

export const fetchRoles = async (pageable: { page: number; size: number }): Promise<Page<Role>> => {
  const { page, size } = pageable;
  return fetchWithAuth(`/api/roles?page=${page}&size=${size}`);
};

export const useRoles = (page: number, size: number) => {
  return useQuery<Page<Role>, Error>({
    queryKey: ['roles', page, size],
    queryFn: () => fetchRoles({ page, size }),
  });
};

export const fetchRoleById = async (id: number): Promise<Role> => {
  return fetchWithAuth(`/api/roles/${id}`);
};

export const useRoleById = (id: number) => {
  return useQuery<Role, Error>({ queryKey: ['role', id], queryFn: () => fetchRoleById(id) });
};

export const createRole = async (newRole: Omit<Role, 'id'>): Promise<Role> => {
  return fetchWithAuth('/api/roles', {
    method: 'POST',
    body: JSON.stringify(newRole),
  });
};

export const useCreateRole = () => {
  const queryClient = useQueryClient();
  return useMutation<Role, Error, Omit<Role, 'id'>>({
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
  return useMutation<Role, Error, Role>({
    mutationFn: updateRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] });
      queryClient.invalidateQueries({ queryKey: ['role', 1] }); // Hack to invalidate single role
    },
  });
};

export const deleteRole = async (roleId: number): Promise<void> => {
  return fetchWithAuth(`/api/roles/${roleId}`, {
    method: 'DELETE',
  });
};

export const useDeleteRole = () => {
  const queryClient = useQueryClient();
  return useMutation<void, Error, number>({
    mutationFn: deleteRole,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] });
    },
  });
};