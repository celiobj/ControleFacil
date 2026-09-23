import axios from "axios";
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || window.location.origin,
});
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
api.interceptors.response.use(undefined, (error) => {
  if (error.response?.status === 401) {
    localStorage.removeItem("token");
    if (window.location.pathname !== "/login") window.location.assign("/login");
  }
  return Promise.reject(error);
});
export type Property = {
  id: string;
  code: string;
  title: string;
  city: string;
  neighborhood: string;
  status: string;
  type: string;
  createdAt: string;
};
