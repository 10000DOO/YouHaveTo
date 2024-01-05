package com.yht.exerciseassist.gpt.routine.service;

import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import com.yht.exerciseassist.gpt.routine.dto.*;
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

    private static RestTemplate restTemplate = new RestTemplate();

    public ResponseResult<String> getRoutineResponse(RoutineReq routineReq) {

        String prompt = "Now answer me, assuming you're a body bill trainer, " +
                "I'm doing weight training for " + routineReq.getHealthPurpose().getMessage() +
                " I'm a beginner in weight training. Please make an exercise routine with "
                + routineReq.getDivisions() + " divisions referring to height  " + routineReq.getHeight() + " weight " + routineReq.getWeight() + ". " +
                "I'll tell you the answer format\n\nDay 1:\n\nExercise Name - Settings - Number of times - weight in kg\n" +
                "Exercise Name - Settings - Number of times - weight in kg\nExercise Name - Settings - Number of times - weight in kg\n" +
                "Day 2:\n\nExercise Name - Settings - Number of times - weight in kg\nExercise Name - Settings - Number of times - weight in kg" +
                "\nExercise Name - Settings - Number of times - weight in kg\n.\n.\n.\nPlease answer in the following format" +
                "Please answer in Korean";

        String answer = requestGPT(prompt) + " 결과는 openAI의 gpt모델에 의해 계산된 것으로 참고의 목적으로만 사용하길 권장합니다.";

        return new ResponseResult<>(HttpStatus.OK.value(), answer);
    }

    public ResponseResult<String> getDietResponse(DietReq dietReq) {

        String food = String.join(",", dietReq.getFood());
        double recommendedProtein = 0;
        if (dietReq.getHealthPurpose() == HealthPurpose.MUSCLE_GAIN){
            recommendedProtein = dietReq.getWeight() * 1.7;
        } else {
            recommendedProtein = dietReq.getWeight() * 1.2;
        }

        String prompt = "You're now a bodybuilding trainer and please be polite I am doing weight training for the purpose of " +
                dietReq.getHealthPurpose().getMessage() + " I ate " + food +
                " If I ate 300g each how many grams of carbohydrates, protein, and fat did I eat today? Don't talk about anything else," +
                " just let me know the results of what I asked, even if it's not an exact number, let me know as an estimate, " +
                "Total: Carbohydrates: (sum of carbohydrates estimates, e.g., 10-20)grams Protein: (sum of protein estimates, e.g., 10-20)grams " +
                "Fat: (sum of fat estimates, e.g., 10-20)grams Please answer in one line in this format\n" +
                "When you have to eat a total of " + recommendedProtein + "grams of protein a day, you have to eat how many grams less than the total protein you currently " +
                "consume and how much more protein you have now. Please answer in one line in this format\n" +
                "Lastly, please recommend what food I should eat based on 300g to eat more protein Please list the names of the foods" +
                "Please answer in Korean";

        String answer = requestGPT(prompt) + " 결과는 openAI의 gpt모델에 의해 계산된 것으로 참고의 목적으로만 사용하길 권장합니다.";

        return new ResponseResult<>(HttpStatus.OK.value(), "각 음식 300g 기준 " + answer);
    }

    public ResponseResult<String> checkPosture(PostureReq postureReq) {

        String prompt = "You are now a bodybuilding trainer. I did " + postureReq.getExerciseName() +
                " and felt a pump in my " + postureReq.getMuscleName() + ". Please let me know which muscle should be " +
                "pumped among the " + postureReq.getMuscleName() + " if I did the " + postureReq.getExerciseName() +
                " with the correct posture. Also, let me know which muscle should not be pumped among the " + postureReq.getMuscleName() +
                " if I did the exercise with the correct posture. Lastly, if there is any incorrect muscle pumped, tell me why it was pumped and how to correct it. " +
                "All answers should be determined between the " + postureReq.getMuscleName() + "." +
                "And tell me that out of the " + postureReq.getMuscleName() + " muscles, only the muscles that are usually pumped through "
                + postureReq.getExerciseName() + " are correct." +
                " I'll give you the answer format. Normal Stimulation Sites 1. ~~~ 2. ~~~ wrong stimulation site 1. ~~~ 2. ~~~ wrong reason 1. ~~~ 2. ~~~ " +
                "Fix method 1. ~~~ 2. ~~~. Please don't say anything other than the format I specified. if no content just response (no content) do not response ~~~" +
                "Please answer in Korean";

        String answer = requestGPT(prompt) + " 결과는 openAI의 gpt모델에 의해 계산된 것으로 참고의 목적으로만 사용하길 권장합니다.";

        return new ResponseResult<>(HttpStatus.OK.value(), answer);
    }

    public String requestGPT(String prompt) {

        ChatGptRequest request = new ChatGptRequest(model, prompt);
        System.out.println(prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + gptKey);
        ChatGptResponse chatGptResponse = restTemplate.postForObject(url, new HttpEntity<>(request, headers), ChatGptResponse.class);

//        TranslateOptions translateOptions = TranslateOptions.newBuilder().setApiKey(googleKey).build();
//        System.out.println(chatGptResponse.getChoices().get(0).getMessage().getContent());
//        Translate translate = translateOptions.getService();
//        Translation translation = translate.translate(chatGptResponse.getChoices().get(0).getMessage().getContent(),Translate.TranslateOption.targetLanguage("ko"));

        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
