import { useState } from 'react';
import { Link } from 'react-router-dom';
import { BadgeStock } from './BadgeStock';
import { ProductImage } from './ProductImage';
import { useCarrito } from '../context/CarritoContext';
import { formatearPrecio } from '../utils/formato';

export function ProductCard({ producto, onAgregarAlCarrito }) {
  const [aviso, setAviso] = useState(null);
  const { items } = useCarrito();

  // Stock "en tiempo real": el stock que vino del backend, menos lo que
  // el usuario ya tiene cargado en el carrito para este mismo producto.
  // Asi el contador baja en vivo a medida que agrega unidades, sin esperar
  // a recargar la pagina o volver a pedirle el producto al backend.
  const enElCarrito = items.find((i) => i.productoId === producto.id)?.cantidad ?? 0;
  const stockRestante = producto.stock - enElCarrito;
  const sinStock = stockRestante <= 0;

  function handleAgregar() {
    const resultado = onAgregarAlCarrito(producto);
    if (resultado?.limitado) {
      setAviso(`Solo quedan ${producto.stock} unidades disponibles`);
      setTimeout(() => setAviso(null), 2500);
    }
  }

  return (
    <div className="group flex flex-col overflow-hidden rounded-lg border border-muted/15 bg-surface dark:bg-surface-dark transition-shadow hover:shadow-lg">
      <Link to={`/productos/${producto.id}`} className="block">
        <div className="aspect-square overflow-hidden bg-canvas dark:bg-canvas-dark">
          <ProductImage
            producto={producto}
            className="h-full w-full object-cover transition-transform duration-200 group-hover:scale-105"
          />
        </div>
      </Link>

      <div className="flex flex-1 flex-col gap-2 p-4">
        {/* Eyebrow estilo "ficha tecnica": marca + categoria como dato, no decoracion */}
        <div className="flex items-center justify-between font-mono text-xs uppercase tracking-wide text-muted dark:text-muted-dark">
          <span>{producto.marca}</span>
          <span>{producto.categoriaNombre}</span>
        </div>

        <Link to={`/productos/${producto.id}`}>
          <h3 className="font-display text-base font-semibold leading-snug text-[#1A2332] dark:text-[#F5F6F7] hover:text-accent transition-colors">
            {producto.nombre}
          </h3>
        </Link>

        <BadgeStock stock={stockRestante} />

        {aviso && (
          <p className="text-xs font-medium text-warning" role="status">{aviso}</p>
        )}

        <div className="mt-auto flex items-center justify-between pt-2">
          <span className="font-display text-lg font-bold text-accent">
            {formatearPrecio(producto.precio)}
          </span>
          <button
            onClick={handleAgregar}
            disabled={sinStock}
            className="rounded-md bg-accent px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-accent-hover disabled:cursor-not-allowed disabled:bg-muted/30"
          >
            {sinStock ? 'Sin stock' : 'Agregar'}
          </button>
        </div>
      </div>
    </div>
  );
}
