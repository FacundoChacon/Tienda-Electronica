package com.facundochacon.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * CONFIGURACION TEMPORAL para esta etapa de pruebas con data.sql.
 *
 * Solo declara el PasswordEncoder (BCrypt), que UsuarioService necesita
 * para hashear contraseñas en el registro. Por ahora Spring Security
 * esta en el classpath (lo agregamos en el pom.xml) pero SIN configurar,
 * lo que significa que TODOS los endpoints estan protegidos por defecto
 * con un login basico autogenerado (usuario "user" y una contrasena
 * que Spring imprime en la consola al arrancar).
 *
 * Cuando armemos la capa de seguridad completa (proximo paso), esta clase
 * se reemplaza por un SecurityConfig real que:
 *   - Deja publicos los endpoints de catalogo (GET productos/categorias) y registro/login.
 *   - Protege el resto con JWT y valida roles (ADMIN vs CLIENTE).
 *
 * Mientras tanto, para probar los controllers con Postman/Swagger sin
 * pelear con el login basico, ver la nota en el README sobre como
 * desactivarlo temporalmente.
 */
@Configuration
public class SecurityConfigTemporal {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Abre TODOS los endpoints sin autenticacion. Es exclusivamente para esta
    // etapa de pruebas con data.sql. El proximo paso reemplaza este metodo
    // por reglas reales (publico vs protegido vs solo-ADMIN) usando JWT.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
