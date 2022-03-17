package com.defiigosProject.SchoolCRMBackend.exception.extend;

import java.util.Arrays;

public class BadEnumException extends Exception {
    public BadEnumException(Class<? extends Enum<?>> enumType, String constantName) {
        super("'" + constantName + "' is incorrect, try one on this: " + Arrays.toString(enumType.getEnumConstants()) + "!");
    }
}
