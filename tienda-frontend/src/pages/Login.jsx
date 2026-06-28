import { useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [enviando, setEnviando] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Si llegamos aca redirigidos desde una ruta protegida, volvemos a esa
  // ruta despues del login en vez de mandar siempre al catalogo.
  const destino = location.state?.from || '/';

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setEnviando(true);
    try {
      await login(email, password);
      navigate(destino, { replace: true });
    } catch (err) {
      setError(err.message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="mx-auto max-w-sm px-4 py-16">
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Ingresar</h1>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <div>
          <label htmlFor="email" className="mb-1 block text-sm font-medium">Email</label>
          <input
            id="email"
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
          />
        </div>

        <div>
          <label htmlFor="password" className="mb-1 block text-sm font-medium">Contraseña</label>
          <input
            id="password"
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
          />
        </div>

        {error && <p className="text-sm text-danger">{error}</p>}

        <button
          type="submit"
          disabled={enviando}
          className="rounded-md bg-accent px-4 py-2 font-medium text-white transition-colors hover:bg-accent-hover disabled:opacity-60"
        >
          {enviando ? 'Ingresando...' : 'Ingresar'}
        </button>
      </form>

      <p className="mt-4 text-sm text-muted dark:text-muted-dark">
        ¿No tenes cuenta? <Link to="/registro" className="text-accent hover:underline">Registrate</Link>
      </p>
    </div>
  );
}
