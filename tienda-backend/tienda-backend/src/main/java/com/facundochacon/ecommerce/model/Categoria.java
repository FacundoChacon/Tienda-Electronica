package com.facundochacon.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Categoria de productos (por ej: "Notebooks", "Celulares", "Audio").
 * Separar la categoria en su propia tabla permite armar filtros
 * de busqueda en el catalogo de una forma consistente.
 */
@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producto> productos;
}
