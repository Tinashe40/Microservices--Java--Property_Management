import { useQuery, useMutation } from '@tanstack/react-query';
import { fetchWithAuth } from './apiClient';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export interface UserDTO {
    id: number;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    roles: string[];
    enabled: boolean;
    accountNonExpired: boolean;
    accountNonLocked: boolean;
    credentialsNonExpired: boolean;
}

export interface JwtAuthResponse {
    accessToken: string;
    tokenType: string;
}

export interface LoginResponse {
    token: JwtAuthResponse;
    user: UserDTO;
}

export interface SignUpRequest {
    username: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    roles: string[];
}

export interface LoginRequest {
    usernameOrEmail: string;
    password: string;
}

export const signIn = async (loginRequest: LoginRequest): Promise<LoginResponse> => {
    return fetchWithAuth('/api/auth/sign-in', {
        method: 'POST',
        body: JSON.stringify(loginRequest),
    });
};

export const useSignIn = () => {
    return useMutation({ mutationFn: signIn });
};

export const signUp = async (signUpRequest: SignUpRequest): Promise<UserDTO> => {
    return fetchWithAuth('/api/auth/signup', {
        method: 'POST',
        body: JSON.stringify(signUpRequest),
    });
};

export const useSignUp = () => {
    return useMutation({ mutationFn: signUp });
};

export const fetchCurrentUser = async (): Promise<UserDTO> => {
    return fetchWithAuth('/api/auth/me');
};

export const useCurrentUser = () => {
    return useQuery<UserDTO, Error>({
        queryKey: ['currentUser'],
        queryFn: fetchCurrentUser,
    });
};

export const validateToken = async (token: string): Promise<void> => {
    return fetchWithAuth(`/api/auth/validate-token?token=${token}`);
};

export const useValidateToken = (token: string) => {
    return useQuery({
        queryKey: ['validateToken', token],
        queryFn: () => validateToken(token),
        enabled: !!token,
    });
};
