package com.facundochacon.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public class ProductoDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "La categoria es obligatoria")
        private Long categoriaId;

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        private String nombre;

        @Size(max = 80)
        private String marca;

        @Size(max = 1000)
        private String descripcion;

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal precio;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;

        private String imagenUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private Long categoriaId;
        private String categoriaNombre;
        private String nombre;
        private String marca;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String imagenUrl;
    }
}
