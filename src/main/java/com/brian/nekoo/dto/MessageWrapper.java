package com.brian.nekoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageWrapper<T> {
    private boolean isSuccess;
    private T data;
    private String error;

    public static <T> MessageWrapper<T> of(T data) {
        return MessageWrapper.<T>builder()
            .data(data)
            .isSuccess(data != null)
            .build();
    }
    
    public static <T> ResponseEntity<Object> toResponseEntityOk(T data) {
        return ResponseEntity.ok(
            of(data)
        );
    }
}
