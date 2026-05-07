package com.home_fixer_hub.catalog_service.Persitense.Repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.catalog_service.Persitense.Model.TypeService;

@Repository
public interface TypeServiceRepository extends ReactiveCrudRepository<TypeService, String> {

}
