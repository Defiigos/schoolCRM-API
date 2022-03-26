package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class BadEnumException extends HttpRequestException {
    public BadEnumException(Class<? extends Enum<?>> enumType, String constantName) {
        super("'" + constantName + "' is incorrect, try one on this: "
                + Arrays.toString(enumType.getEnumConstants()) + "!",
                HttpStatus.NOT_ACCEPTABLE);
    }
}
