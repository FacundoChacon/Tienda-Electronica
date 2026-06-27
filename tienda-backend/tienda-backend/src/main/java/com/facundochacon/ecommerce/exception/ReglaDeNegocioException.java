package com.facundochacon.ecommerce.exception;

/**
 * Se lanza cuando una operacion viola una regla de negocio,
 * por ejemplo intentar comprar mas unidades de las que hay en stock.
 * El GlobalExceptionHandler la traduce a un 400.
 */
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
