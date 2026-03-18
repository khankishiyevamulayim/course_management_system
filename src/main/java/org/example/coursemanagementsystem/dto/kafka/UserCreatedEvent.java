package org.example.coursemanagementsystem.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coursemanagementsystem.util.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private Long userId;
    private String email;
    private String finCode;
    private Role role;
    private String name;
    private String surname;
}