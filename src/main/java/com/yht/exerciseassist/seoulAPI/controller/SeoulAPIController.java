package com.yht.exerciseassist.seoulAPI.controller;

import com.yht.exerciseassist.seoulAPI.ApiExplorer;
import com.yht.exerciseassist.seoulAPI.District;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SeoulAPIController {

    private final ApiExplorer apiExplorer;

    @GetMapping("/api/exercise/seoul")
    public ResponseEntity<ResponseResult<JSONObject>> getSportsFacility(@RequestParam("district") District district, @RequestParam("offset") int offset,
                                                                        @RequestParam("limit") int limit) throws IOException, ParseException {

        return ResponseEntity.status(HttpStatus.OK).body(apiExplorer.getSportsFacility(district, offset, limit));
    }
}
