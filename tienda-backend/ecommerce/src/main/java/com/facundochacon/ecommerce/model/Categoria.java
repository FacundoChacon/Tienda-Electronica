package com.facundochacon.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Categoria de productos (ej: "Notebooks", "Celulares", "Audio").
 * Separar la categoria en su propia tabla permite armar filtros
 * de busqueda en el catalogo de forma consistente.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
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
