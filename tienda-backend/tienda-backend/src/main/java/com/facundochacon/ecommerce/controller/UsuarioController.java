package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.UsuarioDTO;
import com.facundochacon.ecommerce.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Publico: cualquiera puede registrarse. En SecurityConfig este endpoint
    // se marca explicitamente como accesible sin token.
    @PostMapping("/registro")
    @Operation(summary = "Registra un nuevo usuario (publico, sin autenticacion)")
    public ResponseEntity<UsuarioDTO.Response> registrar(@Valid @RequestBody UsuarioDTO.RegistroRequest request) {
        UsuarioDTO.Response creado = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un usuario por id")
    public ResponseEntity<UsuarioDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos los usuarios (solo ADMIN)")
    public ResponseEntity<List<UsuarioDTO.Response>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}
