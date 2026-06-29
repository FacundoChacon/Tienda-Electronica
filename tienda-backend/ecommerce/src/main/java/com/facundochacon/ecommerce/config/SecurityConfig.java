package com.facundochacon.ecommerce.config;

import com.facundochacon.ecommerce.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuracion real de seguridad. Reglas de acceso por endpoint:
 *
 *   PUBLICOS (sin token):
 *     - POST /api/auth/login
 *     - POST /api/usuarios/registro
 *     - GET  /api/productos/**   (catalogo: cualquiera puede mirar la tienda)
 *     - GET  /api/categorias/**
 *     - Swagger UI
 *
 *   SOLO AUTENTICADO (cualquier rol, requiere token valido):
 *     - POST /api/ordenes        (comprar)
 *     - GET  /api/ordenes/mis-ordenes  (ver mi propio historial)
 *
 *   SOLO ADMIN:
 *     - POST/PUT/DELETE /api/productos/**
 *     - POST/PUT/DELETE /api/categorias/**
 *     - GET  /api/ordenes        (ver TODAS las ordenes)
 *     - PATCH /api/ordenes/{id}/estado
 *     - GET  /api/usuarios       (listar todos los usuarios)
 *
 * La autorizacion fina (ej: que un CLIENTE no pueda ver el historial de OTRO
 * usuario) se completa con @PreAuthorize en los controllers, porque depende
 * del id especifico del recurso, no solo del rol.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // URL del frontend desplegado (ej: https://tu-app.netlify.app), leida de una
    // variable de entorno para no hardcodear el dominio en el codigo. En desarrollo
    // local no se usa (el proxy de Vite evita el problema de CORS), pero Spring
    // necesita un valor por default igual para no fallar al arrancar sin la variable.
    @Value("${CORS_ALLOWED_ORIGIN:http://localhost:5173}")
    private String corsAllowedOrigin;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define que origenes (dominios) externos pueden llamar a esta API desde
     * el navegador. Sin esto, el navegador bloquea las peticiones del frontend
     * en Netlify hacia el backend en Render, aunque el backend funcione bien
     * (CORS es una restriccion que aplica el NAVEGADOR, no el servidor).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(corsAllowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        // Necesario para que el header "Authorization: Bearer <token>" llegue al backend.
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // APIs REST con JWT no usan sesiones de servidor: cada request se autentica
            // de forma independiente con su propio token.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Publicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuarios/registro").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // IMPORTANTE: esta regla va ANTES que el permitAll() general de GET /api/productos/**,
                // porque Spring Security evalua los matchers en orden y usa el PRIMERO que matchee.
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/productos/metricas").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/categorias/**").permitAll()

                // Solo ADMIN: gestion de catalogo y de ordenes en general
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers("/api/ordenes/{id}/estado").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")

                // Todo lo demas requiere estar logueado (cualquier rol)
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
