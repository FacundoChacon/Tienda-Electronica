import { api } from './api';

export const ordenService = {
  // Checkout: el usuario sale del token en el backend, aca solo mandamos el carrito.
  crear: (items) => api.post('/ordenes', { items }, true),

  misOrdenes: () => api.get('/ordenes/mis-ordenes', true),

  buscarPorId: (id) => api.get(`/ordenes/${id}`, true),

  // Solo ADMIN (validado por el backend)
  listarTodas: () => api.get('/ordenes', true),
  cambiarEstado: (id, estado) => api.patch(`/ordenes/${id}/estado`, { estado }, true),
};
