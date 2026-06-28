import { useState, useEffect, useCallback } from 'react';
import { productoService } from '../../services/productoService';
import { categoriaService } from '../../services/categoriaService';
import { Modal } from '../../components/Modal';
import { Paginacion } from '../../components/Paginacion';
import { formatearPrecio } from '../../utils/formato';

const VACIO = { categoriaId: '', nombre: '', marca: '', descripcion: '', precio: '', stock: '', imagenUrl: '' };
const PRODUCTOS_POR_PAGINA = 12;

export function AdminProductos() {
  const [productos, setProductos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [modalAbierto, setModalAbierto] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(VACIO);

  const [pagina, setPagina] = useState(0);
  const [totalPaginas, setTotalPaginas] = useState(0);

  const cargar = useCallback(() => {
    setCargando(true);
    Promise.all([
      productoService.listar({ page: pagina, size: PRODUCTOS_POR_PAGINA }),
      categoriaService.listar(),
    ])
      .then(([paginaProductos, c]) => {
        setProductos(paginaProductos.contenido);
        setTotalPaginas(paginaProductos.totalPaginas);
        setCategorias(c);
      })
      .catch((err) => setError(err.message))
      .finally(() => setCargando(false));
  }, [pagina]);

  useEffect(cargar, [cargar]);

  function abrirCrear() {
    setEditando(null);
    setForm(VACIO);
    setModalAbierto(true);
  }

  function abrirEditar(producto) {
    setEditando(producto);
    setForm({
      categoriaId: producto.categoriaId,
      nombre: producto.nombre,
      marca: producto.marca || '',
      descripcion: producto.descripcion || '',
      precio: producto.precio,
      stock: producto.stock,
      imagenUrl: producto.imagenUrl || '',
    });
    setModalAbierto(true);
  }

  async function handleGuardar(e) {
    e.preventDefault();
    try {
      const payload = {
        ...form,
        categoriaId: Number(form.categoriaId),
        precio: Number(form.precio),
        stock: Number(form.stock),
      };
      if (editando) {
        await productoService.actualizar(editando.id, payload);
      } else {
        await productoService.crear(payload);
      }
      setModalAbierto(false);
      cargar();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleEliminar(id) {
    if (!confirm('¿Dar de baja este producto del catalogo?')) return;
    try {
      await productoService.eliminar(id);
      cargar();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Productos</h1>
        <button
          onClick={abrirCrear}
          className="rounded-md bg-accent px-4 py-2 text-sm font-medium text-white hover:bg-accent-hover"
        >
          + Nuevo producto
        </button>
      </div>

      {error && <p className="mb-4 text-sm text-danger">{error}</p>}
      {cargando && <p className="text-muted dark:text-muted-dark">Cargando...</p>}

      <div className="overflow-hidden rounded-lg border border-muted/15">
        <table className="w-full text-sm">
          <thead className="bg-muted/5">
            <tr>
              <th className="px-4 py-2 text-left font-medium">Producto</th>
              <th className="px-4 py-2 text-left font-medium">Categoria</th>
              <th className="px-4 py-2 text-right font-medium">Precio</th>
              <th className="px-4 py-2 text-right font-medium">Stock</th>
              <th className="px-4 py-2 text-right font-medium">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productos.map((p) => (
              <tr key={p.id} className="border-t border-muted/10">
                <td className="px-4 py-2 font-medium">{p.nombre}</td>
                <td className="px-4 py-2 text-muted dark:text-muted-dark">{p.categoriaNombre}</td>
                <td className="px-4 py-2 text-right font-mono">{formatearPrecio(p.precio)}</td>
                <td className="px-4 py-2 text-right font-mono">{p.stock}</td>
                <td className="px-4 py-2 text-right">
                  <button onClick={() => abrirEditar(p)} className="mr-3 text-accent hover:underline">Editar</button>
                  <button onClick={() => handleEliminar(p.id)} className="text-danger hover:underline">Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Paginacion paginaActual={pagina} totalPaginas={totalPaginas} onCambiarPagina={setPagina} />

      {modalAbierto && (
        <Modal titulo={editando ? 'Editar producto' : 'Nuevo producto'} onCerrar={() => setModalAbierto(false)}>
          <form onSubmit={handleGuardar} className="flex flex-col gap-3">
            <div>
              <label className="mb-1 block text-sm font-medium">Categoria</label>
              <select
                required
                value={form.categoriaId}
                onChange={(e) => setForm({ ...form, categoriaId: e.target.value })}
                className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
              >
                <option value="">Seleccionar...</option>
                {categorias.map((c) => (
                  <option key={c.id} value={c.id}>{c.nombre}</option>
                ))}
              </select>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="mb-1 block text-sm font-medium">Nombre</label>
                <input
                  required
                  value={form.nombre}
                  onChange={(e) => setForm({ ...form, nombre: e.target.value })}
                  className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                />
              </div>
              <div>
                <label className="mb-1 block text-sm font-medium">Marca</label>
                <input
                  value={form.marca}
                  onChange={(e) => setForm({ ...form, marca: e.target.value })}
                  className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                />
              </div>
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium">Descripcion</label>
              <textarea
                value={form.descripcion}
                onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
                className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                rows={2}
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="mb-1 block text-sm font-medium">Precio</label>
                <input
                  required
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={form.precio}
                  onChange={(e) => setForm({ ...form, precio: e.target.value })}
                  className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                />
              </div>
              <div>
                <label className="mb-1 block text-sm font-medium">Stock</label>
                <input
                  required
                  type="number"
                  min="0"
                  value={form.stock}
                  onChange={(e) => setForm({ ...form, stock: e.target.value })}
                  className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                />
              </div>
            </div>

            <div>
              <label className="mb-1 block text-sm font-medium">URL de imagen</label>
              <input
                value={form.imagenUrl}
                onChange={(e) => setForm({ ...form, imagenUrl: e.target.value })}
                placeholder="https://..."
                className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
              />
            </div>

            <button type="submit" className="mt-2 rounded-md bg-accent px-4 py-2 font-medium text-white hover:bg-accent-hover">
              Guardar
            </button>
          </form>
        </Modal>
      )}
    </div>
  );
}
