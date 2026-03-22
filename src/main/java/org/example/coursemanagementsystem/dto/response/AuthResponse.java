package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.coursemanagementsystem.util.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String accessToken;
    String refreshToken;
    Role role;
    Boolean isFirstLogin;
    Long userId;
    String displayName;
}