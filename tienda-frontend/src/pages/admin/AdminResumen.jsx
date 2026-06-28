import { useState, useEffect } from 'react';
import { productoService } from '../../services/productoService';
import { ordenService } from '../../services/ordenService';
import { formatearPrecio } from '../../utils/formato';

export function AdminResumen() {
  const [metricas, setMetricas] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    Promise.all([productoService.obtenerMetricas(), ordenService.listarTodas()])
      .then(([metricasProductos, ordenes]) => {
        const ingresosTotales = ordenes.reduce((acc, o) => acc + o.total, 0);
        const pendientes = ordenes.filter((o) => o.estado === 'PENDIENTE').length;

        setMetricas({
          totalProductos: metricasProductos.totalProductos,
          sinStock: metricasProductos.productosSinStock,
          totalOrdenes: ordenes.length,
          pendientes,
          ingresosTotales,
        });
      })
      .catch((err) => setError(err.message));
  }, []);

  if (error) return <p className="text-sm text-danger">{error}</p>;
  if (!metricas) return <p className="text-muted dark:text-muted-dark">Cargando...</p>;

  const tarjetas = [
    { etiqueta: 'Productos en catalogo', valor: metricas.totalProductos },
    { etiqueta: 'Sin stock', valor: metricas.sinStock, alerta: metricas.sinStock > 0 },
    { etiqueta: 'Ordenes totales', valor: metricas.totalOrdenes },
    { etiqueta: 'Pendientes de envio', valor: metricas.pendientes, alerta: metricas.pendientes > 0 },
    { etiqueta: 'Ingresos totales', valor: formatearPrecio(metricas.ingresosTotales) },
  ];

  return (
    <div>
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Resumen</h1>
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3">
        {tarjetas.map((t) => (
          <div key={t.etiqueta} className="rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark p-4">
            <p className="text-xs uppercase tracking-wide text-muted dark:text-muted-dark">{t.etiqueta}</p>
            <p className={`mt-1 font-display text-2xl font-bold ${t.alerta ? 'text-warning' : ''}`}>
              {t.valor}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
