package com.facundochacon.ecommerce.service;

import com.facundochacon.ecommerce.dto.UsuarioDTO;
import com.facundochacon.ecommerce.exception.RecursoNoEncontradoException;
import com.facundochacon.ecommerce.exception.ReglaDeNegocioException;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Bean provisto por SecurityConfig (proximo paso). Se inyecta por interfaz
    // para no acoplar este service a una implementacion concreta de hashing.
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO.Response registrar(UsuarioDTO.RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ReglaDeNegocioException(
                    "Ya existe una cuenta registrada con el email " + request.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Usuario.Rol.CLIENTE);

        return aResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO.Response buscarPorId(Long id) {
        return aResponse(obtenerEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO.Response> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::aResponse)
                .toList();
    }

    private Usuario obtenerEntidadPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el usuario con id " + id));
    }

    private UsuarioDTO.Response aResponse(Usuario usuario) {
        return new UsuarioDTO.Response(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getFechaRegistro()
        );
    }
}
