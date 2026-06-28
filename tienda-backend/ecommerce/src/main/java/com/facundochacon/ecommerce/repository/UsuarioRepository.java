package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Usado por el login y por el filtro JWT para validar el usuario autenticado.
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
