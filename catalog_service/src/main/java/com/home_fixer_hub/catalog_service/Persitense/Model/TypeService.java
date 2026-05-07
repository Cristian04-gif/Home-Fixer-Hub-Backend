package com.home_fixer_hub.catalog_service.Persitense.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table("servicios")
@Data
@NoArgsConstructor
public class TypeService /*implements Persistable<String>*/ {
    @Id
    private String id;
    private String nombre;


}
