package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpVerificationRequest {

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Düzgün email formatı daxil edin")
    String email;

    @NotBlank(message = "OTP kodu boş ola bilməz")
    @Size(min = 6, max = 6, message = "OTP kodu 6 rəqəm olmalıdır")
    @Pattern(regexp = "\\d{6}", message = "OTP kodu yalnız rəqəmlərdən ibarət olmalıdır")
    String otp;

    @NotBlank(message = "Yeni şifrə boş ola bilməz")
    @Size(min = 8, max = 20, message = "Şifrə 8-20 simvol aralığında olmalıdır")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()_\\-]).*$",
            message = "Şifrədə ən azı bir rəqəm, kiçik/böyük hərf və xüsusi simvol olmalıdır"
    )
    String newPassword;
}
