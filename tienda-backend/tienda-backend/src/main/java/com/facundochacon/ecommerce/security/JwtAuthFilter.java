package com.facundochacon.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Se ejecuta una vez por cada request (OncePerRequestFilter) ANTES de que
 * la peticion llegue al controller. Su trabajo:
 *
 *   1. Leer el header "Authorization: Bearer <token>".
 *   2. Si no hay token, dejar pasar el request tal cual (puede ser un endpoint publico).
 *   3. Si hay token, validarlo y cargar el usuario en el SecurityContext,
 *      para que el resto de Spring Security (y nuestros controllers, via
 *      Authentication/Principal) sepan quien esta haciendo la peticion.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No hay token: dejamos que el request siga su curso.
            // Si el endpoint requiere autenticacion, SecurityConfig lo va a rechazar mas adelante.
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // saca el prefijo "Bearer "
        String email = jwtService.extraerEmail(token);

        // Solo autenticamos si todavia no hay nadie autenticado en este request
        // (evita procesar el token de nuevo si ya se valido antes en la misma cadena de filtros).
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.esTokenValido(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
