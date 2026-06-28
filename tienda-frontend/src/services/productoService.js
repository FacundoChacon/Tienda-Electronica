import { api } from './api';

export const productoService = {
  /**
   * Lista el catalogo de forma paginada, con filtros opcionales, igual que
   * el endpoint del backend:
   * GET /api/productos?categoriaId=X&nombre=Y&precioMin=A&precioMax=B&page=0&size=12
   *
   * Devuelve un PageResponse: { contenido, paginaActual, totalPaginas, totalElementos, ... }
   */
  listar: (filtros = {}) => {
    const params = new URLSearchParams();
    if (filtros.categoriaId) params.set('categoriaId', filtros.categoriaId);
    if (filtros.nombre) params.set('nombre', filtros.nombre);
    if (filtros.precioMin) params.set('precioMin', filtros.precioMin);
    if (filtros.precioMax) params.set('precioMax', filtros.precioMax);
    // page es 0-indexed, igual que en el backend (Spring Data).
    params.set('page', filtros.page ?? 0);
    params.set('size', filtros.size ?? 12);

    return api.get(`/productos?${params.toString()}`);
  },

  buscarPorId: (id) => api.get(`/productos/${id}`),

  // Metricas para el dashboard de admin (requiere rol ADMIN en el backend).
  obtenerMetricas: () => api.get('/productos/metricas', true),

  // Operaciones de administracion: requieren rol ADMIN (el backend lo valida igual,
  // esto solo evita llamadas innecesarias si alguien sin permiso llega al formulario).
  crear: (producto) => api.post('/productos', producto, true),
  actualizar: (id, producto) => api.put(`/productos/${id}`, producto, true),
  eliminar: (id) => api.delete(`/productos/${id}`, true),
};

