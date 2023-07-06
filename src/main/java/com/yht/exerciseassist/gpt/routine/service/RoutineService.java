package com.yht.exerciseassist.gpt.routine.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptRequest;
import com.yht.exerciseassist.gpt.routine.dto.ChatGptResponse;
import com.yht.exerciseassist.gpt.routine.dto.DietReq;
import com.yht.exerciseassist.gpt.routine.dto.RoutineReq;
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

    public ResponseResult<String> getRoutineResponse(RoutineReq routineReq) {

        String prompt = "Now answer me, assuming you're a body bill trainer, " +
                "I'm doing weight training for " + routineReq.getHealthPurpose().getMessage() +
                " I'm a beginner in weight training. Please make an exercise routine with "
                + routineReq.getDivisions() + " divisions referring to height  " + routineReq.getHeight() + " weight " + routineReq.getWeight() + ". " +
                "I'll tell you the answer format\n\nDay 1:\n\nExercise Name - Settings - Number of times - weight in kg\n" +
                "Exercise Name - Settings - Number of times - weight in kg\nExercise Name - Settings - Number of times - weight in kg\n" +
                "Day 2:\n\nExercise Name - Settings - Number of times - weight in kg\nExercise Name - Settings - Number of times - weight in kg" +
                "\nExercise Name - Settings - Number of times - weight in kg\n.\n.\n.\nPlease answer in the following format";

        return new ResponseResult<>(HttpStatus.OK.value(), requestGPT(prompt));
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
                "Total: Carbohydrates: ?? Protein: ?? Provincial: ?? Please answer in one line in this format\n" +
                "When you have to eat a total of " + recommendedProtein + "grams of protein a day, you have to eat how many grams less than the total protein you currently " +
                "consume and how much more protein you have now. Please answer in one line in this format\n" +
                "Lastly, please recommend what food I should eat based on 300g to eat more protein Please list the names of the foods";

        return new ResponseResult<>(HttpStatus.OK.value(), "각 음식 300g 기준 " + requestGPT(prompt));
    }

    private String requestGPT(String prompt){

        ChatGptRequest request = new ChatGptRequest(model, prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + gptKey);
        ChatGptResponse chatGptResponse = restTemplate.postForObject(url, new HttpEntity<>(request, headers), ChatGptResponse.class);

        TranslateOptions translateOptions = TranslateOptions.newBuilder().setApiKey(googleKey).build();
        System.out.println(chatGptResponse.getChoices().get(0).getMessage().getContent());
        Translate translate = translateOptions.getService();
        Translation translation = translate.translate(chatGptResponse.getChoices().get(0).getMessage().getContent(),Translate.TranslateOption.targetLanguage("ko"));

        return translation.getTranslatedText();
    }
}
