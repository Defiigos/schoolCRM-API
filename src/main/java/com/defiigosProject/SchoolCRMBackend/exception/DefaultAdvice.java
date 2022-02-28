package com.defiigosProject.SchoolCRMBackend.exception;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler({
            BadRequestException.class,
            EnumConstantNotFoundException.class,
            FieldRequiredException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<MessageResponse> handleException(Exception e){
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(e.getMessage()));
    }
}
