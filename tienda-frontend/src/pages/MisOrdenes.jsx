import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { ordenService } from '../services/ordenService';
import { formatearPrecio, formatearFecha } from '../utils/formato';

const COLOR_ESTADO = {
  PENDIENTE: 'bg-warning text-[#412402]',
  PAGADA: 'bg-success text-[#04342C]',
  ENVIADA: 'bg-success text-[#04342C]',
  ENTREGADA: 'bg-success text-[#04342C]',
  CANCELADA: 'bg-danger text-white',
};

export function MisOrdenes() {
  const [ordenes, setOrdenes] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const location = useLocation();

  useEffect(() => {
    ordenService
      .misOrdenes()
      .then(setOrdenes)
      .catch((err) => setError(err.message))
      .finally(() => setCargando(false));
  }, []);

  return (
    <div className="mx-auto max-w-3xl px-4 py-8">
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Mis pedidos</h1>

      {location.state?.ordenCreada && (
        <p className="mb-6 rounded-md bg-success/10 px-4 py-3 text-sm text-success">
          Pedido #{location.state.ordenCreada} confirmado correctamente.
        </p>
      )}

      {cargando && <p className="text-muted dark:text-muted-dark">Cargando...</p>}
      {error && <p className="text-sm text-danger">{error}</p>}

      {!cargando && ordenes.length === 0 && (
        <p className="text-muted dark:text-muted-dark">Todavia no hiciste ningun pedido.</p>
      )}

      <div className="flex flex-col gap-3">
        {ordenes.map((orden) => (
          <div key={orden.id} className="rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark p-4">
            <div className="mb-3 flex items-center justify-between">
              <span className="font-mono text-sm text-muted dark:text-muted-dark">
                Pedido #{orden.id} · {formatearFecha(orden.fecha)}
              </span>
              <span className={`rounded-full px-2.5 py-0.5 font-mono text-xs font-medium ${COLOR_ESTADO[orden.estado]}`}>
                {orden.estado}
              </span>
            </div>

            <div className="flex flex-col gap-1">
              {orden.detalles.map((d) => (
                <div key={d.productoId} className="flex justify-between text-sm">
                  <span>{d.cantidad}x {d.productoNombre}</span>
                  <span className="font-mono">{formatearPrecio(d.subtotal)}</span>
                </div>
              ))}
            </div>

            <div className="mt-3 flex justify-between border-t border-muted/15 pt-2 font-display font-bold">
              <span>Total</span>
              <span className="text-accent">{formatearPrecio(orden.total)}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
