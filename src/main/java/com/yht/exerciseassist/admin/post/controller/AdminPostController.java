package com.yht.exerciseassist.admin.post.controller;

import com.yht.exerciseassist.domain.post.dto.PostDetailRes;
import com.yht.exerciseassist.domain.post.dto.PostListWithSliceDto;
import com.yht.exerciseassist.domain.post.service.PostService;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminPostController {

    private final PostService postService;

    @GetMapping("/admin/post")
    public ResponseEntity<ResponseResult<PostListWithSliceDto>> getAdminPostList(@RequestParam(value = "postType", required = false) List<String> postType,
                                                                                 @RequestParam(value = "woryOutCategory", required = false) List<String> workOutCategories,
                                                                                 @RequestParam(value = "username", required = false) String username, Pageable pageable) throws ParseException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList(postType, workOutCategories, username, pageable));
    }

    @GetMapping("/admin/post/detail/{postId}")
    public ResponseEntity<ResponseResult<PostDetailRes>> adminPostDetail(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostDetail(postId));
    }

    @PatchMapping("/admin/post/delete/{postId}")
    public ResponseEntity<ResponseResult<Long>> deletePost(@PathVariable Long postId) throws java.io.IOException, IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }
}
