package com.facundochacon.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Envoltorio generico para respuestas paginadas. Nunca devolvemos
 * org.springframework.data.domain.Page directamente desde un controller:
 * es una clase pensada para uso interno de Spring Data, con campos como
 * "pageable" o "sort" que no queremos exponer tal cual en la API publica.
 * Este DTO expone solo lo que el frontend realmente necesita para paginar.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> contenido;
    private int paginaActual;       // 0-indexed, igual que Spring Data
    private int totalPaginas;
    private long totalElementos;
    private int tamanioPagina;
    private boolean esUltimaPagina;

    /**
     * Convierte un Page<E> de Spring Data en un PageResponse<T>, mapeando
     * cada elemento con la funcion "mapper" (tipicamente, entidad -> DTO).
     */
    public static <E, T> PageResponse<T> desde(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast()
        );
    }
}
