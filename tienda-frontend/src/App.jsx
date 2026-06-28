import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout';
import { RutaProtegida } from './components/RutaProtegida';

import { Catalogo } from './pages/Catalogo';
import { DetalleProducto } from './pages/DetalleProducto';
import { Login } from './pages/Login';
import { Registro } from './pages/Registro';
import { Carrito } from './pages/Carrito';
import { Checkout } from './pages/Checkout';
import { MisOrdenes } from './pages/MisOrdenes';

import { AdminLayout } from './pages/admin/AdminLayout';
import { AdminResumen } from './pages/admin/AdminResumen';
import { AdminProductos } from './pages/admin/AdminProductos';
import { AdminCategorias } from './pages/admin/AdminCategorias';
import { AdminOrdenes } from './pages/admin/AdminOrdenes';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          {/* Publicas */}
          <Route path="/" element={<Catalogo />} />
          <Route path="/productos/:id" element={<DetalleProducto />} />
          <Route path="/login" element={<Login />} />
          <Route path="/registro" element={<Registro />} />
          <Route path="/carrito" element={<Carrito />} />

          {/* Requieren login (cualquier rol) */}
          <Route path="/checkout" element={<RutaProtegida><Checkout /></RutaProtegida>} />
          <Route path="/mis-ordenes" element={<RutaProtegida><MisOrdenes /></RutaProtegida>} />

          {/* Solo ADMIN */}
          <Route
            path="/admin"
            element={<RutaProtegida soloAdmin><AdminLayout /></RutaProtegida>}
          >
            <Route index element={<AdminResumen />} />
            <Route path="productos" element={<AdminProductos />} />
            <Route path="categorias" element={<AdminCategorias />} />
            <Route path="ordenes" element={<AdminOrdenes />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
