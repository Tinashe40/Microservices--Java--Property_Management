import { QueryClient } from '@tanstack/react-query';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export const queryClient = new QueryClient();

const getAuthToken = () => {
    return sessionStorage.getItem('token');
};

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
            // Unauthorized, redirect to login
            window.location.href = '/login';
        }
        throw new Error('Request failed');
    }

    return response.json();
};
