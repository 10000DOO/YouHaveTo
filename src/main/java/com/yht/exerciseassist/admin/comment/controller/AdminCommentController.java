package com.yht.exerciseassist.admin.comment.controller;

import com.yht.exerciseassist.domain.comment.dto.CommentListwithSliceDto;
import com.yht.exerciseassist.domain.comment.service.CommentService;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;
    @PatchMapping("/admin/comment/delete/{commentId}")
    public ResponseEntity<ResponseResult<Long>> deleteAdminComment(@PathVariable Long commentId) throws IllegalAccessException {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId));
    }

    @GetMapping({"/admin/comment"})
    public ResponseEntity<ResponseResult<CommentListwithSliceDto>> getAdminComment(@RequestParam(value = "postId", required = false) Long postId,
                                                                                   @RequestParam(value = "parentId", required = false) Long parentId,
                                                                                   @RequestParam(value = "username", required = false) String username, Pageable pageable) throws ParseException, IllegalAccessException {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(postId, parentId, username, pageable));
    }
}
