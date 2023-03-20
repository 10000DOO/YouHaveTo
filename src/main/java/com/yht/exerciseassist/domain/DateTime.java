package com.yht.exerciseassist.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateTime dateTime)) return false;
        return Objects.equals(getCreatedAt(), dateTime.getCreatedAt()) && Objects.equals(getCanceledAt(), dateTime.getCanceledAt()) && getUpdatedAt().equals(dateTime.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreatedAt(), getCanceledAt(), getUpdatedAt());
    }
}
