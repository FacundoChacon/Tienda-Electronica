package com.facundochacon.ecommerce.repository;

import com.facundochacon.ecommerce.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

    List<DetalleOrden> findByOrdenId(Long ordenId);
}
