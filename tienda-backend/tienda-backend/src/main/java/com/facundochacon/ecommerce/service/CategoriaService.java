package com.facundochacon.ecommerce.service;

import com.facundochacon.ecommerce.dto.CategoriaDTO;
import com.facundochacon.ecommerce.exception.RecursoNoEncontradoException;
import com.facundochacon.ecommerce.model.Categoria;
import com.facundochacon.ecommerce.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO.Response> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::aResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO.Response buscarPorId(Long id) {
        Categoria categoria = obtenerEntidadPorId(id);
        return aResponse(categoria);
    }

    @Transactional
    public CategoriaDTO.Response crear(CategoriaDTO.Request request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return aResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaDTO.Response actualizar(Long id, CategoriaDTO.Request request) {
        Categoria categoria = obtenerEntidadPorId(id);
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return aResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidadPorId(id);
        categoriaRepository.delete(categoria);
    }

    // Metodo interno reusado por buscarPorId, actualizar y eliminar.
    private Categoria obtenerEntidadPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la categoria con id " + id));
    }

    private CategoriaDTO.Response aResponse(Categoria categoria) {
        return new CategoriaDTO.Response(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}
