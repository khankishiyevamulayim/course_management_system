package org.example.coursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "teachers")
@Data
public class Teacher {
    @Id
    Long id;

    String name;
    String surname;
    String specialization;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    User user;
}
