import { Link, useNavigate } from 'react-router-dom';
import { useCarrito } from '../context/CarritoContext';
import { useAuth } from '../context/AuthContext';
import { ProductImage } from '../components/ProductImage';
import { formatearPrecio } from '../utils/formato';

export function Carrito() {
  const { items, actualizarCantidad, quitarItem, total } = useCarrito();
  const { usuario } = useAuth();
  const navigate = useNavigate();

  function handleCheckout() {
    if (!usuario) {
      // Lo mandamos a loguearse y, gracias al "state.from" en Login,
      // vuelve directo aca despues de ingresar.
      navigate('/login', { state: { from: '/carrito' } });
      return;
    }
    navigate('/checkout');
  }

  // Resta de a 1; si la cantidad llega a 0, se interpreta como "quitar del carrito"
  // (asi el boton "-" tiene un comportamiento predecible hasta el final).
  function handleRestar(item) {
    if (item.cantidad <= 1) {
      quitarItem(item.productoId);
    } else {
      actualizarCantidad(item.productoId, item.cantidad - 1);
    }
  }

  // Suma de a 1, respetando el stock disponible (actualizarCantidad ya capea
  // internamente contra item.stockDisponible, ver CarritoContext).
  function handleSumar(item) {
    actualizarCantidad(item.productoId, item.cantidad + 1);
  }

  if (items.length === 0) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-16 text-center">
        <h1 className="mb-2 font-display text-xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Tu carrito esta vacio</h1>
        <p className="mb-6 text-muted dark:text-muted-dark">Agrega productos del catalogo para empezar.</p>
        <Link to="/" className="rounded-md bg-accent px-4 py-2 font-medium text-white hover:bg-accent-hover">
          Ver catalogo
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-8">
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Tu carrito</h1>

      <div className="flex flex-col gap-3">
        {items.map((item) => {
          const enElMaximo = item.cantidad >= item.stockDisponible;

          return (
            <div
              key={item.productoId}
              className="flex items-center gap-4 rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark p-4"
            >
              <ProductImage producto={item} className="h-16 w-16 rounded-md object-cover" tamanioIcono={28} />

              <div className="flex-1">
                <p className="font-medium text-[#1A2332] dark:text-[#F5F6F7]">{item.nombre}</p>
                <p className="font-mono text-sm text-accent">{formatearPrecio(item.precio)}</p>
              </div>

              <div className="flex flex-col items-center gap-1">
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => handleRestar(item)}
                    aria-label={`Quitar una unidad de ${item.nombre}`}
                    className="flex h-7 w-7 items-center justify-center rounded-md border border-muted/20 text-base font-medium hover:border-accent hover:text-accent transition-colors"
                  >
                    −
                  </button>

                  <span className="w-6 text-center font-mono text-sm" aria-live="polite">
                    {item.cantidad}
                  </span>

                  <button
                    onClick={() => handleSumar(item)}
                    disabled={enElMaximo}
                    aria-label={`Agregar una unidad de ${item.nombre}`}
                    className="flex h-7 w-7 items-center justify-center rounded-md border border-muted/20 text-base font-medium hover:border-accent hover:text-accent transition-colors disabled:cursor-not-allowed disabled:opacity-30 disabled:hover:border-muted/20 disabled:hover:text-inherit"
                  >
                    +
                  </button>
                </div>

                {/* Avisa en el momento exacto en que ya no se puede sumar mas,
                    en vez de dejar que el usuario adivine por que el "+" esta gris. */}
                {enElMaximo && (
                  <span className="font-mono text-[11px] text-warning">Stock maximo</span>
                )}
              </div>

              <button
                onClick={() => quitarItem(item.productoId)}
                className="text-sm text-danger hover:underline"
                aria-label={`Quitar ${item.nombre} del carrito`}
              >
                Quitar
              </button>
            </div>
          );
        })}
      </div>

      <div className="mt-8 flex items-center justify-between border-t border-muted/15 pt-6">
        <span className="font-display text-xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Total: {formatearPrecio(total)}</span>
        <button
          onClick={handleCheckout}
          className="rounded-md bg-accent px-6 py-3 font-medium text-white transition-colors hover:bg-accent-hover"
        >
          Finalizar compra
        </button>
      </div>
    </div>
  );
}
