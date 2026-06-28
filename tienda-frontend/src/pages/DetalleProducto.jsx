import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { productoService } from '../services/productoService';
import { BadgeStock } from '../components/BadgeStock';
import { useCarrito } from '../context/CarritoContext';
import { formatearPrecio } from '../utils/formato';

export function DetalleProducto() {
  const { id } = useParams();
  const [producto, setProducto] = useState(null);
  const [cantidad, setCantidad] = useState(1);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [agregado, setAgregado] = useState(false);
  const [aviso, setAviso] = useState(null);

  const { agregarItem, items } = useCarrito();

  useEffect(() => {
    setCargando(true);
    productoService
      .buscarPorId(id)
      .then(setProducto)
      .catch((err) => setError(err.message))
      .finally(() => setCargando(false));
  }, [id]);

  // Si el stock restante baja (por ejemplo, el usuario ya agrego varias unidades
  // y vuelve a este selector), la cantidad elegida no puede quedar por encima
  // de lo que realmente queda disponible.
  useEffect(() => {
    if (producto) {
      const restante = producto.stock - (items.find((i) => i.productoId === producto.id)?.cantidad ?? 0);
      setCantidad((prev) => Math.min(prev, Math.max(1, restante)));
    }
  }, [items, producto]);

  function handleAgregar() {
    const resultado = agregarItem(producto, cantidad);
    if (resultado.limitado) {
      setAviso(`Ya tenes el maximo disponible (${producto.stock}) en el carrito`);
      setTimeout(() => setAviso(null), 3000);
    } else {
      setAgregado(true);
      setTimeout(() => setAgregado(false), 2000);
    }
  }

  if (cargando) return <p className="mx-auto max-w-4xl px-4 py-8 text-muted dark:text-muted-dark">Cargando...</p>;

  if (error || !producto) {
    return (
      <div className="mx-auto max-w-4xl px-4 py-8">
        <p className="rounded-md bg-danger/10 px-4 py-3 text-sm text-danger">
          No encontramos este producto.
        </p>
        <Link to="/" className="mt-4 inline-block text-accent hover:underline">Volver al catalogo</Link>
      </div>
    );
  }

  // Stock en tiempo real: lo que reporto el backend, menos lo que el usuario
  // ya tiene en el carrito para este mismo producto.
  const enElCarrito = items.find((i) => i.productoId === producto.id)?.cantidad ?? 0;
  const stockRestante = producto.stock - enElCarrito;
  const sinStock = stockRestante <= 0;

  return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <Link to="/" className="mb-6 inline-block text-sm text-muted dark:text-muted-dark hover:text-accent">
        ← Volver al catalogo
      </Link>

      <div className="grid gap-8 md:grid-cols-2">
        <div className="aspect-square overflow-hidden rounded-lg bg-surface dark:bg-surface-dark">
          <img src={producto.imagenUrl} alt={producto.nombre} className="h-full w-full object-cover" />
        </div>

        <div className="flex flex-col gap-4">
          <div className="font-mono text-xs uppercase tracking-wide text-muted dark:text-muted-dark">
            {producto.marca} · {producto.categoriaNombre}
          </div>

          <h1 className="font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">{producto.nombre}</h1>

          <BadgeStock stock={stockRestante} />

          {producto.descripcion && (
            <p className="text-sm leading-relaxed text-muted dark:text-muted-dark">
              {producto.descripcion}
            </p>
          )}

          <div className="font-display text-3xl font-bold text-accent">
            {formatearPrecio(producto.precio)}
          </div>

          {!sinStock && (
            <div className="flex items-center gap-3">
              <label htmlFor="cantidad" className="text-sm font-medium">Cantidad</label>
              <input
                id="cantidad"
                type="number"
                min="1"
                max={stockRestante}
                value={cantidad}
                onChange={(e) => setCantidad(Math.max(1, Math.min(stockRestante, Number(e.target.value))))}
                className="w-20 rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
              />
            </div>
          )}

          {aviso && (
            <p className="text-sm font-medium text-warning" role="status">{aviso}</p>
          )}

          <button
            onClick={handleAgregar}
            disabled={sinStock}
            className="rounded-md bg-accent px-6 py-3 font-medium text-white transition-colors hover:bg-accent-hover disabled:cursor-not-allowed disabled:bg-muted/30"
          >
            {sinStock ? 'Sin stock' : agregado ? 'Agregado ✓' : 'Agregar al carrito'}
          </button>
        </div>
      </div>
    </div>
  );
}
