package com.yht.exerciseassist.domain.comment.controller;

import com.yht.exerciseassist.domain.comment.dto.CommentListwithSliceDto;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.service.CommentService;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/write")
    public ResponseEntity<ResponseResult<String>> writeComment(@RequestBody @Valid WriteCommentDto writeCommentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(writeCommentDto));
    }

    @PatchMapping("/comment/delete/{commentId}")
    public ResponseEntity<ResponseResult<Long>> deleteComment(@PathVariable Long commentId) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId));
    }

    @GetMapping({"/comment"})
    public ResponseEntity<ResponseResult<CommentListwithSliceDto>> getChildComment(@RequestParam(value = "postId", required = false) Long postId,
                                                                                   @RequestParam(value = "parentId", required = false) Long parentId,
                                                                                   @RequestParam(value = "username", required = false) String username, Pageable pageable) throws ParseException, IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(postId, parentId, username, pageable));
    }
}
