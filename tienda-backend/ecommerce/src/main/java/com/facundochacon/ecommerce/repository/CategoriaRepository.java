package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
