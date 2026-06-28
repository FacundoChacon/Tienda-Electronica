import { useState } from 'react';
import { Laptop, Smartphone, Headphones, Cpu, Mouse, Package } from 'lucide-react';

/**
 * Mapeo de categoria -> [icono, color de fondo, color de icono].
 * Las claves deben coincidir EXACTAMENTE con el nombre de la categoria
 * tal como esta en la base de datos (ver data.sql / panel admin).
 * Si se agrega una categoria nueva que no esta en este mapa, se usa
 * el fallback "Package" con colores neutros (ver getConfigCategoria).
 */
const CONFIG_POR_CATEGORIA = {
  Notebooks: { Icono: Laptop, claro: '#E0E7FF', oscuro: '#1E2A4A', icono: '#4F6BED' },
  Celulares: { Icono: Smartphone, claro: '#FCE7E1', oscuro: '#3A1F1A', icono: '#FF5A1F' },
  Audio: { Icono: Headphones, claro: '#E3F5EC', oscuro: '#13312A', icono: '#3DDC97' },
  'Componentes PC': { Icono: Cpu, claro: '#F3E8FF', oscuro: '#2A1F3D', icono: '#A855F7' },
  Accesorios: { Icono: Mouse, claro: '#FFF4DE', oscuro: '#3D2F12', icono: '#FFC857' },
};

const FALLBACK = { Icono: Package, claro: '#E8E9EB', oscuro: '#252A35', icono: '#7C8B9C' };

function getConfigCategoria(nombreCategoria) {
  return CONFIG_POR_CATEGORIA[nombreCategoria] ?? FALLBACK;
}

/**
 * Muestra la imagen real del producto si existe y carga correctamente.
 * Si no hay imagenUrl, si la imagen falla al cargar (link roto, dominio
 * caido, etc), o si la URL es de placehold.co (los placeholders grises
 * genericos que se usaron como dato de prueba inicial), muestra en su
 * lugar un placeholder con el color e icono de la categoria.
 */
export function ProductImage({ producto, className = '', tamanioIcono = 56 }) {
  const [fallo, setFallo] = useState(false);
  const esPlaceholderGenerico = producto.imagenUrl?.includes('placehold.co') ?? true;
  const tieneImagenReal = Boolean(producto.imagenUrl) && !fallo && !esPlaceholderGenerico;

  if (tieneImagenReal) {
    return (
      <img
        src={producto.imagenUrl}
        alt={producto.nombre}
        loading="lazy"
        onError={() => setFallo(true)}
        className={className}
      />
    );
  }

  const { Icono, claro, oscuro, icono } = getConfigCategoria(producto.categoriaNombre);

  return (
    <div
      className={`flex items-center justify-center bg-[var(--ph-claro)] dark:bg-[var(--ph-oscuro)] ${className}`}
      style={{ '--ph-claro': claro, '--ph-oscuro': oscuro }}
    >
      <Icono size={tamanioIcono} color={icono} strokeWidth={1.5} aria-hidden="true" />
    </div>
  );
}
