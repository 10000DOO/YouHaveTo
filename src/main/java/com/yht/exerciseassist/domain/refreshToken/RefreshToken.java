package com.yht.exerciseassist.domain.refreshToken;

import com.yht.exerciseassist.domain.DateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    private String refreshToken;

    private DateTime dateTime;


    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.dateTime = new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.dateTime.updatedAtUpdate();
    }
}
