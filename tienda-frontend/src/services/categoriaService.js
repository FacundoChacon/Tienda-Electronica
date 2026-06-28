import { api } from './api';

export const categoriaService = {
  listar: () => api.get('/categorias'),
  buscarPorId: (id) => api.get(`/categorias/${id}`),

  crear: (categoria) => api.post('/categorias', categoria, true),
  actualizar: (id, categoria) => api.put(`/categorias/${id}`, categoria, true),
  eliminar: (id) => api.delete(`/categorias/${id}`, true),
};
