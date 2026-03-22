package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.AssertTrue;
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
public class ResetPasswordRequest {

    @NotBlank(message = "Token boş ola bilməz")
    String token;

    @NotBlank(message = "Yeni şifrə boş ola bilməz")
    @Size(min = 8, max = 20, message = "Şifrə 8-20 simvol aralığında olmalıdır")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Şifrədə ən azı bir rəqəm, kiçik/böyük hərf və xüsusi simvol olmalıdır"
    )
    String newPassword;

    @NotBlank(message = "Şifrə təkrarı boş ola bilməz")
    String confirmPassword;

    @AssertTrue(message = "Şifrələr uyğun deyil")
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
