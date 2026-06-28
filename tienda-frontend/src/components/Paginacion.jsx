/**
 * Controles de paginacion reusables. Trabaja con paginas 0-indexed
 * (igual que Spring Data) pero se los muestra al usuario como 1-indexed,
 * que es lo natural para una persona ("Pagina 1 de 5", no "Pagina 0 de 5").
 */
export function Paginacion({ paginaActual, totalPaginas, onCambiarPagina }) {
  if (totalPaginas <= 1) return null;

  const esPrimera = paginaActual === 0;
  const esUltima = paginaActual >= totalPaginas - 1;

  // Muestra hasta 5 numeros de pagina centrados alrededor de la actual,
  // para no listar 100 botones si el catalogo tiene muchas paginas.
  const inicio = Math.max(0, Math.min(paginaActual - 2, totalPaginas - 5));
  const numerosVisibles = Array.from(
    { length: Math.min(5, totalPaginas) },
    (_, i) => inicio + i
  );

  return (
    <nav aria-label="Paginacion del catalogo" className="mt-8 flex items-center justify-center gap-1">
      <button
        onClick={() => onCambiarPagina(paginaActual - 1)}
        disabled={esPrimera}
        aria-label="Pagina anterior"
        className="rounded-md px-3 py-2 text-sm font-medium text-muted dark:text-muted-dark hover:bg-muted/10 disabled:cursor-not-allowed disabled:opacity-40"
      >
        ←
      </button>

      {numerosVisibles.map((num) => (
        <button
          key={num}
          onClick={() => onCambiarPagina(num)}
          aria-current={num === paginaActual ? 'page' : undefined}
          className={`min-w-[2.25rem] rounded-md px-3 py-2 text-sm font-medium transition-colors ${
            num === paginaActual
              ? 'bg-accent text-white'
              : 'text-muted dark:text-muted-dark hover:bg-muted/10'
          }`}
        >
          {num + 1}
        </button>
      ))}

      <button
        onClick={() => onCambiarPagina(paginaActual + 1)}
        disabled={esUltima}
        aria-label="Pagina siguiente"
        className="rounded-md px-3 py-2 text-sm font-medium text-muted dark:text-muted-dark hover:bg-muted/10 disabled:cursor-not-allowed disabled:opacity-40"
      >
        →
      </button>
    </nav>
  );
}
