package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.Email;
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
public class TeacherRegistrationRequest {

    @NotBlank(message = "Ad mütləqdir")
    String name;

    @NotBlank(message = "Soyad mütləqdir")
    String surname;

    @NotBlank(message = "Ata adı mütləqdir")
    String fatherName;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    String email;

    @NotBlank(message = "FİN kod mütləqdir")
    @Pattern(regexp = "^[A-HJ-NP-Z0-9]{7}$", message = "FİN 7 simvol və düzgün formatda olmalıdır")
    String finCode;

    @NotBlank(message = "İxtisas mütləqdir")
    String specialization;

    @Size(max = 500, message = "Foto URL 500 simvoldan çox ola bilməz")
    String photoUrl;
}