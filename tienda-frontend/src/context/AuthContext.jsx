import { createContext, useContext, useState, useCallback } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

/**
 * Maneja la sesion del usuario en toda la app: token JWT, email y rol.
 * Persistimos en localStorage para que la sesion sobreviva un refresh
 * de pagina (sin esto, el usuario tendria que loguearse cada vez que recarga).
 */
export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(() => {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');
    const rol = localStorage.getItem('rol');
    return token ? { token, email, rol } : null;
  });

  const login = useCallback(async (email, password) => {
    const data = await authService.login(email, password);
    localStorage.setItem('token', data.token);
    localStorage.setItem('email', data.email);
    localStorage.setItem('rol', data.rol);
    setUsuario({ token: data.token, email: data.email, rol: data.rol });
    return data;
  }, []);

  const registrar = useCallback(async (nombre, email, password) => {
    await authService.registro(nombre, email, password);
    // Despues de registrarse, lo logueamos directamente para no pedirle
    // que escriba las credenciales dos veces.
    return login(email, password);
  }, [login]);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('rol');
    setUsuario(null);
  }, []);

  const esAdmin = usuario?.rol === 'ADMIN';

  return (
    <AuthContext.Provider value={{ usuario, login, registrar, logout, esAdmin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth debe usarse dentro de un AuthProvider');
  return context;
}
