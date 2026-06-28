import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCarrito } from '../context/CarritoContext';
import { useTema } from '../context/TemaContext';

export function Navbar() {
  const { usuario, logout, esAdmin } = useAuth();
  const { cantidadTotal } = useCarrito();
  const { esOscuro, alternarTema } = useTema();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/');
  }

  return (
    <header className="sticky top-0 z-10 border-b border-muted/15 bg-surface/95 dark:bg-surface-dark/95 backdrop-blur-sm">
      <nav className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-3">
        <Link to="/" className="font-display text-lg font-bold tracking-tight text-[#1A2332] dark:text-[#F5F6F7]">
          Tienda<span className="text-accent">Electronica</span>
        </Link>

        <div className="flex items-center gap-3">
          {esAdmin && (
            <Link
              to="/admin"
              className="rounded-md px-3 py-1.5 text-sm font-medium text-muted dark:text-muted-dark hover:text-accent transition-colors"
            >
              Panel admin
            </Link>
          )}

          <button
            onClick={alternarTema}
            aria-label={esOscuro ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro'}
            className="rounded-md p-2 text-muted dark:text-muted-dark hover:text-accent transition-colors"
          >
            {esOscuro ? '☀' : '☾'}
          </button>

          <Link
            to="/carrito"
            className="relative rounded-md p-2 text-muted dark:text-muted-dark hover:text-accent transition-colors"
            aria-label={`Carrito, ${cantidadTotal} productos`}
          >
            🛒
            {cantidadTotal > 0 && (
              <span className="absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-accent font-mono text-[11px] text-white">
                {cantidadTotal}
              </span>
            )}
          </Link>

          {usuario ? (
            <div className="flex items-center gap-2">
              <Link
                to="/mis-ordenes"
                className="text-sm text-muted dark:text-muted-dark hover:text-accent transition-colors"
              >
                Mis pedidos
              </Link>
              <button
                onClick={handleLogout}
                className="rounded-md border border-muted/20 px-3 py-1.5 text-sm font-medium hover:border-accent hover:text-accent transition-colors"
              >
                Salir
              </button>
            </div>
          ) : (
            <Link
              to="/login"
              className="rounded-md bg-accent px-3 py-1.5 text-sm font-medium text-white hover:bg-accent-hover transition-colors"
            >
              Ingresar
            </Link>
          )}
        </div>
      </nav>
    </header>
  );
}
