package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRegistrationRequest {

    @NotBlank(message = "Ad mütləqdir")
    String name;

    @NotBlank(message = "Soyad mütləqdir")
    String surname;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    String email;

    @NotBlank(message = "FİN kod mütləqdir")
    @Pattern(
            regexp = "^[A-HJ-NP-Z0-9]{7}$",
            message = "FİN kod düzgün deyil"
    )
    String finCode;

    @NotBlank(message = "Tələbə nömrəsi mütləqdir")
    String studentNumber;
}
