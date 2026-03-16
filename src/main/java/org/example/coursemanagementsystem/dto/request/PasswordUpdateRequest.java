package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordUpdateRequest {
    @NotBlank(message = "Köhnə şifrə mütləqdir")
    String oldPassword;

    @NotBlank(message = "Yeni şifrə mütləqdir")
    @Size(min = 8, message = "Şifrə ən az 8 simvol olmalıdır")
    String newPassword;
}
