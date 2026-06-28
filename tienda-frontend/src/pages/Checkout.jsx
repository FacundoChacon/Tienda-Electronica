import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCarrito } from '../context/CarritoContext';
import { ordenService } from '../services/ordenService';
import { formatearPrecio } from '../utils/formato';

export function Checkout() {
  const { items, total, vaciarCarrito } = useCarrito();
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  async function handleConfirmar() {
    setProcesando(true);
    setError(null);
    try {
      // El backend recalcula precios y valida stock con sus propios datos;
      // aca solo mandamos que producto y cuanta cantidad, nunca el precio.
      const itemsRequest = items.map((i) => ({ productoId: i.productoId, cantidad: i.cantidad }));
      const orden = await ordenService.crear(itemsRequest);
      vaciarCarrito();
      navigate(`/mis-ordenes`, { state: { ordenCreada: orden.id } });
    } catch (err) {
      // El backend devuelve un mensaje claro, por ejemplo "Stock insuficiente para...",
      // que mostramos tal cual: el usuario necesita saber exactamente que producto fallo.
      setError(err.message);
    } finally {
      setProcesando(false);
    }
  }

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Confirmar compra</h1>

      <div className="flex flex-col gap-2 rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark p-4">
        {items.map((item) => (
          <div key={item.productoId} className="flex justify-between text-sm">
            <span>{item.cantidad}x {item.nombre}</span>
            <span className="font-mono">{formatearPrecio(item.precio * item.cantidad)}</span>
          </div>
        ))}
        <div className="mt-2 flex justify-between border-t border-muted/15 pt-2 font-display font-bold">
          <span>Total</span>
          <span className="text-accent">{formatearPrecio(total)}</span>
        </div>
      </div>

      {error && (
        <p className="mt-4 rounded-md bg-danger/10 px-4 py-3 text-sm text-danger">{error}</p>
      )}

      <button
        onClick={handleConfirmar}
        disabled={procesando}
        className="mt-6 w-full rounded-md bg-accent px-6 py-3 font-medium text-white transition-colors hover:bg-accent-hover disabled:opacity-60"
      >
        {procesando ? 'Procesando...' : 'Confirmar compra'}
      </button>
    </div>
  );
}
