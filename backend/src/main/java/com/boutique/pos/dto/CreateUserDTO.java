package com.boutique.pos.dto;

import lombok.*;
import java.time.LocalDateTime;
import com.boutique.pos.model.Role;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
