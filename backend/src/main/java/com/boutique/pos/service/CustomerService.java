package com.boutique.pos.service;

import com.boutique.pos.model.Customer;
import com.boutique.pos.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
