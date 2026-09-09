package com.boutique.pos.model;

import java.time.LocalDateTime ;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(unique = true, length = 100, nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 100)
    private String firstName;
    @Column(nullable = false, unique = true, length = 100)
    private String lastName;
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.CASHIER;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
