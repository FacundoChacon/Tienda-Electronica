package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Query unica que combina todos los filtros del catalogo (categoria,
     * nombre/marca, rango de precio) de forma OPCIONAL: si un parametro
     * llega como null, esa condicion se ignora gracias al "OR :param IS NULL".
     *
     * Ventaja sobre tener un metodo por cada combinacion de filtros (como
     * estaba antes): el paginado (Pageable) se aplica una sola vez, en un
     * solo lugar, sin duplicar la logica para cada caso.
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = true " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "     OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
           "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    Page<Producto> buscarConFiltros(@Param("categoriaId") Long categoriaId,
                                     @Param("nombre") String nombre,
                                     @Param("precioMin") BigDecimal precioMin,
                                     @Param("precioMax") BigDecimal precioMax,
                                     Pageable pageable);

    // Usados por el dashboard de metricas del panel admin: contar es mucho
    // mas barato que traer todos los productos al frontend solo para contarlos ahi.
    long countByActivoTrue();

    long countByActivoTrueAndStock(Integer stock);
}

