package com.yht.exerciseassist.gpt.routine.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptRequest;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptResponse;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoutineService {

    @Value("${chatgpt.api-key}")
    private String gptKey;
    @Value("${chatgpt.url}")
    private String url;
    @Value("${chatgpt.model}")
    private String model;
    @Value("${google.api-key}")
    private String googleKey;

    private static RestTemplate restTemplate = new RestTemplate();

    public ResponseResult<String> getRoutineResponse(HealthPurpose healthPurpose, int height, int weight, int divisions) {

        String prompt = "Now answer me, assuming you're a body bill trainer, " +
                "I'm doing weight training for " + healthPurpose.getMessage() +
                " I'm a beginner in weight training. Please make an exercise routine with "
                + divisions + " divisions referring to height  " + height + " weight " + weight + ". " +
                "I'll tell you the answer format\n\nDay 1:\n\nExercise Name - Settings - Number of times\n" +
                "Exercise Name - Settings - Number of times\nExercise Name - Settings - Number of times\n" +
                "Day 2:\n\nExercise Name - Settings - Number of times\nExercise Name - Settings - Number of times" +
                "\nExercise Name - Settings - Number of times\n.\n.\n.\nPlease answer in the following format";

        ChatGptRequest request = new ChatGptRequest(model, prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + gptKey);
        ChatGptResponse chatGptResponse = restTemplate.postForObject(url, new HttpEntity<>(request, headers), ChatGptResponse.class);

        TranslateOptions translateOptions = TranslateOptions.newBuilder().setApiKey(googleKey).build();
        Translate translate = translateOptions.getService();
        Translation translation = translate.translate(chatGptResponse.getChoices().get(0).getMessage().getContent(),Translate.TranslateOption.targetLanguage("ko"));

        return new ResponseResult<>(HttpStatus.OK.value(), translation.getTranslatedText());
    }
}
