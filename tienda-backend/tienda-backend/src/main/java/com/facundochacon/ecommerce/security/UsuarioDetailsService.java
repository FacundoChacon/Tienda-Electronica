package com.facundochacon.ecommerce.security;

import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Le ensena a Spring Security como cargar un usuario desde NUESTRA base de datos.
 * Spring Security trabaja con su propia interfaz UserDetails, asi que este
 * service traduce un Usuario (entidad JPA) a un UserDetails (lo que entiende Spring).
 *
 * El "rol" de nuestro Usuario se traduce a un GrantedAuthority con prefijo "ROLE_",
 * que es la convencion que despues usamos en @PreAuthorize("hasRole('ADMIN')").
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe un usuario con el email " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
