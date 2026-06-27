package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.OrdenDTO;
import com.facundochacon.ecommerce.model.Orden;
import com.facundochacon.ecommerce.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@Tag(name = "Ordenes", description = "Checkout, historial de compras y gestion de pedidos")
public class OrdenController {

    private final OrdenService ordenService;

    /**
     * Checkout: crea una orden a partir del carrito.
     *
     * NOTA TEMPORAL: el usuarioId viene por @RequestParam mientras no tenemos JWT.
     * Cuando armemos la capa de seguridad (proximo paso), esto se reemplaza
     * por el usuario autenticado, obtenido del token: no se va a recibir mas
     * por parametro, sino de un objeto Authentication/Principal que inyecta Spring Security.
     * Asi se evita que cualquiera pueda crear ordenes a nombre de otro usuario.
     */
    @PostMapping
    @Operation(summary = "Crea una orden a partir del carrito (checkout)")
    public ResponseEntity<OrdenDTO.Response> crearOrden(@RequestParam Long usuarioId,
                                                          @Valid @RequestBody OrdenDTO.Request request) {
        OrdenDTO.Response creada = ordenService.crearOrden(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Historial de compras de un usuario (pantalla 'Mis pedidos')")
    public ResponseEntity<List<OrdenDTO.Response>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.listarPorUsuario(usuarioId));
    }

    @GetMapping
    @Operation(summary = "Lista todas las ordenes (panel de administracion, solo ADMIN)")
    public ResponseEntity<List<OrdenDTO.Response>> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca una orden por id")
    public ResponseEntity<OrdenDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambia el estado de una orden, ej: PENDIENTE -> ENVIADA (solo ADMIN)")
    public ResponseEntity<OrdenDTO.Response> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody OrdenDTO.CambiarEstadoRequest request) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, request.getEstado()));
    }
}
