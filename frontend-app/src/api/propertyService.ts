import { useQuery } from '@tanstack/react-query';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export interface Property {
  id: string;
  name: string;
  address: string;
  // Add other property fields as needed
}

export const fetchProperties = async (): Promise<Property[]> => {
  const response = await fetch(`${API_BASE_URL}/api/properties`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useProperties = () => {
  return useQuery<Property[], Error>({ queryKey: ['properties'], queryFn: fetchProperties });
};
