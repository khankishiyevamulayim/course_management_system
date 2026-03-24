package org.example.coursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "students",
        indexes = @Index(name = "idx_student_number", columnList = "student_number")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    private Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String surname;

    @Column(nullable = false)
    String fatherName;

    @Column(name = "student_number", unique = true)
    String studentNumber;

    String profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId//user id-si nedirse o olur
    @JoinColumn(name = "user_id")
    User user;
}