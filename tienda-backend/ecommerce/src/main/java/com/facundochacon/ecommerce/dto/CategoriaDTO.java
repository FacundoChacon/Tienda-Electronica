package com.facundochacon.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CategoriaDTO {

    /**
     * Lo que el cliente envia para crear o editar una categoria.
     * No incluye "id" porque eso lo genera la base de datos.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
        private String nombre;

        @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
        private String descripcion;
    }

    /**
     * Lo que la API devuelve. Separarlo de la entidad evita
     * exponer campos internos o relaciones completas innecesarias.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private String nombre;
        private String descripcion;
    }
}
