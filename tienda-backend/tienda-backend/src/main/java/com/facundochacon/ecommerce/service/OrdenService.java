package com.facundochacon.ecommerce.service;

import com.facundochacon.ecommerce.dto.OrdenDTO;
import com.facundochacon.ecommerce.exception.RecursoNoEncontradoException;
import com.facundochacon.ecommerce.exception.ReglaDeNegocioException;
import com.facundochacon.ecommerce.model.DetalleOrden;
import com.facundochacon.ecommerce.model.Orden;
import com.facundochacon.ecommerce.model.Producto;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.OrdenRepository;
import com.facundochacon.ecommerce.repository.ProductoRepository;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;

    /**
     * Crea una orden a partir del carrito (lista de items).
     *
     * Pasos, todos dentro de la misma transaccion:
     *   1. Validar que el usuario exista.
     *   2. Por cada item: buscar el producto, validar stock, calcular el subtotal
     *      con el precio ACTUAL del producto (nunca confiar en un precio que venga del frontend).
     *   3. Descontar el stock de cada producto.
     *   4. Calcular el total sumando los subtotales.
     *   5. Persistir la orden junto con sus detalles.
     *
     * Si cualquier item falla por stock insuficiente, @Transactional revierte
     * todo lo anterior: no queda stock descontado a medias.
     */
    @Transactional
    public OrdenDTO.Response crearOrden(Long usuarioId, OrdenDTO.Request request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el usuario con id " + usuarioId));

        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setEstado(Orden.Estado.PENDIENTE);

        List<DetalleOrden> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrdenDTO.ItemRequest item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontro el producto con id " + item.getProductoId()));

            if (!producto.getActivo()) {
                throw new ReglaDeNegocioException(
                        "El producto '" + producto.getNombre() + "' ya no esta disponible");
            }

            // Descuenta el stock; lanza ReglaDeNegocioException si no alcanza,
            // lo que aborta toda la transaccion (rollback automatico de Spring).
            productoService.descontarStock(producto.getId(), item.getCantidad());

            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalles.add(detalle);

            total = total.add(detalle.getSubtotal());
        }

        orden.setDetalles(detalles);
        orden.setTotal(total);

        return aResponse(ordenRepository.save(orden));
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO.Response> listarPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(this::aResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO.Response> listarTodas() {
        return ordenRepository.findAll().stream()
                .map(this::aResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrdenDTO.Response buscarPorId(Long id) {
        return aResponse(obtenerEntidadPorId(id));
    }

    /**
     * Igual que buscarPorId, pero ademas valida que quien pregunta sea
     * el dueno de la orden o un ADMIN. Sin esta validacion, cualquier usuario
     * logueado podria ver el pedido de otro simplemente cambiando el id en la URL
     * (un caso clasico de "broken object level authorization").
     */
    @Transactional(readOnly = true)
    public OrdenDTO.Response buscarPorIdYValidarPropietario(Long id, String emailAutenticado, boolean esAdmin) {
        Orden orden = obtenerEntidadPorId(id);
        boolean esPropietario = orden.getUsuario().getEmail().equalsIgnoreCase(emailAutenticado);

        if (!esPropietario && !esAdmin) {
            throw new ReglaDeNegocioException("No tenes permiso para ver esta orden");
        }
        return aResponse(orden);
    }

    // Usado por el panel de administracion (ej: marcar como ENVIADA o ENTREGADA).
    @Transactional
    public OrdenDTO.Response cambiarEstado(Long id, Orden.Estado nuevoEstado) {
        Orden orden = obtenerEntidadPorId(id);
        orden.setEstado(nuevoEstado);
        return aResponse(ordenRepository.save(orden));
    }

    private Orden obtenerEntidadPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la orden con id " + id));
    }

    private OrdenDTO.Response aResponse(Orden orden) {
        List<OrdenDTO.DetalleResponse> detalles = orden.getDetalles().stream()
                .map(d -> new OrdenDTO.DetalleResponse(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()
                ))
                .toList();

        return new OrdenDTO.Response(
                orden.getId(),
                orden.getUsuario().getId(),
                orden.getFecha(),
                orden.getEstado(),
                orden.getTotal(),
                detalles
        );
    }
}
