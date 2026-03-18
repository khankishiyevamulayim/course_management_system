package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 8, max = 20, message = "Şifrə 8-20 simvol aralığında olmalıdır")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Şifrədə ən azı bir rəqəm, bir kiçik, bir böyük hərf və bir xüsusi simvol (@#$%^&+=!) olmalıdır"
    )
    String newPassword;
}
