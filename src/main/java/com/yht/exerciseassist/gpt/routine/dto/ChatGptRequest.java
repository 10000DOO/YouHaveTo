package com.yht.exerciseassist.gpt.routine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();

    public ChatGptRequest(String model, String prompt) {
        this.model = model;
        this.messages.add(new Message("user", prompt));
    }
}
