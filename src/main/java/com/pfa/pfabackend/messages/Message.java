package com.pfa.pfabackend.messages;

import java.util.HashMap;
import java.util.Map;

import com.pfa.pfabackend.messages.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String message;
    private MessageType type;
    private Map<String, String> messages;
}
