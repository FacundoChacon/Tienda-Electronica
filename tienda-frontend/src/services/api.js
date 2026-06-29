const BASE_URL = 'https://tienda-electronica-wxfk.onrender.com/api';

/**
 * Cliente HTTP centralizado. Todo el resto de los services (productos,
 * ordenes, auth) pasan por aqui, asi el manejo del token y de errores
 * queda en un solo lugar en vez de repetirse en cada llamada a fetch.
 */
async function request(path, { method = 'GET', body, auth = false } = {}) {
  const headers = { 'Content-Type': 'application/json' };

  if (auth) {
    const token = localStorage.getItem('token');
    if (token) headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  // El backend devuelve 204 (No Content) en deletes, sin body que parsear.
  if (response.status === 204) return null;

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    // El GlobalExceptionHandler del backend siempre manda "mensaje" o "errores".
    const mensaje = data?.mensaje || formatearErroresDeValidacion(data?.errores) || 'Ocurrio un error inesperado';
    throw new ApiError(mensaje, response.status);
  }

  return data;
}

function formatearErroresDeValidacion(errores) {
  if (!errores) return null;
  return Object.values(errores).join(' | ');
}

export class ApiError extends Error {
  constructor(mensaje, status) {
    super(mensaje);
    this.status = status;
  }
}

export const api = {
  get: (path, auth = false) => request(path, { method: 'GET', auth }),
  post: (path, body, auth = false) => request(path, { method: 'POST', body, auth }),
  put: (path, body, auth = false) => request(path, { method: 'PUT', body, auth }),
  patch: (path, body, auth = false) => request(path, { method: 'PATCH', body, auth }),
  delete: (path, auth = false) => request(path, { method: 'DELETE', auth }),
};
