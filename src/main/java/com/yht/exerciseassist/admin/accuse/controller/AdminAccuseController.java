package com.yht.exerciseassist.admin.accuse.controller;

import com.yht.exerciseassist.admin.accuse.dto.AccuseListWithSliceDto;
import com.yht.exerciseassist.admin.accuse.service.AdminAccuseService;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminAccuseController {

    private final AdminAccuseService adminAccuseService;

    @GetMapping({"/admin/accuse"})
    public ResponseEntity<ResponseResult<AccuseListWithSliceDto>> getAdminAcccuse(@RequestParam(value = "accuseType", required = false) List<String> accuseType,
                                                                                  @RequestParam(value = "accuseGetType", required = false) List<String> typeList,
                                                                                  Pageable pageable) throws ParseException, IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(adminAccuseService.getAccuse(accuseType, typeList, pageable));
    }
}
