package org.example.coursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.coursemanagementsystem.util.Role;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, length = 100)
    String email;

    @Column(nullable = false)
    String password;

    @Column(name = "fin_code", unique = true, nullable = false, length = 7)
    String finCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    Role role;

    @Builder.Default
    Boolean isFirstLogin = true;

    @Builder.Default
    boolean isActive = true;


}


