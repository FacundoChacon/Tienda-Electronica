package com.facundochacon.ecommerce.exception;

/**
 * Se lanza cuando se busca una entidad por ID y no existe
 * (ej: GET /api/productos/999 si el producto 999 no existe).
 * El GlobalExceptionHandler la traduce a un 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
