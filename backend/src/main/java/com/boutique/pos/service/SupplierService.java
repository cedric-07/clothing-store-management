package com.boutique.pos.service;

import com.boutique.pos.dto.CreateSupplierDTO;
import com.boutique.pos.dto.SupplierDTO;
import com.boutique.pos.model.Supplier;
import com.boutique.pos.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    public SupplierDTO create(CreateSupplierDTO createSupplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setName(createSupplierDTO.getName());
        supplier.setPhone(createSupplierDTO.getPhone());
        supplier.setEmail(createSupplierDTO.getEmail());
        supplier.setAddress(createSupplierDTO.getAddress());
        Supplier savedSupplier = supplierRepository.save(supplier);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(savedSupplier.getId());
        supplierDTO.setName(savedSupplier.getName());
        supplierDTO.setPhone(savedSupplier.getPhone());
        supplierDTO.setEmail(savedSupplier.getEmail());
        supplierDTO.setAddress(savedSupplier.getAddress());
        supplierDTO.setActive(savedSupplier.getActive());
        return supplierDTO;
    }
    public List<SupplierDTO> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream().map(supplier -> {
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setId(supplier.getId());
            supplierDTO.setName(supplier.getName());
            supplierDTO.setPhone(supplier.getPhone());
            supplierDTO.setEmail(supplier.getEmail());
            supplierDTO.setAddress(supplier.getAddress());
            supplierDTO.setActive(supplier.getActive());
            return supplierDTO;
        }).toList();
    }
    public SupplierDTO getById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplier.getId());
        supplierDTO.setName(supplier.getName());
        supplierDTO.setPhone(supplier.getPhone());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setActive(supplier.getActive());
        return supplierDTO;
    }
    public SupplierDTO update(Long id, CreateSupplierDTO createSupplierDTO) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplier.setName(createSupplierDTO.getName());
        supplier.setPhone(createSupplierDTO.getPhone());
        supplier.setEmail(createSupplierDTO.getEmail());
        supplier.setAddress(createSupplierDTO.getAddress());
        Supplier updatedSupplier = supplierRepository.save(supplier);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(updatedSupplier.getId());
        supplierDTO.setName(updatedSupplier.getName());
        supplierDTO.setPhone(updatedSupplier.getPhone());
        supplierDTO.setEmail(updatedSupplier.getEmail());
        supplierDTO.setAddress(updatedSupplier.getAddress());
        supplierDTO.setActive(updatedSupplier.getActive());
        return supplierDTO;
    }
    public SupplierDTO deactivate(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplier.setActive(false);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(updatedSupplier.getId());
        supplierDTO.setName(updatedSupplier.getName());
        supplierDTO.setPhone(updatedSupplier.getPhone());
        supplierDTO.setEmail(updatedSupplier.getEmail());
        supplierDTO.setAddress(updatedSupplier.getAddress());
        supplierDTO.setActive(updatedSupplier.getActive());
        return supplierDTO;
    }



}
