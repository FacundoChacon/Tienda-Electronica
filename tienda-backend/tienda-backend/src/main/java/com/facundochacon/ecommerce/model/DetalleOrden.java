package com.facundochacon.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Linea de detalle dentro de una orden: que producto, cuanta cantidad
 * y a que precio se vendio. El precio se copia al momento de la compra
 * para que cambios futuros en el precio del producto no alteren
 * el historico de ventas ya cerrado.
 */
@Entity
@Table(name = "detalle_ordenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /**
     * Subtotal de esta linea (cantidad * precioUnitario).
     * Se calcula en el momento, no se persiste como columna,
     * para evitar inconsistencias si cantidad o precio cambiaran.
     */
    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
