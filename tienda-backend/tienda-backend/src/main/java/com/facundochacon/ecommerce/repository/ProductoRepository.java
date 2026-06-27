package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Catalogo publico: solo productos activos.
    List<Producto> findByActivoTrue();

    // Filtro por categoria, usado en los menus de filtrado del catalogo.
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    // Busqueda por nombre o marca (case-insensitive), para la barra de busqueda.
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    // Filtro combinado por rango de precio, usado en el catalogo avanzado.
    @Query("SELECT p FROM Producto p WHERE p.activo = true " +
           "AND p.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByRangoPrecio(@Param("precioMin") BigDecimal precioMin,
                                      @Param("precioMax") BigDecimal precioMax);
}
