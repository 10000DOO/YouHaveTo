package com.yht.exerciseassist.domain.diary.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

//    @GetMapping("/diary")
//    public ResponseResult diaryList(@RequestParam @Pattern(regexp = "/^\\d{4}-(0[1-9]|1[012])$/") String date) {
//        return diaryService.getDiaryList(date);
//    }

    @PostMapping("/diary/write")
    public ResponseResult writeDiary(@RequestBody @Valid WriteDiaryDto writeDiaryDto) {
        return diaryService.saveDiary(writeDiaryDto);
    }
}
