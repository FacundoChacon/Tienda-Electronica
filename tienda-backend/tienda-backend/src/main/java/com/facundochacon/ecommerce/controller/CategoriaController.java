package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.CategoriaDTO;
import com.facundochacon.ecommerce.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gestion de categorias del catalogo")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Lista todas las categorias")
    public ResponseEntity<List<CategoriaDTO.Response>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca una categoria por id")
    public ResponseEntity<CategoriaDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    // Solo ADMIN puede crear/editar/eliminar categorias.
    // La restriccion real se aplica con @PreAuthorize una vez que armemos la capa de seguridad.
    @PostMapping
    @Operation(summary = "Crea una nueva categoria (solo ADMIN)")
    public ResponseEntity<CategoriaDTO.Response> crear(@Valid @RequestBody CategoriaDTO.Request request) {
        CategoriaDTO.Response creada = categoriaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una categoria existente (solo ADMIN)")
    public ResponseEntity<CategoriaDTO.Response> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody CategoriaDTO.Request request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una categoria (solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
