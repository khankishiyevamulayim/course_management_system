package org.example.coursemanagementsystem.dto.kafka;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordEvent {
    private Long userId;
    private String email;
    @ToString.Exclude
    private String otp;
}
