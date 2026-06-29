package com.facundochacon.ecommerce.config;

import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@tienda.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@tienda.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin@tienda.com / admin123");
        } else {
            log.info("El usuario admin ya existe, se omite la creacion");
        }
    }
}
