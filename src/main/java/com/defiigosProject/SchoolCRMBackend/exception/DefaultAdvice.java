package com.defiigosProject.SchoolCRMBackend.exception;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler({
            BadRequestException.class,
            BadEnumException.class,
            FieldRequiredException.class,
            FieldNotNullException.class,
            EntityNotFoundException.class,
            EntityAlreadyExistException.class,
            EntityUsedException.class
    })
    public ResponseEntity<MessageResponse> handleException(Exception e){
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(e.getMessage()));
    }
}
