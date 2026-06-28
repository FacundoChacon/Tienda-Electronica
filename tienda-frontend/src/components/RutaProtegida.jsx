import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Envuelve una ruta para exigir login (y opcionalmente rol ADMIN).
 * Esto es solo una proteccion de UX (ocultar botones, redirigir);
 * la seguridad real sigue viviendo en el backend, que rechaza
 * cualquier request sin el token o rol correcto sin importar
 * lo que haga el frontend.
 */
export function RutaProtegida({ children, soloAdmin = false }) {
  const { usuario, esAdmin } = useAuth();

  if (!usuario) return <Navigate to="/login" replace />;
  if (soloAdmin && !esAdmin) return <Navigate to="/" replace />;

  return children;
}
