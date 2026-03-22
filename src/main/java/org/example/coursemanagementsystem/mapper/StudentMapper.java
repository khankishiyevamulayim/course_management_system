package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.request.StudentRegistrationRequest;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentNumber", ignore = true)
    @Mapping(target = "profileImage", source = "photoUrl")
    Student toEntity(StudentRegistrationRequest request);

    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "enrolledCourse", ignore = true)
    @Mapping(target = "gpa", ignore = true)
    StudentProfileResponse toProfileResponse(Student student);
}