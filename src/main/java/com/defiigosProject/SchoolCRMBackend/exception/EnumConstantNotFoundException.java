package com.defiigosProject.SchoolCRMBackend.exception;

import java.util.Arrays;

public class EnumConstantNotFoundException extends Exception {
    public EnumConstantNotFoundException(Class<? extends Enum> enumType, String constantName) {
        super("'" + constantName + "' is incorrect, try one on this: " + Arrays.toString(enumType.getEnumConstants()) + "!");
    }
}
