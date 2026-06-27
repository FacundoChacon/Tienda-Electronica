package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.ProductoDTO;
import com.facundochacon.ecommerce.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Catalogo de productos: listado, filtros y gestion")
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Endpoint principal del catalogo. Soporta filtros opcionales por query param,
     * pensado para que el frontend combine los filtros de la pantalla de catalogo
     * sin necesitar multiples endpoints distintos.
     *
     * Ejemplos:
     *   GET /api/productos                          -> catalogo completo
     *   GET /api/productos?categoriaId=2             -> filtrado por categoria
     *   GET /api/productos?nombre=samsung             -> busqueda por nombre/marca
     *   GET /api/productos?precioMin=100&precioMax=500 -> filtrado por rango de precio
     */
    @GetMapping
    @Operation(summary = "Lista el catalogo, con filtros opcionales por categoria, nombre o rango de precio")
    public ResponseEntity<List<ProductoDTO.Response>> listarCatalogo(
            @Parameter(description = "Filtra por id de categoria") @RequestParam(required = false) Long categoriaId,
            @Parameter(description = "Busca por nombre o marca") @RequestParam(required = false) String nombre,
            @Parameter(description = "Precio minimo") @RequestParam(required = false) BigDecimal precioMin,
            @Parameter(description = "Precio maximo") @RequestParam(required = false) BigDecimal precioMax) {

        if (categoriaId != null) {
            return ResponseEntity.ok(productoService.filtrarPorCategoria(categoriaId));
        }
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
        }
        if (precioMin != null && precioMax != null) {
            return ResponseEntity.ok(productoService.filtrarPorRangoPrecio(precioMin, precioMax));
        }
        return ResponseEntity.ok(productoService.listarCatalogo());
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
