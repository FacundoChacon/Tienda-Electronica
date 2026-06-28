import { useState, useEffect, useCallback } from 'react';
import { productoService } from '../services/productoService';
import { categoriaService } from '../services/categoriaService';
import { ProductCard } from '../components/ProductCard';
import { Paginacion } from '../components/Paginacion';
import { useCarrito } from '../context/CarritoContext';

const PRODUCTOS_POR_PAGINA = 12;

export function Catalogo() {
  const [productos, setProductos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  const [categoriaId, setCategoriaId] = useState('');
  const [busqueda, setBusqueda] = useState('');
  const [precioMin, setPrecioMin] = useState('');
  const [precioMax, setPrecioMax] = useState('');

  const [pagina, setPagina] = useState(0);
  const [totalPaginas, setTotalPaginas] = useState(0);
  const [totalElementos, setTotalElementos] = useState(0);

  const { agregarItem } = useCarrito();

  const cargarProductos = useCallback(async () => {
    setCargando(true);
    setError(null);
    try {
      const filtros = { page: pagina, size: PRODUCTOS_POR_PAGINA };
      if (categoriaId) filtros.categoriaId = categoriaId;
      if (busqueda) filtros.nombre = busqueda;
      if (precioMin) filtros.precioMin = precioMin;
      if (precioMax) filtros.precioMax = precioMax;

      const data = await productoService.listar(filtros);
      setProductos(data.contenido);
      setTotalPaginas(data.totalPaginas);
      setTotalElementos(data.totalElementos);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, [categoriaId, busqueda, precioMin, precioMax, pagina]);

  useEffect(() => {
    categoriaService.listar().then(setCategorias).catch(() => {});
  }, []);

  // Si el usuario cambia un filtro mientras esta en, por ejemplo, la pagina 3,
  // y el nuevo filtro solo tiene 1 pagina de resultados, sin este reseteo
  // se quedaria viendo una pagina vacia. Volvemos siempre a la pagina 0
  // cuando cambia CUALQUIER filtro (no cuando cambia la pagina en si).
  useEffect(() => {
    setPagina(0);
  }, [categoriaId, busqueda, precioMin, precioMax]);

  // Los filtros (y la pagina) se vuelven a pedir al backend cada vez que cambian
  // (debounce simple para no disparar un request por cada tecla en la busqueda).
  useEffect(() => {
    const timeout = setTimeout(cargarProductos, 300);
    return () => clearTimeout(timeout);
  }, [cargarProductos]);

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="mb-6 font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Catalogo</h1>

      <div className="mb-8 flex flex-wrap gap-3">
        <input
          type="text"
          placeholder="Buscar por nombre o marca..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
          className="flex-1 min-w-[200px] rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
        />

        <select
          value={categoriaId}
          onChange={(e) => setCategoriaId(e.target.value)}
          className="rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
        >
          <option value="">Todas las categorias</option>
          {categorias.map((c) => (
            <option key={c.id} value={c.id}>{c.nombre}</option>
          ))}
        </select>

        <input
          type="number"
          placeholder="Precio min"
          value={precioMin}
          onChange={(e) => setPrecioMin(e.target.value)}
          className="w-28 rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
        />
        <input
          type="number"
          placeholder="Precio max"
          value={precioMax}
          onChange={(e) => setPrecioMax(e.target.value)}
          className="w-28 rounded-md border border-muted/20 bg-surface dark:bg-surface-dark px-3 py-2 text-sm"
        />
      </div>

      {cargando && <p className="text-muted dark:text-muted-dark">Cargando catalogo...</p>}

      {error && (
        <p className="rounded-md bg-danger/10 px-4 py-3 text-sm text-danger">
          No pudimos cargar el catalogo: {error}
        </p>
      )}

      {!cargando && !error && productos.length === 0 && (
        <p className="text-muted dark:text-muted-dark">
          No encontramos productos con esos filtros. Probá ajustarlos.
        </p>
      )}

      {!cargando && !error && productos.length > 0 && (
        <p className="mb-4 text-sm text-muted dark:text-muted-dark">
          {totalElementos} {totalElementos === 1 ? 'producto encontrado' : 'productos encontrados'}
        </p>
      )}

      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-4">
        {productos.map((producto) => (
          <ProductCard key={producto.id} producto={producto} onAgregarAlCarrito={agregarItem} />
        ))}
      </div>

      <Paginacion paginaActual={pagina} totalPaginas={totalPaginas} onCambiarPagina={setPagina} />
    </div>
  );
}

