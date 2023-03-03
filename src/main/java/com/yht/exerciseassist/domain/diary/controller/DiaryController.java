package com.yht.exerciseassist.domain.diary.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.diary.dto.DiaryDetailDto;
import com.yht.exerciseassist.domain.diary.dto.DiaryEditData;
import com.yht.exerciseassist.domain.diary.dto.DiaryListDto;
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
    public ResponseEntity<ResponseResult<DiaryListDto>> diaryList(@RequestParam("date")
                                                                  @Pattern(regexp = "(19|20)\\d{2}-(0[1-9]|1[012])",
                                                                          message = "YYYY-MM 형식과 일치해야 합니다.") String date) {
        return ResponseEntity.status(HttpStatus.OK).body(diaryService.getDiaryList(date));
    }

    @PostMapping("/diary/write")
    public ResponseEntity<ResponseResult<String>> writeDiary(@RequestPart @Valid WriteDiaryDto writeDiaryDto,
                                                             @RequestPart(required = false) List<MultipartFile> files) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.saveDiary(writeDiaryDto, files));
    }

    @GetMapping("/diary/detail")
    public ResponseEntity<ResponseResult<DiaryDetailDto>> diaryDetail(@RequestParam("date")
                                                                      @Pattern(regexp = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])",
                                                                              message = "YYYY-MM-DD 형식과 일치해야 합니다.") String date) {

        return ResponseEntity.status(HttpStatus.OK).body(diaryService.getdiaryDetail(date));
    }

    @GetMapping("/diary/edit/{diaryId}")
    public ResponseEntity<ResponseResult<DiaryEditData>> getEditData(@PathVariable Long diaryId) {

        return ResponseEntity.status(HttpStatus.OK).body(diaryService.getDiaryEditData(diaryId));
    }

    @PatchMapping("/diary/edit/{diaryId}")
    public ResponseEntity<ResponseResult<String>> editDiary(@RequestPart @Valid WriteDiaryDto writeDiaryDto,
                                                            @RequestPart(required = false) List<MultipartFile> files,
                                                            @PathVariable Long diaryId) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(diaryService.editDiary(writeDiaryDto, files, diaryId));
    }

    @PatchMapping("diary/delete/{diaryId}")
    public ResponseEntity<ResponseResult<Long>> deleteDiary(@PathVariable Long diaryId) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(diaryService.deleteDiary(diaryId));
    }
}
