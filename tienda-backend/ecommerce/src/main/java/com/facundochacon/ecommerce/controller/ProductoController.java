package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.MetricasDTO;
import com.facundochacon.ecommerce.dto.PageResponse;
import com.facundochacon.ecommerce.dto.ProductoDTO;
import com.facundochacon.ecommerce.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Catalogo de productos: listado paginado, filtros y gestion")
public class ProductoController {

    private final ProductoService productoService;

    // Limite defensivo: si el frontend (o alguien probando la API a mano)
    // pide un size enorme, lo recortamos para no sobrecargar la base de datos.
    private static final int TAMANIO_MAXIMO_PAGINA = 50;

    /**
     * Endpoint principal del catalogo: paginado, con filtros opcionales
     * combinables por query param.
     *
     * Ejemplos:
     *   GET /api/productos                              -> pagina 0, 12 productos (default)
     *   GET /api/productos?page=1&size=12                 -> segunda pagina
     *   GET /api/productos?categoriaId=2&page=0&size=12    -> filtrado por categoria, paginado
     *   GET /api/productos?nombre=samsung                  -> busqueda por nombre/marca, paginado
     *   GET /api/productos?precioMin=100&precioMax=500      -> filtrado por rango de precio, paginado
     *
     * "page" es 0-indexed (la primera pagina es 0, no 1), siguiendo la convencion de Spring Data.
     */
    @GetMapping
    @Operation(summary = "Lista el catalogo de forma paginada, con filtros opcionales por categoria, nombre o rango de precio")
    public ResponseEntity<PageResponse<ProductoDTO.Response>> listarCatalogo(
            @Parameter(description = "Filtra por id de categoria") @RequestParam(required = false) Long categoriaId,
            @Parameter(description = "Busca por nombre o marca") @RequestParam(required = false) String nombre,
            @Parameter(description = "Precio minimo") @RequestParam(required = false) BigDecimal precioMin,
            @Parameter(description = "Precio maximo") @RequestParam(required = false) BigDecimal precioMax,
            @Parameter(description = "Numero de pagina, empezando en 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de productos por pagina") @RequestParam(defaultValue = "12") int size) {

        int tamanioSeguro = Math.min(size, TAMANIO_MAXIMO_PAGINA);
        Pageable pageable = PageRequest.of(page, tamanioSeguro);

        return ResponseEntity.ok(
                productoService.listarCatalogo(categoriaId, nombre, precioMin, precioMax, pageable));
    }

    @GetMapping("/metricas")
    @Operation(summary = "Metricas de productos para el dashboard de administracion (solo ADMIN)")
    public ResponseEntity<MetricasDTO> obtenerMetricas() {
        return ResponseEntity.ok(productoService.obtenerMetricas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un producto por id (pantalla de detalle)")
    public ResponseEntity<ProductoDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo producto (solo ADMIN)")
    public ResponseEntity<ProductoDTO.Response> crear(@Valid @RequestBody ProductoDTO.Request request) {
        ProductoDTO.Response creado = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un producto existente (solo ADMIN)")
    public ResponseEntity<ProductoDTO.Response> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody ProductoDTO.Request request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Da de baja un producto del catalogo (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

