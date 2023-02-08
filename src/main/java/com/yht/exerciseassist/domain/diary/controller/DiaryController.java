package com.yht.exerciseassist.domain.diary.controller;

import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

//    @GetMapping("/diary")
//    public ResponseResult diaryList(@RequestParam @Pattern(regexp = "/^\\d{4}-(0[1-9]|1[012])$/") String date) {
//        return diaryService.getDiaryList(date);
//    }

    @PostMapping("/diary/write")
    public ResponseEntity writeDiary(@RequestPart @Valid WriteDiaryDto writeDiaryDto,
                                     @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        return diaryService.saveDiary(writeDiaryDto, files);
    }
}
