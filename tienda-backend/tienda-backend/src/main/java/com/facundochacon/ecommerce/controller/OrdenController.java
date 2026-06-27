package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.OrdenDTO;
import com.facundochacon.ecommerce.exception.RecursoNoEncontradoException;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import com.facundochacon.ecommerce.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@Tag(name = "Ordenes", description = "Checkout, historial de compras y gestion de pedidos")
public class OrdenController {

    private final OrdenService ordenService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Checkout: crea una orden a partir del carrito.
     *
     * El usuario YA NO se recibe por parametro: se obtiene del token JWT
     * a traves de Authentication, que Spring Security inyecta automaticamente
     * una vez que JwtAuthFilter valido el token. Asi nadie puede comprar
     * "a nombre de otro usuario" manipulando un parametro en la request.
     */
    @PostMapping
    @Operation(summary = "Crea una orden a partir del carrito (checkout). Requiere estar logueado.")
    public ResponseEntity<OrdenDTO.Response> crearOrden(Authentication authentication,
                                                          @Valid @RequestBody OrdenDTO.Request request) {
        Usuario usuario = obtenerUsuarioAutenticado(authentication);
        OrdenDTO.Response creada = ordenService.crearOrden(usuario.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Historial de compras del usuario logueado (no de cualquier id arbitrario).
     * Cambiamos la ruta de /usuario/{usuarioId} a /mis-ordenes para reforzar
     * que siempre devuelve el historial del usuario autenticado, nunca el de otro.
     */
    @GetMapping("/mis-ordenes")
    @Operation(summary = "Historial de compras del usuario logueado (pantalla 'Mis pedidos')")
    public ResponseEntity<List<OrdenDTO.Response>> listarMisOrdenes(Authentication authentication) {
        Usuario usuario = obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(ordenService.listarPorUsuario(usuario.getId()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todas las ordenes (panel de administracion, solo ADMIN)")
    public ResponseEntity<List<OrdenDTO.Response>> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    /**
     * Busca una orden por id, validando que quien pregunta sea el dueno
     * de la orden o un ADMIN (ver OrdenService.buscarPorIdYValidarPropietario).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca una orden por id (solo el dueno o un ADMIN)")
    public ResponseEntity<OrdenDTO.Response> buscarPorId(@PathVariable Long id, Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(
                ordenService.buscarPorIdYValidarPropietario(id, authentication.getName(), esAdmin));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambia el estado de una orden, ej: PENDIENTE -> ENVIADA (solo ADMIN)")
    public ResponseEntity<OrdenDTO.Response> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody OrdenDTO.CambiarEstadoRequest request) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, request.getEstado()));
    }

    // El "name" del Authentication es el email (asi lo seteamos en JwtAuthFilter,
    // porque UsuarioDetailsService usa el email como username).
    private Usuario obtenerUsuarioAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el usuario autenticado con email " + email));
    }
}

