package org.example.coursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter
@Setter
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

    String profileImage;
}
