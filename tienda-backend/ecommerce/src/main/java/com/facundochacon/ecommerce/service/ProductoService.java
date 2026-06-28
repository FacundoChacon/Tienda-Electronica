package com.facundochacon.ecommerce.service;

import com.facundochacon.ecommerce.dto.MetricasDTO;
import com.facundochacon.ecommerce.dto.PageResponse;
import com.facundochacon.ecommerce.dto.ProductoDTO;
import com.facundochacon.ecommerce.exception.RecursoNoEncontradoException;
import com.facundochacon.ecommerce.exception.ReglaDeNegocioException;
import com.facundochacon.ecommerce.model.Categoria;
import com.facundochacon.ecommerce.model.Producto;
import com.facundochacon.ecommerce.repository.CategoriaRepository;
import com.facundochacon.ecommerce.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Catalogo publico, paginado y con filtros opcionales combinables.
     * Los 4 parametros de filtro pueden venir null (sin ese filtro aplicado);
     * Pageable ya trae la pagina, el tamanio y el orden pedidos por el cliente.
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductoDTO.Response> listarCatalogo(Long categoriaId, String nombre,
                                                              BigDecimal precioMin, BigDecimal precioMax,
                                                              Pageable pageable) {
        Page<Producto> pagina = productoRepository.buscarConFiltros(categoriaId, nombre, precioMin, precioMax, pageable);
        return PageResponse.desde(pagina, this::aResponse);
    }

    @Transactional(readOnly = true)
    public ProductoDTO.Response buscarPorId(Long id) {
        return aResponse(obtenerEntidadPorId(id));
    }

    // Metricas para el dashboard del panel admin: dos conteos directos en SQL,
    // mucho mas eficiente que traer todos los productos al frontend para contarlos.
    @Transactional(readOnly = true)
    public MetricasDTO obtenerMetricas() {
        long total = productoRepository.countByActivoTrue();
        long sinStock = productoRepository.countByActivoTrueAndStock(0);
        return new MetricasDTO(total, sinStock);
    }

    @Transactional
    public ProductoDTO.Response crear(ProductoDTO.Request request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la categoria con id " + request.getCategoriaId()));

        Producto producto = new Producto();
        producto.setCategoria(categoria);
        producto.setActivo(true);
        mapearRequestAEntidad(request, producto);

        return aResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoDTO.Response actualizar(Long id, ProductoDTO.Request request) {
        Producto producto = obtenerEntidadPorId(id);

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la categoria con id " + request.getCategoriaId()));

        producto.setCategoria(categoria);
        mapearRequestAEntidad(request, producto);

        return aResponse(productoRepository.save(producto));
    }

    // Baja logica: el producto deja de aparecer en el catalogo pero no se borra
    // (asi se conserva el historico de ordenes que lo referencian).
    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerEntidadPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    /**
     * Verifica que haya stock suficiente y lo descuenta.
     * Lo usa OrdenService al confirmar una compra.
     * Lanza ReglaDeNegocioException si no hay stock suficiente,
     * lo que el GlobalExceptionHandler traduce en un 400 claro para el frontend.
     */
    @Transactional
    public void descontarStock(Long productoId, int cantidad) {
        Producto producto = obtenerEntidadPorId(productoId);
        if (producto.getStock() < cantidad) {
            throw new ReglaDeNegocioException(
                    "Stock insuficiente para el producto '" + producto.getNombre() +
                    "'. Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    private Producto obtenerEntidadPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el producto con id " + id));
    }

    private void mapearRequestAEntidad(ProductoDTO.Request request, Producto producto) {
        producto.setNombre(request.getNombre());
        producto.setMarca(request.getMarca());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
    }

    private ProductoDTO.Response aResponse(Producto producto) {
        return new ProductoDTO.Response(
                producto.getId(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getNombre(),
                producto.getMarca(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenUrl()
        );
    }
}
