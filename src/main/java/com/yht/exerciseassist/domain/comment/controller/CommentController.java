package com.yht.exerciseassist.domain.comment.controller;

import com.yht.exerciseassist.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

//    @PostMapping("/comment/write")
//    public ResponseEntity<ResponseResult<String>> writeComment(@RequestPart WriteCommentDto writeCommentDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(writeCommentDto));
//    }
}
