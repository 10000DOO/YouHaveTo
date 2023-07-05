package com.yht.exerciseassist.gpt.routine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptResponse {

    private List<Choice> choices;

    @Builder
    public ChatGptResponse(List<Choice> choices) {
        this.choices = choices;
    }
}

