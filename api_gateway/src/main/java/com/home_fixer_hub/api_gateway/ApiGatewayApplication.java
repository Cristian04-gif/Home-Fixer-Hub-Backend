package com.home_fixer_hub.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import com.home_fixer_hub.api_gateway.Config.AuthenticationFilter;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("identity-route", r -> r.path("/auth/**")
						// .filters(f -> f.preserveHostHeader()) // Añadimos esto por seguridad
						.uri("lb://IDENTITY-SERVICE")) // Asegúrate que coincida con Eureka
				// En el Gateway (ApiGatewayApplication o config)
				.route("profile-service", r -> r.path("/profiles/**")
						//.filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://PROFILE-SERVICE"))
				.build();
	}

}
