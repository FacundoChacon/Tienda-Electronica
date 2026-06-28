package com.facundochacon.ecommerce.controller;

import com.facundochacon.ecommerce.dto.AuthDTO;
import com.facundochacon.ecommerce.exception.ReglaDeNegocioException;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import com.facundochacon.ecommerce.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Login y generacion de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Login: valida email + password contra la base de datos
     * (AuthenticationManager delega en UsuarioDetailsService + el PasswordEncoder)
     * y, si son correctos, genera y devuelve un JWT.
     */
    @PostMapping("/login")
    @Operation(summary = "Inicia sesion y devuelve un token JWT")
    public AuthDTO.LoginResponse login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new ReglaDeNegocioException("Email o contrasena incorrectos");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generarToken(userDetails);

        // Buscamos el usuario de nuevo solo para devolver su rol en la respuesta
        // (UserDetails ya lo tiene como authority "ROLE_X", pero esto es mas legible para el frontend).
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        return new AuthDTO.LoginResponse(token, usuario.getEmail(), usuario.getRol().name());
    }
}
