package com.yht.exerciseassist.domain.media;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    private String originalFilename;

    private String filename;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @Builder
    public Media(String originalFilename, String filename, String filePath, DateTime dateTime) {
        this.originalFilename = originalFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.dateTime = dateTime;
    }

    public void setMediaIdUsedOnlyTest(Long id) {
        this.id = id;
    }

    public void linkToDiary(Diary diary) {
        this.diary = diary;
    }
}
