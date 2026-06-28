/**
 * Contador de stock en tiempo real. Muestra el numero exacto de unidades
 * disponibles (no una categoria como "stock bajo"), para que el usuario
 * sepa con precision cuanto puede comprar. El color sigue funcionando
 * como senal visual rapida (rojo = agotado, amarillo = bajo, verde = ok),
 * pero el dato preciso es el numero, no el color.
 */
export function BadgeStock({ stock }) {
  const sinStock = stock === 0;
  const stockBajo = stock > 0 && stock <= 5;

  const color = sinStock ? 'text-danger' : stockBajo ? 'text-warning' : 'text-success';
  const punto = sinStock ? 'bg-danger' : stockBajo ? 'bg-warning' : 'bg-success';

  return (
    <div
      className="flex items-center gap-1.5"
      role="status"
      aria-label={sinStock ? 'Sin stock' : `${stock} unidades disponibles`}
    >
      <span className={`h-2 w-2 rounded-full ${punto}`} aria-hidden="true" />
      <span className={`font-mono text-xs font-medium ${color}`}>
        {sinStock ? 'Sin stock' : `${stock} disponibles`}
      </span>
    </div>
  );
}
