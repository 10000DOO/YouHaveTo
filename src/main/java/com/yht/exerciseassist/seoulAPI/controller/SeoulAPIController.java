package com.yht.exerciseassist.seoulAPI.controller;

import com.yht.exerciseassist.seoulAPI.ApiExplorer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SeoulAPIController {

    private final ApiExplorer apiExplorer;

    @GetMapping("/api/exercise/seoul")
    public void getSportsFacility() throws IOException {

        apiExplorer.getSportsFacility();
    }
}
