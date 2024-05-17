package com.pfa.pfabackend.basic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.messages.enums.MessageType;

@Component
public class BasicValiadtion {

    public Message handleValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            // Check if there is already an error for this field
            if (!errors.containsKey(error.getField())) {
                // Store the first error message for this field
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
        return Message.builder()
                .message("Validation errors occurred")
                .type(MessageType.ERROR)
                .messages(errors)
                .build();

    }
}
