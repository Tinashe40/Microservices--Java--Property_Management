import { QueryClient } from '@tanstack/react-query';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || '';

export const queryClient = new QueryClient();

const getAuthToken = () => {
    return sessionStorage.getItem('token');
};

export class ApiError extends Error {
    constructor(public status: number, message: string) {
        super(message);
        this.name = 'ApiError';
    }
}

export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    const token = getAuthToken();
    const headers: Record<string, string> = {
        'Content-Type': 'application/json',
        ...options.headers as Record<string, string>,
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        if (response.status === 401) {
            sessionStorage.removeItem('token');
            window.location.href = '/login';
        }
        
        const errorData = await response.json().catch(() => null);
        const errorMessage = errorData?.message || response.statusText;
        throw new ApiError(response.status, errorMessage);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
};
