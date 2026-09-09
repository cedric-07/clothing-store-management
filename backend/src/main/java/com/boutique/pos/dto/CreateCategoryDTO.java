package com.boutique.pos.dto;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryDTO {
    private String name;
}
