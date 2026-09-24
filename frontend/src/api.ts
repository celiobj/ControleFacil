import axios from "axios";

export type AppEnvironment = "hml" | "prd";
export const environmentConfig: Record<
  AppEnvironment,
  { label: string; apiUrl: string }
> = {
  hml: { label: "Homologação", apiUrl: "http://localhost:3001" },
  prd: { label: "Produção", apiUrl: "http://localhost:3000" },
};

export const getEnvironment = (): AppEnvironment =>
  (localStorage.getItem("environment") as AppEnvironment) || "prd";
export const setEnvironment = (environment: AppEnvironment) =>
  localStorage.setItem("environment", environment);
export const getTokenKey = (environment = getEnvironment()) =>
  `token_${environment}`;

export const api = axios.create({
  baseURL: environmentConfig[getEnvironment()].apiUrl,
});
api.interceptors.request.use((config) => {
  const environment = getEnvironment();
  config.baseURL = environmentConfig[environment].apiUrl;
  const token = localStorage.getItem(getTokenKey(environment));
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
api.interceptors.response.use(undefined, (error) => {
  if (error.response?.status === 401) {
    localStorage.removeItem(getTokenKey());
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
