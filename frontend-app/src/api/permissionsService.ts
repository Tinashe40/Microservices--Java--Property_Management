import { useQuery } from '@tanstack/react-query';
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