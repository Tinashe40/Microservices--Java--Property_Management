
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';
import { Page } from './userService';

export enum PropertyType {
    RESIDENTIAL = 'RESIDENTIAL',
    COMMERCIAL = 'COMMERCIAL',
    INDUSTRIAL = 'INDUSTRIAL',
    LAND = 'LAND',
    SPECIAL_PURPOSE = 'SPECIAL_PURPOSE',
}

export interface Property {
    id: number;
    name: string;
    address: string;
    type: PropertyType;
    managerId: number;
    propertyType: string;
    managedByDetails: {
        username: string;
    };
}

export interface PropertyStats {
    totalFloors: number;
    totalUnits: number;
    occupiedUnits: number;
    vacantUnits: number;
    reservedUnits: number;
    notAvailableUnits: number;
    underMaintenanceUnits: number;
    occupancyRate: number;
    vacancyRate: number;
    totalRentalIncome: number;
    potentialRentalIncome: number;
}

export interface SystemStats {
    totalProperties: number;
    totalFloors: number;
    totalUnits: number;
    occupancyRate: number;
    overallOccupancyRate: number;
    totalActualIncome: number;
    totalPotentialIncome: number;
}

export interface PropertyFilterDTO {
    name?: string;
    address?: string;
    propertyType?: PropertyType;
    minFloors?: number;
    maxFloors?: number;
    minUnits?: number;
    maxUnits?: number;
}

export const useSystemWideStats = () => {
    return useQuery<SystemStats, Error>({
        queryKey: ['systemStats'],
        queryFn: () => fetchWithAuth('/api/properties/stats/system-wide'),
    });
};

export const useSystemWideStatsByType = (propertyType: PropertyType) => {
    return useQuery<SystemStats, Error>({
        queryKey: ['systemStats', propertyType],
        queryFn: () => fetchWithAuth(`/api/properties/stats/system-wide-by-type?propertyType=${propertyType}`),
        enabled: !!propertyType,
    });
};

export const useProperties = (page: number, size: number) => {
    return useQuery<Page<Property>, Error>({
        queryKey: ['properties', page, size],
        queryFn: () => fetchWithAuth(`/api/properties?page=${page}&size=${size}`),
    });
};

export const usePropertiesCount = () => {
    return useQuery<number, Error>({
        queryKey: ['propertiesCount'],
        queryFn: () => fetchWithAuth('/api/properties/count'),
    });
};

export const usePropertiesCountByType = (propertyType: PropertyType) => {
    return useQuery<number, Error>({
        queryKey: ['propertiesCount', propertyType],
        queryFn: () => fetchWithAuth(`/api/properties/count-by-type?propertyType=${propertyType}`),
        enabled: !!propertyType,
    });
};

export const useSearchProperties = (filter: PropertyFilterDTO, page: number, size: number) => {
    return useQuery<Page<Property>, Error>({
        queryKey: ['properties', 'search', filter, page, size],
        queryFn: () => fetchWithAuth(`/api/properties/search?page=${page}&size=${size}`, {
            method: 'POST',
            body: JSON.stringify(filter),
        }),
        enabled: !!filter,
    });
};

export const usePropertiesByManager = (managerId: number, page: number, size: number) => {
    return useQuery<Page<Property>, Error>({
        queryKey: ['properties', managerId, page, size],
        queryFn: () => fetchWithAuth(`/api/properties/by-manager/${managerId}?page=${page}&size=${size}`),
        enabled: !!managerId,
    });
};

export const usePropertyById = (propertyId: number) => {
    return useQuery<Property, Error>({
        queryKey: ['property', propertyId],
        queryFn: () => fetchWithAuth(`/api/properties/${propertyId}`),
        enabled: !!propertyId,
    });
};

export const usePropertyStats = (propertyId: number) => {
    return useQuery<PropertyStats, Error>({
        queryKey: ['propertyStats', propertyId],
        queryFn: () => fetchWithAuth(`/api/properties/${propertyId}/stats`),
        enabled: !!propertyId,
    });
};

export const usePropertyStatsByManager = (managerId: number) => {
    return useQuery<PropertyStats, Error>({
        queryKey: ['propertyStats', 'manager', managerId],
        queryFn: () => fetchWithAuth(`/api/properties/by-manager/${managerId}/stats`),
        enabled: !!managerId,
    });
};

export const useCreateProperty = () => {
    const queryClient = useQueryClient();
    return useMutation<Property, Error, Omit<Property, 'id' | 'managerId'>>({
        mutationFn: (newProperty) =>
            fetchWithAuth('/api/properties', {
                method: 'POST',
                body: JSON.stringify(newProperty),
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['properties'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            queryClient.invalidateQueries({ queryKey: ['propertiesCount'] });
        },
    });
};

export const useUpdateProperty = () => {
    const queryClient = useQueryClient();
    return useMutation<Property, Error, Property>({
        mutationFn: (updatedProperty) =>
            fetchWithAuth(`/api/properties/${updatedProperty.id}`, {
                method: 'PUT',
                body: JSON.stringify(updatedProperty),
            }),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ['properties'] });
            queryClient.invalidateQueries({ queryKey: ['property', variables.id] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            queryClient.invalidateQueries({ queryKey: ['propertyStats', variables.id] });
        },
    });
};

export const useDeleteProperty = () => {
    const queryClient = useQueryClient();
    return useMutation<void, Error, number>({
        mutationFn: (propertyId) =>
            fetchWithAuth(`/api/properties/${propertyId}`, {
                method: 'DELETE',
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['properties'] });
            queryClient.invalidateQueries({ queryKey: ['systemStats'] });
            queryClient.invalidateQueries({ queryKey: ['propertiesCount'] });
        },
    });
};

