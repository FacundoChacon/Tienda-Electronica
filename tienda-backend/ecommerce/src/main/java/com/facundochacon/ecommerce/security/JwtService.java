package com.facundochacon.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Encapsula todo lo relacionado a generar y validar JWT.
 * El resto de la app (filtro, controller de login) no sabe nada
 * de la libreria jjwt; solo le pide a este service "generame un token"
 * o "decime si este token es valido".
 */
@Service
public class JwtService {

    // Se inyectan desde application.properties (jwt.secret, jwt.expiration).
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    /**
     * Genera un token nuevo para un usuario que acaba de loguearse.
     * El "subject" del token es el email (lo usamos como identificador unico).
     */
    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(obtenerClaveFirma())
                .compact();
    }

    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Valida que el token corresponda al usuario dado y que no haya expirado.
     * La firma se valida implicitamente: si alguien modifico el token,
     * parserSignedClaims lanza una excepcion antes de llegar a este punto.
     */
    public boolean esTokenValido(String token, UserDetails userDetails) {
        String email = extraerEmail(token);
        return email.equals(userDetails.getUsername()) && !haExpirado(token);
    }

    private boolean haExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
