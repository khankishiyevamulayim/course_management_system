package org.example.coursemanagementsystem.repository;


import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByFinCode(String finCode);

    Optional<User> findByEmailAndIsActiveTrue(String email);
    long countByIsActiveTrue();
    long countByRole(Role role);
    List<User> findByIsActiveTrue();
    List<User> findByIsActiveFalse();
    List<User> findByRole(Role role);

}
