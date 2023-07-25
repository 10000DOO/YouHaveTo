package com.yht.exerciseassist.gpt.routine.controller;

import com.yht.exerciseassist.gpt.routine.dto.DietReq;
import com.yht.exerciseassist.gpt.routine.dto.PostureReq;
import com.yht.exerciseassist.gpt.routine.dto.RoutineReq;
import com.yht.exerciseassist.gpt.routine.service.RoutineService;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/routines")
    public ResponseEntity<ResponseResult<String>> getRoutines(@RequestBody @Valid RoutineReq routineReq){

        return ResponseEntity.status(HttpStatus.OK).body(routineService.getRoutineResponse(routineReq));
    }

    @PostMapping("/diet")
    public ResponseEntity<ResponseResult<String>> getRoutines(@RequestBody @Valid DietReq dietReq){

        return ResponseEntity.status(HttpStatus.OK).body(routineService.getDietResponse(dietReq));
    }

    @PostMapping("/posture")
    public ResponseEntity<ResponseResult<String>> confirmPosture(@RequestBody @Valid PostureReq postureReq){

        return ResponseEntity.status(HttpStatus.OK).body(routineService.checkPosture(postureReq));
    }
}
