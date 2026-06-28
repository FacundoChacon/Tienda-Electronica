import { useState, useEffect } from 'react';
import { ordenService } from '../../services/ordenService';
import { formatearPrecio, formatearFecha } from '../../utils/formato';

const ESTADOS = ['PENDIENTE', 'PAGADA', 'ENVIADA', 'ENTREGADA', 'CANCELADA'];

export function AdminOrdenes() {
  const [ordenes, setOrdenes] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  function cargar() {
    setCargando(true);
    ordenService.listarTodas().then(setOrdenes).catch((err) => setError(err.message)).finally(() => setCargando(false));
  }

  useEffect(cargar, []);

  async function handleCambiarEstado(id, nuevoEstado) {
    try {
      await ordenService.cambiarEstado(id, nuevoEstado);
      cargar();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Ordenes</h1>

      {error && <p className="mb-4 text-sm text-danger">{error}</p>}
      {cargando && <p className="text-muted dark:text-muted-dark">Cargando...</p>}
      {!cargando && ordenes.length === 0 && (
        <p className="text-muted dark:text-muted-dark">Todavia no hay ordenes registradas.</p>
      )}

      <div className="flex flex-col gap-3">
        {ordenes.map((orden) => (
          <div key={orden.id} className="rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark p-4">
            <div className="mb-3 flex items-center justify-between">
              <span className="font-mono text-sm text-muted dark:text-muted-dark">
                Pedido #{orden.id} · usuario #{orden.usuarioId} · {formatearFecha(orden.fecha)}
              </span>

              <select
                value={orden.estado}
                onChange={(e) => handleCambiarEstado(orden.id, e.target.value)}
                className="rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-2 py-1 font-mono text-xs"
              >
                {ESTADOS.map((estado) => (
                  <option key={estado} value={estado}>{estado}</option>
                ))}
              </select>
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
