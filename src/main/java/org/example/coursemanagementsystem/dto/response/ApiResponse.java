package org.example.coursemanagementsystem.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static org.example.coursemanagementsystem.util.Constants.ERROR_MESSAGE;
import static org.example.coursemanagementsystem.util.Constants.SUCCESS_MESSAGE;

@Builder
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    String message;
    T data;
    Boolean success;

    public static <T>ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .message(SUCCESS_MESSAGE)
                .data(data)
                .success(true)
                .build();
    }
    public static <T>ApiResponse<T> error(T data) {
        return ApiResponse.<T>builder()
                .message(ERROR_MESSAGE)
                .data(data)
                .success(false)
                .build();
    }
}
