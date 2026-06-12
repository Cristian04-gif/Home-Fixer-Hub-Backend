package com.home_fixer_hub.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private AuthenticationFilter authenticationFilter;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("identity-route", r -> r.path("/api/auth/**")
						.uri("lb://IDENTITY-SERVICE"))
				.route("profile-service", r -> r.path("/api/profile/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://PROFILE-SERVICE"))
				.route("catalog-service", r -> r.path("/api/catalog/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://CATALOG-SERVICE"))
				.build();
	}

}
