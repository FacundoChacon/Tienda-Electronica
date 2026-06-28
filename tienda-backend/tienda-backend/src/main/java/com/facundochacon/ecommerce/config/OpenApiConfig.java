package com.facundochacon.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Define el esquema de seguridad que Swagger UI necesita para mostrar
 * el boton "Authorize". Sin esta clase, springdoc no tiene ninguna referencia
 * a JWT y simplemente no dibuja el boton, aunque el backend ya valide tokens
 * correctamente (son dos cosas independientes: una es la seguridad real
 * de la API, la otra es que la documentacion "sepa" que esa seguridad existe).
 *
 * "bearerAuth" es solo un nombre interno que usamos para conectar
 * el SecurityScheme con el SecurityRequirement; podria llamarse distinto
 * mientras ambos usen el mismo nombre.
 */
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_BEARER = "bearerAuth";

    @Bean
    public OpenAPI configuracionOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tienda Electronica")
                        .description("Catalogo, carrito, ordenes y autenticacion JWT para un e-commerce de electronica")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BEARER))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_BEARER, new SecurityScheme()
                                .name(ESQUEMA_BEARER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}