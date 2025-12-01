import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { Page } from './userService';

export enum OccupancyStatus {
    AVAILABLE = 'AVAILABLE',
    RESERVED = 'RESERVED',
    VACANT = 'VACANT',
    OCCUPIED = 'OCCUPIED',
    UNDER_MAINTENANCE = 'UNDER_MAINTENANCE',
}

export interface Unit {
    id: number;
    name: string;
    floorId: number;
    propertyId: number;
    occupancyStatus: OccupancyStatus;
    tenant: string;
    monthlyRent: number;
}

export const useAllSystemUnits = (page: number, size: number) => {
    return useQuery<Page<Unit>, Error>({
        queryKey: ['units', 'all', page, size],
        queryFn: () => fetchWithAuth(`/api/units?page=${page}&size=${size}`),
    });
};

export const useUnitsByProperty = (propertyId: number, page: number, size: number) => {
    return useQuery<Page<Unit>, Error>({
        queryKey: ['units', propertyId, page, size],
        queryFn: () => fetchWithAuth(`/api/units?propertyId=${propertyId}&page=${page}&size=${size}`),
        enabled: !!propertyId,
    });
};

export const useUnitsByFloor = (floorId: number, page: number, size: number) => {
    return useQuery<Page<Unit>, Error>({
        queryKey: ['units', floorId, page, size],
        queryFn: () => fetchWithAuth(`/api/units?floorId=${floorId}&page=${page}&size=${size}`),
        enabled: !!floorId,
    });
};

export const useUnitById = (unitId: number) => {
    return useQuery<Unit, Error>({
        queryKey: ['unit', unitId],
        queryFn: () => fetchWithAuth(`/api/units/${unitId}`),
        enabled: !!unitId,
    });
};

export const useUnitByName = (name: string, propertyId: number) => {
    return useQuery<Unit, Error>({
        queryKey: ['unit', name, propertyId],
        queryFn: () => fetchWithAuth(`/api/units/name/${name}?propertyId=${propertyId}`),
        enabled: !!name && !!propertyId,
    });
};

export const useCreateUnit = () => {
    const queryClient = useQueryClient();
    return useMutation<Unit, Error, Omit<Unit, 'id'>>({
        mutationFn: (newUnit) =>
            fetchWithAuth('/api/units', {
                method: 'POST',
                body: JSON.stringify(newUnit),
            }),
        onSuccess: (data) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', data.floorId] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', data.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useUpdateUnit = () => {
    const queryClient = useQueryClient();
    return useMutation<Unit, Error, Unit>({
        mutationFn: (updatedUnit) =>
            fetchWithAuth(`/api/units/${updatedUnit.id}`, {
                method: 'PUT',
                body: JSON.stringify(updatedUnit),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['unit', variables.id] });
            queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', variables.floorId] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', variables.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useDeleteUnit = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Unit>({
        mutationFn: (unit) =>
            fetchWithAuth(`/api/units/${unit.id}`, {
                method: 'DELETE',
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', variables.floorId] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', variables.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useUpdateUnitOccupancy = () => {
    const queryClient = useQueryClient();
    return useMutation<Unit, Error, { unitId: number; occupancyStatus: OccupancyStatus; tenant?: string }>({
        mutationFn: ({ unitId, occupancyStatus, tenant }) =>
            fetchWithAuth(`/api/units/${unitId}/occupancy?occupancyStatus=${occupancyStatus}&tenant=${tenant || ''}`, {
                method: 'PATCH',
            }),
        onSuccess: (data) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', data.floorId] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', data.propertyId] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
        },
    });
};

export const useSearchUnits = (query: string, page: number, size: number) => {
    return useQuery<Page<Unit>, Error>({
        queryKey: ['units', 'search', query, page, size],
        queryFn: () => fetchWithAuth(`/api/units/search?query=${query}&page=${page}&size=${size}`),
        enabled: !!query,
    });
};

export const useCalculateRentalIncome = (propertyId: number) => {
    return useQuery<number, Error>({
        queryKey: ['rentalIncome', propertyId],
        queryFn: () => fetchWithAuth(`/api/units/property/${propertyId}/income`),
        enabled: !!propertyId,
    });
};

export const useCountUnitsByProperty = (propertyId: number) => {
    return useQuery<number, Error>({
        queryKey: ['unitCount', propertyId],
        queryFn: () => fetchWithAuth(`/api/units/property/${propertyId}/count`),
        enabled: !!propertyId,
    });
};

export const useCreateUnitsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Unit[]>({
        mutationFn: (units) =>
            fetchWithAuth('/api/units/bulk-create', {
                method: 'POST',
                body: JSON.stringify(units),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            variables.forEach(unit => {
                queryClient.invalidateQueries({ queryKey: ['propertyStats', unit.propertyId] });
                queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', unit.floorId] });
            });
        },
    });
};

export const useUpdateUnitsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, Unit[]>({
        mutationFn: (units) =>
            fetchWithAuth('/api/units/bulk-update', {
                method: 'PUT',
                body: JSON.stringify(units),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            variables.forEach(unit => {
                queryClient.invalidateQueries({ queryKey: ['propertyStats', unit.propertyId] });
                queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats', unit.floorId] });
            });
        },
    });
};

export const useDeleteUnitsInBulk = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, number[]>({
        mutationFn: (ids) =>
            fetchWithAuth('/api/units/bulk-delete', {
                method: 'DELETE',
                body: JSON.stringify(ids),
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['units'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats'] });
            queryClient.invalidateQueries({ queryKey: ['floorOccupancyStats'] });
        },
    });
};