package com.yht.exerciseassist.seoulAPI.controller;

import com.yht.exerciseassist.seoulAPI.ApiExplorer;
import com.yht.exerciseassist.seoulAPI.District;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SeoulAPIController {

    private final ApiExplorer apiExplorer;

    @GetMapping("/api/exercise/seoul")
    public void getSportsFacility(@RequestParam("district") District district, @RequestParam("start") String start, @RequestParam("end") String end) throws IOException {

        apiExplorer.getSportsFacility(district, start, end);
    }
}
