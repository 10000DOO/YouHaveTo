package com.yht.exerciseassist.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateTime {

    private String createdAt;
    private String canceledAt;
    private String updatedAt;

    public DateTime(String createdAt, String updatedAt, String canceledAt) {
        this.createdAt = createdAt;
        this.canceledAt = canceledAt;
        this.updatedAt = updatedAt;
    }

    public void canceledAtUpdate() {
        this.canceledAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void updatedAtUpdate() {
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
