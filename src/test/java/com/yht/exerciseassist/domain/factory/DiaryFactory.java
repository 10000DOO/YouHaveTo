package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.diary.dto.DiaryDetailDto;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoResDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

public class DiaryFactory {

    public static Diary createTestDiary(Member member) {

        ExerciseInfo exInfo = ExerciseInfo.builder()
                .exerciseName("pushUp")
                .reps(10)
                .exSetCount(10)
                .cardio(true)
                .cardioTime(30)
                .finished(true)
                .bodyPart("TRICEP")
                .build();

        List<ExerciseInfo> exInfoList = new ArrayList<>();
        exInfoList.add(exInfo);

        Diary diary = Diary.builder()
                .member(member)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        return diary;
    }

    public static WriteDiaryDto createTestWriteDiaryDto() {
        ExerciseInfoDto exerciseInfoDto = new ExerciseInfoDto();
        exerciseInfoDto.setExerciseName("pushUp");
        exerciseInfoDto.setReps(10);
        exerciseInfoDto.setCardio(true);
        exerciseInfoDto.setExSetCount(10);
        exerciseInfoDto.setCardioTime(30);
        exerciseInfoDto.setBodyPart("TRICEP");
        exerciseInfoDto.setFinished(true);

        List<ExerciseInfoDto> exerciseInfoDtoList = new ArrayList<>();
        exerciseInfoDtoList.add(exerciseInfoDto);

        WriteDiaryDto writeDiaryDto = new WriteDiaryDto();
        writeDiaryDto.setExerciseInfo(exerciseInfoDtoList);
        writeDiaryDto.setReview("오늘 운동 끝");
        writeDiaryDto.setExerciseDate("2023-01-30");

        return writeDiaryDto;
    }

    public static DiaryDetailDto createTestDiaryDetailDto() {
        List<String> mediaIdList = new ArrayList<>();
        mediaIdList.add("www.amazon-s3.com/tuxCoding.jpg");

        ExerciseInfoResDto exerciseInfoDto = new ExerciseInfoResDto(); //내가 기대한 Dto반환값
        exerciseInfoDto.setExerciseName("pushUp");
        exerciseInfoDto.setReps(10);
        exerciseInfoDto.setCardio(true);
        exerciseInfoDto.setExSetCount(10);
        exerciseInfoDto.setCardioTime(30);
        exerciseInfoDto.setBodyPart("TRICEP");
        exerciseInfoDto.setFinished(true);

        List<ExerciseInfoResDto> exerciseInfoDtoList = new ArrayList<>();
        exerciseInfoDtoList.add(exerciseInfoDto);

        DiaryDetailDto diaryDetailDto = DiaryDetailDto.builder()
                .exerciseDate("2023-01-30")
                .review("열심히 했다 오운완")
                .exerciseInfo(exerciseInfoDtoList)
                .createdAt("2023-02-11 11:11")
                .mediaList(mediaIdList)
                .build();

        diaryDetailDto.setDiaryId(1L);

        return diaryDetailDto;
    }

    public static List<ExerciseInfoResDto> getExerciseInfoDto() {
        ExerciseInfoResDto exerciseInfoDto = new ExerciseInfoResDto(); //내가 기대한 Dto반환값
        exerciseInfoDto.setExerciseName("pushUp");
        exerciseInfoDto.setReps(10);
        exerciseInfoDto.setCardio(true);
        exerciseInfoDto.setExSetCount(10);
        exerciseInfoDto.setCardioTime(30);
        exerciseInfoDto.setBodyPart("TRICEP");
        exerciseInfoDto.setFinished(true);

        List<ExerciseInfoResDto> exerciseInfoDtoList = new ArrayList<>();
        exerciseInfoDtoList.add(exerciseInfoDto);

        return exerciseInfoDtoList;
    }
}
