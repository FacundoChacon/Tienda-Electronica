package com.facundochacon.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa a un usuario del sistema, sea cliente o administrador.
 * El campo "rol" define los permisos: CLIENTE puede comprar,
 * ADMIN puede gestionar productos, categorias y ver todas las ordenes.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Se almacena el hash de la contrasena (BCrypt), nunca en texto plano.
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Orden> ordenes;

    @PrePersist
    protected void alCrear() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.rol == null) {
            this.rol = Rol.CLIENTE;
        }
    }

    public enum Rol {
        CLIENTE,
        ADMIN
    }
}
