import { useState, useEffect } from 'react';
import { categoriaService } from '../../services/categoriaService';
import { Modal } from '../../components/Modal';

const VACIO = { nombre: '', descripcion: '' };

export function AdminCategorias() {
  const [categorias, setCategorias] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [modalAbierto, setModalAbierto] = useState(false);
  const [editando, setEditando] = useState(null); // null = creando, objeto = editando
  const [form, setForm] = useState(VACIO);

  function cargar() {
    setCargando(true);
    categoriaService.listar().then(setCategorias).catch((err) => setError(err.message)).finally(() => setCargando(false));
  }

  useEffect(cargar, []);

  function abrirCrear() {
    setEditando(null);
    setForm(VACIO);
    setModalAbierto(true);
  }

  function abrirEditar(categoria) {
    setEditando(categoria);
    setForm({ nombre: categoria.nombre, descripcion: categoria.descripcion || '' });
    setModalAbierto(true);
  }

  async function handleGuardar(e) {
    e.preventDefault();
    try {
      if (editando) {
        await categoriaService.actualizar(editando.id, form);
      } else {
        await categoriaService.crear(form);
      }
      setModalAbierto(false);
      cargar();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleEliminar(id) {
    if (!confirm('¿Eliminar esta categoria? Esta accion no se puede deshacer.')) return;
    try {
      await categoriaService.eliminar(id);
      cargar();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="font-display text-2xl font-bold text-[#1A2332] dark:text-[#F5F6F7]">Categorias</h1>
        <button
          onClick={abrirCrear}
          className="rounded-md bg-accent px-4 py-2 text-sm font-medium text-white hover:bg-accent-hover"
        >
          + Nueva categoria
        </button>
      </div>

      {error && <p className="mb-4 text-sm text-danger">{error}</p>}
      {cargando && <p className="text-muted dark:text-muted-dark">Cargando...</p>}

      <div className="overflow-hidden rounded-lg border border-muted/15">
        <table className="w-full text-sm">
          <thead className="bg-muted/5">
            <tr>
              <th className="px-4 py-2 text-left font-medium">Nombre</th>
              <th className="px-4 py-2 text-left font-medium">Descripcion</th>
              <th className="px-4 py-2 text-right font-medium">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {categorias.map((c) => (
              <tr key={c.id} className="border-t border-muted/10">
                <td className="px-4 py-2 font-medium">{c.nombre}</td>
                <td className="px-4 py-2 text-muted dark:text-muted-dark">{c.descripcion}</td>
                <td className="px-4 py-2 text-right">
                  <button onClick={() => abrirEditar(c)} className="mr-3 text-accent hover:underline">Editar</button>
                  <button onClick={() => handleEliminar(c.id)} className="text-danger hover:underline">Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modalAbierto && (
        <Modal titulo={editando ? 'Editar categoria' : 'Nueva categoria'} onCerrar={() => setModalAbierto(false)}>
          <form onSubmit={handleGuardar} className="flex flex-col gap-4">
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
              <label className="mb-1 block text-sm font-medium">Descripcion</label>
              <textarea
                value={form.descripcion}
                onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
                className="w-full rounded-md border border-muted/20 bg-canvas dark:bg-canvas-dark px-3 py-2 text-sm"
                rows={3}
              />
            </div>
            <button type="submit" className="rounded-md bg-accent px-4 py-2 font-medium text-white hover:bg-accent-hover">
              Guardar
            </button>
          </form>
        </Modal>
      )}
    </div>
  );
}
