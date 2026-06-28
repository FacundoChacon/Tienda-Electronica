import { createContext, useContext, useState, useEffect, useCallback, useMemo } from 'react';

const CarritoContext = createContext(null);

const CLAVE_STORAGE = 'carrito';

export function CarritoProvider({ children }) {
  const [items, setItems] = useState(() => {
    const guardado = localStorage.getItem(CLAVE_STORAGE);
    return guardado ? JSON.parse(guardado) : [];
  });

  // Cada cambio en el carrito se persiste, asi no se pierde si el usuario
  // recarga la pagina antes de llegar al checkout.
  useEffect(() => {
    localStorage.setItem(CLAVE_STORAGE, JSON.stringify(items));
  }, [items]);

  /**
   * Agrega un producto al carrito, respetando el stock disponible en tiempo real.
   * Si el producto ya esta en el carrito, suma a la cantidad existente, pero
   * el TOTAL nunca puede superar producto.stock (lo que el backend reporto
   * como disponible en el momento de cargar el catalogo/detalle).
   *
   * Devuelve un objeto { agregado, cantidadFinal, limitado } para que la UI
   * pueda avisarle al usuario si se topo con el limite de stock.
   */
  const agregarItem = useCallback((producto, cantidad = 1) => {
    let resultado = { agregado: 0, cantidadFinal: 0, limitado: false };

    setItems((prev) => {
      const existente = prev.find((i) => i.productoId === producto.id);
      const cantidadPrevia = existente ? existente.cantidad : 0;
      const cantidadDeseada = cantidadPrevia + cantidad;
      const cantidadFinal = Math.min(cantidadDeseada, producto.stock);

      resultado = {
        agregado: cantidadFinal - cantidadPrevia,
        cantidadFinal,
        limitado: cantidadDeseada > producto.stock,
      };

      if (existente) {
        return prev.map((i) =>
          i.productoId === producto.id
            ? { ...i, cantidad: cantidadFinal, stockDisponible: producto.stock }
            : i
        );
      }

      if (cantidadFinal <= 0) return prev;

      return [
        ...prev,
        {
          productoId: producto.id,
          nombre: producto.nombre,
          precio: producto.precio,
          imagenUrl: producto.imagenUrl,
          stockDisponible: producto.stock,
          cantidad: cantidadFinal,
        },
      ];
    });

    return resultado;
  }, []);

  /**
   * Cambia la cantidad de un item ya en el carrito, respetando el stock
   * disponible (cap superior) y un minimo de 1 (para bajar a 0 hay que
   * usar quitarItem explicitamente, asi el usuario no lo hace "por accidente"
   * con el boton de restar).
   */
  const actualizarCantidad = useCallback((productoId, cantidad) => {
    setItems((prev) =>
      prev.map((i) =>
        i.productoId === productoId
          ? { ...i, cantidad: Math.max(1, Math.min(cantidad, i.stockDisponible)) }
          : i
      )
    );
  }, []);

  const quitarItem = useCallback((productoId) => {
    setItems((prev) => prev.filter((i) => i.productoId !== productoId));
  }, []);

  const vaciarCarrito = useCallback(() => setItems([]), []);

  const total = useMemo(
    () => items.reduce((acc, i) => acc + i.precio * i.cantidad, 0),
    [items]
  );

  const cantidadTotal = useMemo(
    () => items.reduce((acc, i) => acc + i.cantidad, 0),
    [items]
  );

  return (
    <CarritoContext.Provider
      value={{ items, agregarItem, actualizarCantidad, quitarItem, vaciarCarrito, total, cantidadTotal }}
    >
      {children}
    </CarritoContext.Provider>
  );
}

export function useCarrito() {
  const context = useContext(CarritoContext);
  if (!context) throw new Error('useCarrito debe usarse dentro de un CarritoProvider');
  return context;
}
