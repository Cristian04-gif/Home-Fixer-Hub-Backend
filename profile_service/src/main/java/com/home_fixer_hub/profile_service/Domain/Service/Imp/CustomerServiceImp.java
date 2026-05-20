package com.home_fixer_hub.profile_service.Domain.Service.Imp;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.profile_service.Domain.Client.IdentityClient;
import com.home_fixer_hub.profile_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllCustomerDTO;
import com.home_fixer_hub.profile_service.Domain.Service.CustomerService;
import com.home_fixer_hub.profile_service.Persistense.Mapping.CustomerMapper;
import com.home_fixer_hub.profile_service.Persistense.Model.Customer;
import com.home_fixer_hub.profile_service.Persistense.Repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final IdentityClient identityClient;

    @Override
    public Mono<AllCustomerDTO> getAll(int page, int size) {
        Mono<List<CustomerDTO>> customers = customerRepository
                .findAllBy(PageRequest.of(page, size, Sort.by("id").ascending())).map(customerMapper::toDTO)
                .collectList();

        Mono<Long> count = customerRepository.count();

        return Mono.zip(customers, count).map(tuple -> {
            List<CustomerDTO> customerDTOs = tuple.getT1();
            long totalElements = tuple.getT2();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            boolean last = page >= totalPages - 1;
            return AllCustomerDTO.builder()
                    .customerDTOs(customerDTOs)
                    .pageNumber(page)
                    .pageSize(totalElements > 0 ? size : 0)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .last(last)
                    .build();
        });

    }

    @Override
    public Mono<CustomerDTO> getById(String customerId) {
        return customerRepository.findById(customerId).map(customerMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el cliente")));
    }

    @Override
    public Mono<CustomerDTO> register(CustomerDTO customerDTO) {
        return identityClient.isValidUser(customerDTO.userId()).flatMap(isValid -> {
            if (isValid) {
                Customer customer = customerMapper.toEntity(customerDTO);
                customer.setId(UUID.randomUUID().toString());
                return customerRepository.save(customer).map(customerMapper::toDTO);
            }
            return Mono.error(new RuntimeException("usuario de identidad no encontrado"));
        });
    }

    @Override
    public Mono<CustomerDTO> getByUserId(String userId) {
        return customerRepository.findByIdUsuario(userId).map(customerMapper::toDTO)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("No se encontor el cluente vinculado a ese ID de usuario")));
    }

}
