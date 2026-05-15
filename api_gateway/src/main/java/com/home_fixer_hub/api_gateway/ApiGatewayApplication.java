package com.home_fixer_hub.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


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
						.uri("lb://IDENTITY-SERVICE"))
				.route("profile-service", r -> r.path("/api/profile/**")
						.uri("lb://PROFILE-SERVICE"))
				.route("catalog-service", r -> r.path("/api/catalog/**")
						.uri("lb://CATALOG-SERVICE"))
				.build();
	}

}
