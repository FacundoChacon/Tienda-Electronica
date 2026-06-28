import { api } from './api';

export const authService = {
  login: (email, password) => api.post('/auth/login', { email, password }),

  registro: (nombre, email, password) =>
    api.post('/usuarios/registro', { nombre, email, password }),
};
