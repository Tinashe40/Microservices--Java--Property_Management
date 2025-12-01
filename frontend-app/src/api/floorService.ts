import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { Page } from './userService';

export interface Floor {
    id: number;
    name: string;
    propertyId: number;
}

export interface FloorOccupancyStats {
    totalUnits: number;
    occupiedUnits: number;
    vacantUnits: number;
    underMaintenanceUnits: number;
    occupancyRate: number;
}

export const useAllSystemFloors = (page: number, size: number) => {
    return useQuery<Page<Floor>, Error>({
        queryKey: ['floors', 'all', page, size],
        queryFn: () => fetchWithAuth(`/api/floors?page=${page}&size=${size}`),
    });
};

export const useAllFloorsByProperty = (propertyId: number) => {
    return useQuery<Floor[], Error>({
        queryKey: ['floors', propertyId, 'all'],
        queryFn: () => fetchWithAuth(`/api/floors?propertyId=${propertyId}&size=0`),
        enabled: !!propertyId,
    });
};

export const useFloorsByProperty = (propertyId: number, page: number, size: number) => {
    return useQuery<Page<Floor>, Error>({
        queryKey: ['floors', propertyId, page, size],
        queryFn: () => fetchWithAuth(`/api/floors?propertyId=${propertyId}&page=${page}&size=${size}`),
        enabled: !!propertyId,
    });
};

export const useFloorById = (floorId: number) => {
    return useQuery<Floor, Error>({
        queryKey: ['floor', floorId],
        queryFn: () => fetchWithAuth(`/api/floors/${floorId}`),
        enabled: !!floorId,
    });
};

export const useCreateFloor = () => {
    const queryClient = useQueryClient();
    return useMutation<Floor, Error, Omit<Floor, 'id'>>({
        mutationFn: (newFloor) =>
            fetchWithAuth('/api/floors', {
                method: 'POST',
                body: JSON.stringify(newFloor),
            }),
        onSuccess: (data) => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', data.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useUpdateFloor = () => {
    const queryClient = useQueryClient();
    return useMutation<Floor, Error, Floor>({
        mutationFn: (updatedFloor) =>
            fetchWithAuth(`/api/floors/${updatedFloor.id}`, {
                method: 'PUT',
                body: JSON.stringify(updatedFloor),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['floor', variables.id] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', variables.propertyId] });
        },
    });
};

export const useDeleteFloor = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Floor>({
        mutationFn: (floor) =>
            fetchWithAuth(`/api/floors/${floor.id}`, {
                method: 'DELETE',
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', variables.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useFloorOccupancyStats = (floorId: number) => {
    return useQuery<FloorOccupancyStats, Error>({
        queryKey: ['floorOccupancyStats', floorId],
        queryFn: () => fetchWithAuth(`/api/floors/${floorId}/occupancy-stats`),
        enabled: !!floorId,
    });
};

export const useRefreshFloorOccupancy = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, number>({
        mutationFn: (floorId) =>
            fetchWithAuth(`/api/floors/${floorId}/refresh-occupancy`, {
                method: 'POST',
            }),
        onSuccess: (_, floorId) => {
            queryClient.invalidateQueries({ queryKey: ['floors', floorId] });
            queryClient.invalidateQueries({ queryKey: ['units'] });
        },
    });
};

export const useCreateFloorsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Floor[]>({
        mutationFn: (floors) =>
            fetchWithAuth('/api/floors/bulk-create', {
                method: 'POST',
                body: JSON.stringify(floors),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            variables.forEach(floor => {
                queryClient.invalidateQueries({ queryKey: ['propertyStats', floor.propertyId] });
            });
        },
    });
};

export const useUpdateFloorsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Floor[]>({
        mutationFn: (floors) =>
            fetchWithAuth('/api/floors/bulk-update', {
                method: 'PUT',
                body: JSON.stringify(floors),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            variables.forEach(floor => {
                queryClient.invalidateQueries({ queryKey: ['propertyStats', floor.propertyId] });
            });
        },
    });
};

export const useDeleteFloorsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, number[]>({
        mutationFn: (ids) =>
            fetchWithAuth('/api/floors/bulk-delete', {
                method: 'DELETE',
                body: JSON.stringify(ids),
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['floors'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats'] });
        },
    });
};