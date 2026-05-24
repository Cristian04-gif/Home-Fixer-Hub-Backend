package com.home_fixer_hub.api_gateway.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator; // Clase auxiliar para saber qué rutas ignorar (como login/register)
    @Autowired
    private JwtUtil jwtUtil; // Clase para validar el token (debe tener la misma SECRET key)

    public AuthenticationFilter() {
        super(Config.class);
    }

    // En tu AuthenticationFilter.java
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Verificar cabecera
                if (exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION) == null) {
                    throw new RuntimeException("Falta cabecera de autorización");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                // ... dentro del try del filtro ...
                try {
                    // 1. Validar que el token sea legítimo
                    jwtUtil.validateToken(authHeader);
                    String role = jwtUtil.extractRole(authHeader);
                    String path = exchange.getRequest().getURI().getPath();

                    // 2. Extraer el usuario e inyectarlo en las cabeceras de la petición
                    String username = jwtUtil.extractUsername(authHeader);

                    // Lógica de autorización simple
                    if (path.contains("/admin/") && !role.equals("ADMIN")) {
                        throw new RuntimeException("No tienes permisos de administrador");
                    }

                    if (path.contains("/fixer/") && !role.equals("TECNICO")) {
                        throw new RuntimeException("Solo los técnicos pueden acceder aquí");
                    }

                    if (path.contains("/customer") && !role.equalsIgnoreCase("CLIENTE")) {
                        throw new RuntimeException("Solo los clientes pueden acceder aquí");
                    }

                    exchange.getRequest().mutate()
                            .header("loggedInUser", username) // Esta cabecera llegará al microservicio destino
                            .header("role", role)
                            .build();

                } catch (Exception e) {
                    System.out.println("El filtro falló por esto: " + e.getClass().getName() + " -> " + e.getMessage());
    e.printStackTrace();
                    throw new RuntimeException("Acceso no autorizado");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}