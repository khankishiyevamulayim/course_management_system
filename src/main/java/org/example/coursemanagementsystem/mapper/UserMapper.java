package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserBaseResponse toBaseResponse(User user);
    List<UserBaseResponse> toBaseResponseList(List<User> users);
}