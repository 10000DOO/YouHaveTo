package com.yht.exerciseassist.domain.diary.controller;

import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/diary")
    public ResponseEntity diaryList(@RequestParam("date")
                                    @Pattern(regexp = "(19|20)\\d{2}-((11|12)|(0?(\\d)))",
                                            message = "YYYY-MM 형식과 일치해야 합니다.") String date) {
        return ResponseEntity.status(HttpStatus.OK).body(diaryService.getDiaryList(date));
    }

    @PostMapping("/diary/write")
    public ResponseEntity writeDiary(@RequestPart @Valid WriteDiaryDto writeDiaryDto,
                                     @RequestPart(required = false) List<MultipartFile> files) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.saveDiary(writeDiaryDto, files));
    }

    @GetMapping("/diary/detail")
    public ResponseEntity diaryDetail(@RequestParam("date")
                                      @Pattern(regexp = "(19|20)\\d{2}-((11|12)|(0?(\\d)))-(30|31|((0|1|2)?\\d))",
                                              message = "YYYY-MM-DD 형식과 일치해야 합니다.") String date) {

        return ResponseEntity.status(HttpStatus.OK).body(diaryService.getdiaryDetail(date));
    }

}
