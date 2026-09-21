import axios from 'axios';
export const api = axios.create({ baseURL: import.meta.env.VITE_API_URL || window.location.origin });
api.interceptors.request.use((config) => { const token = localStorage.getItem('token'); if (token) config.headers.Authorization = `Bearer ${token}`; return config; });
export type Property = { id: string; code: string; title: string; city: string; neighborhood: string; status: string; type: string; };
