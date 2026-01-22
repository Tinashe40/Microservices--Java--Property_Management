import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { SignUpRequest, UserDto } from './authService';

export interface Page<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface ChangePasswordRequest {
  oldPassword?: string;
  newPassword?: string;
  confirmPassword?: string;
}

export interface ResetPasswordRequest {
  newPassword?: string;
  confirmPassword?: string;
}

export const fetchUsers = async (pageable: { page: number; size: number }): Promise<Page<UserDto>> => {
  const { page, size } = pageable;
  return fetchWithAuth(`/api/users?page=${page}&size=${size}`);
};

export const useUsers = (page: number, size: number) => {
  return useQuery<Page<UserDto>, Error>({
    queryKey: ['users', page, size],
    queryFn: () => fetchUsers({ page, size }),
  });
};

export const fetchUserById = async (id: number): Promise<UserDto> => {
  return fetchWithAuth(`/api/users/${id}`);
};

export const useUserById = (id: number) => {
  return useQuery<UserDto, Error>({ queryKey: ['user', id], queryFn: () => fetchUserById(id) });
};

export const fetchUserByUsername = async (username: string): Promise<UserDto> => {
  return fetchWithAuth(`/api/users/by-username?username=${username}`);
};

export const useUserByUsername = (username: string) => {
  return useQuery<UserDto, Error>({
    queryKey: ['user', username],
    queryFn: () => fetchUserByUsername(username),
  });
};

export const fetchUsersByIds = async (ids: number[]): Promise<UserDto[]> => {
  return fetchWithAuth(`/api/users/by-ids`, {
    method: 'POST',
    body: JSON.stringify(ids),
  });
};

export const useUsersByIds = (ids: number[]) => {
  return useQuery<UserDto[], Error>({ queryKey: ['users', ids], queryFn: () => fetchUsersByIds(ids) });
};

export const createUser = async (newUser: SignUpRequest): Promise<UserDto> => {
  return fetchWithAuth(`/api/users`, {
    method: 'POST',
    body: JSON.stringify(newUser),
  });
};

export const useCreateUser = () => {
  const queryClient = useQueryClient();
  return useMutation<UserDto, Error, SignUpRequest>({
    mutationFn: createUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const updateUser = async (updatedUser: UserDto): Promise<UserDto> => {
  return fetchWithAuth(`/api/users/${updatedUser.id}`, {
    method: 'PUT',
    body: JSON.stringify(updatedUser),
  });
};

export const useUpdateUser = () => {
  const queryClient = useQueryClient();
  return useMutation<UserDto, Error, UserDto>({
    mutationFn: updateUser,
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
      queryClient.invalidateQueries({ queryKey: ['user', variables.id] });
      queryClient.invalidateQueries({ queryKey: ['currentUser'] });
    },
  });
};

export const deleteUser = async (userId: number): Promise<void> => {
  return fetchWithAuth(`/api/users/${userId}`, {
    method: 'DELETE',
  });
};

export const useDeleteUser = () => {
  const queryClient = useQueryClient();
  return useMutation<void, Error, number>({
    mutationFn: deleteUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const assignRolesToUser = async ({ userId, roles }: { userId: number; roles: string[] }): Promise<UserDto> => {
  return fetchWithAuth(`/api/users/${userId}/roles`, {
    method: 'POST',
    body: JSON.stringify(roles),
  });
};

export const useAssignRolesToUser = () => {
  const queryClient = useQueryClient();
  return useMutation<UserDto, Error, { userId: number; roles: string[] }>({
    mutationFn: assignRolesToUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const deactivateUser = async (userId: number): Promise<void> => {
  return fetchWithAuth(`/api/users/${userId}/deactivate`, {
    method: 'PUT',
  });
};

export const useDeactivateUser = () => {
  const queryClient = useQueryClient();
  return useMutation<void, Error, number>({
    mutationFn: deactivateUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const activateUser = async (userId: number): Promise<void> => {
  return fetchWithAuth(`/api/users/${userId}/activate`, {
    method: 'PUT',
  });
};

export const useActivateUser = () => {
  const queryClient = useQueryClient();
  return useMutation<void, Error, number>({
    mutationFn: activateUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const changePassword = async (request: ChangePasswordRequest): Promise<void> => {
  return fetchWithAuth(`/api/users/change-password`, {
    method: 'POST',
    body: JSON.stringify(request),
  });
};

export const useChangePassword = () => {
  return useMutation<void, Error, ChangePasswordRequest>({
    mutationFn: changePassword,
  });
};

export const resetPassword = async ({ userId, request }: { userId: number; request: ResetPasswordRequest }): Promise<void> => {
  return fetchWithAuth(`/api/users/${userId}/reset-password`, {
    method: 'POST',
    body: JSON.stringify(request),
  });
};

export const useResetPassword = () => {
  return useMutation<void, Error, { userId: number; request: ResetPasswordRequest }>({
    mutationFn: resetPassword,
  });
};

export const getUsersCount = async (): Promise<number> => {
  return fetchWithAuth(`/api/users/count`);
};

export const useUsersCount = () => {
  return useQuery<number, Error>({
    queryKey: ['usersCount'],
    queryFn: getUsersCount,
  });
};