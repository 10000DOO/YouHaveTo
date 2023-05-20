package com.yht.exerciseassist.admin.accuse.service;

import com.yht.exerciseassist.admin.accuse.dto.AccuseListDto;
import com.yht.exerciseassist.admin.accuse.dto.AccuseListWithSliceDto;
import com.yht.exerciseassist.admin.accuse.repository.AdminAccuseRepository;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.accuse.AccuseGetType;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.util.TimeConvertUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminAccuseService {
    private final AdminAccuseRepository adminAccuseRepository;

    public ResponseResult<AccuseListWithSliceDto> getAccuse(List<String> accuseType, List<String> typeList, Pageable pageable) throws IllegalAccessException, ParseException {
        if (SecurityUtil.getMemberRole().equals("ADMIN")) {
            Slice<Accuse> accuseList = adminAccuseRepository.accuseAsAccuseType(accuseType, typeList, pageable);
            List<Accuse> accuses = accuseList.getContent();
            boolean hasNext = accuseList.hasNext();
            boolean isFirst = accuseList.isFirst();

            List<AccuseListDto> accuseListDtos = new ArrayList<>();

            for (Accuse accuse : accuses) {
                SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = accuse.getDateTime().getCreatedAt();
                Date date;
                try {
                    date = formatterDateTime.parse(dateString);
                } catch (ParseException e) {
                    throw new ParseException(ErrorCode.DATE_FORMAT_EXCEPTION.getMessage(), e.getErrorOffset());
                }

                String calculateTime = TimeConvertUtil.calculateTime(date);

                if (calculateTime == null) {
                    String[] splitString = accuse.getDateTime().getCreatedAt().split(" ");
                    calculateTime = splitString[0];
                }

                if (accuse.getPost() != null && accuse.getComment() == null) {
                    AccuseListDto accuseListDto = AccuseListDto.builder()
                            .accuseType(accuse.getAccuseType().getMessage())
                            .accuseGetType(accuse.getAccuseGetType().getMessage())
                            .content(accuse.getContent())
                            .postId(accuse.getPost().getId())
                            .createdAt(calculateTime)
                            .build();

                            accuseListDtos.add(accuseListDto);
                        } else if (accuse.getPost() == null && accuse.getComment() != null) {
                            AccuseListDto accuseListDto = AccuseListDto.builder()
                                    .accuseType(accuse.getAccuseType().getMessage())
                                    .accuseGetType(accuse.getAccuseGetType().getMessage())
                                    .content(accuse.getContent())
                                    .commentId(accuse.getComment().getId())
                                    .createdAt(calculateTime)
                                    .build();

                            accuseListDtos.add(accuseListDto);
                        }
                        else {
                            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_ACCESS.getMessage());
                        }
                    }

            AccuseListWithSliceDto accuseListWithSliceDto = AccuseListWithSliceDto.builder()
                    .accuseListDto(accuseListDtos)
                    .hasNext(hasNext)
                    .isFirst(isFirst)
                    .build();

            return new ResponseResult<>(HttpStatus.OK.value(), accuseListWithSliceDto);
        } else {
            throw new IllegalAccessException(ErrorCode.ACCESS_DENIED.getMessage());
        }
    }

    public ResponseResult<Long> deleteAccuse(Long accuseId) throws IllegalAccessException {
        if (SecurityUtil.getMemberRole().equals("ADMIN")) {

            Accuse findAccuse = adminAccuseRepository.findByNotDeletedId(accuseId)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_ACCUSE.getMessage()));

            findAccuse.getDateTime().canceledAtUpdate();
            findAccuse.updateAccuseGetTypeDone(AccuseGetType.DONE);

            log.info("{}유저 {} 신고 삭제 완료", SecurityUtil.getCurrentUsername(), findAccuse.getId());
            return new ResponseResult<>(HttpStatus.OK.value(), findAccuse.getId());
        }
        else {
            throw new IllegalAccessException(ErrorCode.ACCESS_DENIED.getMessage());
        }
    }
}
