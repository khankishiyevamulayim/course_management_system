package org.example.coursemanagementsystem.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationRequest {

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Düzgün email formatı daxil edin")
    private String email;

    @NotBlank(message = "OTP kodu boş ola bilməz")
    @Size(min = 6, max = 6, message = "OTP kodu 6 rəqəm olmalıdır")
    @Pattern(regexp = "\\d{6}", message = "OTP kodu yalnız rəqəmlərdən ibarət olmalıdır")
    private String otp;

    @NotBlank(message = "Yeni şifrə boş ola bilməz")
    @Size(min = 8, max = 20, message = "Şifrə 8-20 simvol aralığında olmalıdır")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()_\\-]).*$",
            message = "Şifrədə ən azı bir rəqəm, bir kiçik, bir böyük hərf və bir xüsusi simvol (@ # $ % ^ & + = ! * ( ) _ -) olmalıdır"
    )
    private String newPassword;
}