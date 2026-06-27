package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.UsuarioDTO;
import com.facundochacon.ecommerce.exception.ReglaDeNegocioException;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import com.facundochacon.ecommerce.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    // Publico: cualquiera puede registrarse. En SecurityConfig este endpoint
    // se marca explicitamente como accesible sin token.
    @PostMapping("/registro")
    @Operation(summary = "Registra un nuevo usuario (publico, sin autenticacion)")
    public ResponseEntity<UsuarioDTO.Response> registrar(@Valid @RequestBody UsuarioDTO.RegistroRequest request) {
        UsuarioDTO.Response creado = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Busca un usuario por id, pero solo si quien pregunta es ese mismo
     * usuario o un ADMIN. Evita que un CLIENTE vea los datos de otro
     * cambiando el id en la URL.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca un usuario por id (solo el propio usuario o un ADMIN)")
    public ResponseEntity<UsuarioDTO.Response> buscarPorId(@PathVariable Long id, Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin) {
            Usuario autenticado = usuarioRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ReglaDeNegocioException("Usuario autenticado invalido"));
            if (!autenticado.getId().equals(id)) {
                throw new ReglaDeNegocioException("No tenes permiso para ver este usuario");
            }
        }

        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todos los usuarios (solo ADMIN)")
    public ResponseEntity<List<UsuarioDTO.Response>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}

