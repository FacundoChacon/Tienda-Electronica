package com.facundochacon.ecommerce.dto;

import com.facundochacon.ecommerce.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UsuarioDTO {

    // DTO de registro. La contrasena llega en texto plano por HTTPS
    // y se hashea con BCrypt en el service antes de guardarla (nunca se persiste tal cual).
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistroRequest {

        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        private String email;

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
        private String password;
    }

    /**
     * Nunca incluye passwordHash. Este es el ejemplo mas claro de por que
     * separar el DTO de la entidad: si devolvieramos la entidad Usuario
     * directo en el JSON, el hash de la contrasena quedaria expuesto en la respuesta.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private String nombre;
        private String email;
        private Usuario.Rol rol;
        private LocalDateTime fechaRegistro;
    }
}
