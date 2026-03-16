package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.coursemanagementsystem.util.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBaseResponse {

    Long id;
    String email;
    String finCode;
    Role role;
    Boolean isFirstLogin;
    Boolean isActive;

}
