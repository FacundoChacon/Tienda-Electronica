package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    // Historial de compras de un cliente (pantalla "Mis pedidos").
    List<Orden> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    // Usado por el panel de administracion para filtrar pedidos por estado.
    List<Orden> findByEstado(Orden.Estado estado);
}
