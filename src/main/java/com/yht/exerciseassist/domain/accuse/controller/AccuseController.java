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

    @PostMapping("/accuse/post/save/{postId}")
    public ResponseEntity<ResponseResult<Long>> postAccuse(@PathVariable Long postId, @RequestBody @Valid AccuseReq accuseReq) {

        return ResponseEntity.status(HttpStatus.CREATED).body(accuseService.savePostAccuse(postId, accuseReq));
    }

    @PostMapping("/accuse/comment/save/{commentId}")
    public ResponseEntity<ResponseResult<Long>> commentAccuse(@PathVariable Long commentId, @RequestBody @Valid AccuseReq accuseReq) {

        return ResponseEntity.status(HttpStatus.CREATED).body(accuseService.saveCommentAccuse(commentId, accuseReq));
    }
}
