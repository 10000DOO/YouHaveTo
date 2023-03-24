package com.yht.exerciseassist.domain.accuse.controller;

import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.accuse.service.AccuseService;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccuseController {

    private final AccuseService accuseService;

    @PostMapping("/accuse/save/{postId}")
    public ResponseEntity<ResponseResult<Long>> accuse(@PathVariable Long postId, @Valid @RequestBody AccuseReq accuseReq) {

        return ResponseEntity.status(HttpStatus.CREATED).body(accuseService.saveAccuse(postId, accuseReq));
    }

    @PatchMapping("/accuse/delete/{accuseId}")
    public ResponseEntity<ResponseResult<Long>> deleteAccuse(@PathVariable Long accuseId) {

        return ResponseEntity.status(HttpStatus.OK).body(accuseService.deleteAccuse(accuseId));
    }
}
