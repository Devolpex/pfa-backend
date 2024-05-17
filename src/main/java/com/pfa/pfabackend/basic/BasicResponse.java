package com.pfa.pfabackend.basic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.pfa.pfabackend.messages.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicResponse {
    private Message message;
    private Object data;
    private String redirectTo;
    private HttpStatus status;

}
