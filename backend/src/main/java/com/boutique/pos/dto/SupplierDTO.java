package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SupplierDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Boolean active;
}
