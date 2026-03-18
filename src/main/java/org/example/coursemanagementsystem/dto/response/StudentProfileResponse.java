package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponse {
    Long id;
    String name;
    String surname;
    String email;
    String studentNumber;
    String profileImage;
}