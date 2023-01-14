package com.yht.exerciseassist.domain.media;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="media_id")
    private Long id;

    private String originalFilename;

    private String filename;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;
}