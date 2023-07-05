package com.yht.exerciseassist.gpt.routine.controller;

import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import com.yht.exerciseassist.gpt.routine.service.RoutineService;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/routines")
    public ResponseEntity<ResponseResult<String>> getRoutines(@RequestParam("purpose")HealthPurpose healthPurpose, @RequestParam("height") int height, @RequestParam("weight") int weight, @RequestParam("divisions") int divisions){

        return ResponseEntity.status(HttpStatus.OK).body(routineService.getRoutineResponse(healthPurpose, height, weight, divisions));
    }
}
