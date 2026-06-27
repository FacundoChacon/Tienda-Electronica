package com.facundochacon.ecommerce.dto;

import com.facundochacon.ecommerce.model.Orden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenDTO {

    /**
     * Lo que el frontend envia al confirmar el checkout: el carrito completo.
     * No incluye precios -- esos se obtienen del producto en el backend,
     * para que el cliente no pueda manipular el precio desde el navegador.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotEmpty(message = "La orden debe tener al menos un producto")
        @Valid
        private List<ItemRequest> items;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRequest {

        @NotNull(message = "El productoId es obligatorio")
        private Long productoId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private Long usuarioId;
        private LocalDateTime fecha;
        private Orden.Estado estado;
        private BigDecimal total;
        private List<DetalleResponse> detalles;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleResponse {

        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }

    // Usado por el panel de administracion para cambiar el estado de una orden.
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CambiarEstadoRequest {

        @NotNull(message = "El estado es obligatorio")
        private Orden.Estado estado;
    }
}
