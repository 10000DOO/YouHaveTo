package com.yht.exerciseassist.domain.accuse;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accuse_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccuseType accuseType;

    @Enumerated(EnumType.STRING)
    private AccuseGetType accuseGetType;

    @Size(max = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @Builder
    public Accuse(AccuseType accuseType, AccuseGetType accuseGetType, String content, Post post, Comment comment, DateTime dateTime) {
        this.accuseType = accuseType;
        this.accuseGetType = accuseGetType;
        this.content = content;
        this.post = post;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public void updateAccuseGetTypeDone(AccuseGetType accuseGetType) {
        this.accuseGetType = accuseGetType;
    }
}
