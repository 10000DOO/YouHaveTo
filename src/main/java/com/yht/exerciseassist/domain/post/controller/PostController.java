package com.yht.exerciseassist.domain.post.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.post.dto.PostDetailRes;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.service.PostService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping("/post/write")
    public ResponseEntity<ResponseResult<String>> writePost(@RequestPart @Valid WritePostDto writePostDto,
                                                            @RequestPart(required = false) List<MultipartFile> files) throws IOException, java.io.IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(writePostDto, files));
    }

    @GetMapping("/post/detail/{postId}")
    public ResponseEntity<ResponseResult<PostDetailRes>> postDetail(@PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostDetail(postId));
    }
}
