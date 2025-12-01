import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { Page } from './userService';

export interface Permission {
  id: number;
  name: string;
}

export const fetchPermissions = async (pageable: { page: number; size: number }): Promise<Page<Permission>> => {
  const { page, size } = pageable;
  return fetchWithAuth(`/api/permissions?page=${page}&size=${size}`);
};

export const usePermissions = (page: number, size: number) => {
  return useQuery<Page<Permission>, Error>({
    queryKey: ['permissions', page, size],
    queryFn: () => fetchPermissions({ page, size }),
  });
};

export const createPermission = async (newPermission: Omit<Permission, 'id'>): Promise<Permission> => {
  return fetchWithAuth('/api/permissions', {
    method: 'POST',
    body: JSON.stringify(newPermission),
  });
};

export const useCreatePermission = () => {
  const queryClient = useQueryClient();
  return useMutation<Permission, Error, Omit<Permission, 'id'>>({
    mutationFn: createPermission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['permissions'] });
    },
  });
};