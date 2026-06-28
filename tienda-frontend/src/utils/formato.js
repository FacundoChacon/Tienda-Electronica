// Formatea numeros como moneda argentina, ej: 980000 -> "$980.000"
export function formatearPrecio(valor) {
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    minimumFractionDigits: 0,
  }).format(valor);
}

export function formatearFecha(fechaIso) {
  return new Date(fechaIso).toLocaleDateString('es-AR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}
