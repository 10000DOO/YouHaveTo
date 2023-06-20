package com.yht.exerciseassist.domain.post.controller;

import com.yht.exerciseassist.domain.post.dto.PostDetailRes;
import com.yht.exerciseassist.domain.post.dto.PostEditList;
import com.yht.exerciseassist.domain.post.dto.PostListWithSliceDto;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.service.PostService;
import com.yht.exerciseassist.util.ResponseResult;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping("/post/write")
    public ResponseEntity<ResponseResult<String>> writePost(@RequestPart("writePostDto") @Valid WritePostDto writePostDto,
                                                            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException, java.io.IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(writePostDto, files));
    }

    @GetMapping("/post/detail/{postId}")
    public ResponseEntity<ResponseResult<PostDetailRes>> postDetail(@PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostDetail(postId));
    }

    @GetMapping("/post/edit/{postId}")
    public ResponseEntity<ResponseResult<PostEditList>> getEditData(@PathVariable Long postId) throws IllegalAccessException {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostEditData(postId));
    }

    @PatchMapping("/post/edit/{postId}")
    public ResponseEntity<ResponseResult<String>> editPost(@RequestPart @Valid WritePostDto writePostDto,
                                                           @RequestPart(required = false) List<MultipartFile> files,
                                                           @PathVariable Long postId) throws java.io.IOException {

        return ResponseEntity.status(HttpStatus.OK).body(postService.editPost(writePostDto, files, postId));
    }

    @PatchMapping("/post/delete/{postId}")
    public ResponseEntity<ResponseResult<Long>> deletePost(@PathVariable Long postId) throws java.io.IOException, IllegalAccessException {

        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }

    @PatchMapping("/post/like")
    public ResponseEntity<ResponseResult<Long>> plusLikeCount(@RequestParam("post_id") Long postId, @RequestParam("clicked") boolean clicked) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.updateLike(postId, clicked));
    }

    @GetMapping("/post")
    public ResponseEntity<ResponseResult<PostListWithSliceDto>> getPostList(@RequestParam(value = "postType", required = false) List<String> postType,
                                                                            @RequestParam(value = "woryOutCategory", required = false) List<String> workOutCategories,
                                                                            @RequestParam(value = "username", required = false) String username, Pageable pageable) throws ParseException {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList(postType, workOutCategories, username, pageable));
    }
}
