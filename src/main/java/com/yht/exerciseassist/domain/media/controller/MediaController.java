package com.yht.exerciseassist.domain.media.controller;

import com.yht.exerciseassist.domain.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/media/{id}")
    public ResponseEntity<FileSystemResource> getMediaFile(@PathVariable Long id) throws IOException {

        return mediaService.getMediaFile(id);
    }
}
