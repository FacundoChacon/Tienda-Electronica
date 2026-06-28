package com.facundochacon.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Orden de compra generada por un usuario.
 * El total se calcula a partir de los DetalleOrden asociados
 * y el estado refleja el ciclo de vida de la compra.
 */
@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles;

    @PrePersist
    protected void alCrear() {
        this.fecha = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = Estado.PENDIENTE;
        }
    }

    public enum Estado {
        PENDIENTE,
        PAGADA,
        ENVIADA,
        ENTREGADA,
        CANCELADA
    }
}
