package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.request.TeacherRegistrationRequest;
import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;
import org.example.coursemanagementsystem.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Teacher toEntity(TeacherRegistrationRequest request);

    @Mapping(source = "user.email", target = "email")
    TeacherProfileResponse toProfileResponse(Teacher teacher);
}