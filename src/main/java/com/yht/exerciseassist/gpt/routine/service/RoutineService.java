package com.yht.exerciseassist.gpt.routine.service;

import com.yht.exerciseassist.configuration.GptConfig;
import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptRequestDto;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptResponseDto;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoutineService {

    @Value("${chatgpt.api-key}")
    private String key;
    private static RestTemplate restTemplate = new RestTemplate();

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(GptConfig.MEDIA_TYPE));
        headers.add(GptConfig.AUTHORIZATION, GptConfig.BEARER + key);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                GptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    public ResponseResult<ChatGptResponseDto> getRoutineResponse(HealthPurpose healthPurpose, int height, int weight, int divisions) {

        String prompt = "Now answer me, assuming you're a body bill trainer, " +
                "I'm doing weight training for " + healthPurpose.getMessage() +
                " I'm a beginner in weight training. Please make an exercise routine with "
                + divisions + " divisions referring to height  " + height + " weight " + weight + ". " +
                "I'll tell you the answer format\n\nDay 1:\n\nExercise Name - Settings - Number of times\n" +
                "Exercise Name - Settings - Number of times\nExercise Name - Settings - Number of times\n" +
                "Day 2:\n\nExercise Name - Settings - Number of times\nExercise Name - Settings - Number of times" +
                "\nExercise Name - Settings - Number of times\n.\n.\n.\nPlease answer in the following format";

        return new ResponseResult<>(HttpStatus.OK.value(), this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                GptConfig.MODEL,
                                prompt,
                                GptConfig.MAX_TOKEN,
                                GptConfig.TEMPERATURE,
                                GptConfig.TOP_P
                        )
                )
        ));
    }
}
