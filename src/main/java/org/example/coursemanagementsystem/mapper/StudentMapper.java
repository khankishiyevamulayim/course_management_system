package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.request.StudentRegistrationRequest;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentRegistrationRequest request);

    @Mapping(source = "user.email", target = "email")
    StudentProfileResponse toProfileResponse(Student student);
}