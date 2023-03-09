package com.yht.exerciseassist.domain.comment.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/write")
    public ResponseEntity<ResponseResult<String>> writeComment(@RequestBody @Valid WriteCommentDto writeCommentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(writeCommentDto));
    }
}
