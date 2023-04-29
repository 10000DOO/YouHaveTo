package com.yht.exerciseassist.domain.accuse;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AccuseType {

    POLITICAL_CONTENT("정치적 내용 포함"),
    ABUSIVE_LANGUAGE_BELITTLE("욕설 및 비하"),
    COMMERCIAL_ADVERTISING("상업적 광고 및 판매"),
    INADEQUATE_CONTENT("부적합한 내용"),
    PORNOGRAPHY_CONTENT("음란물 및 불건전 컨텐츠");

    private final String message;

    AccuseType(String message) {
        this.message = message;
    }

    @JsonCreator
    public static AccuseType parsing(String inputValue) {
        return Stream.of(AccuseType.values())
                .filter(accuseType -> accuseType.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
