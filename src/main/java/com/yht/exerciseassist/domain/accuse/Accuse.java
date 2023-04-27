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
    public Accuse(AccuseType accuseType, String content, Post post, DateTime dateTime) {
        this.accuseType = accuseType;
        this.content = content;
        this.post = post;
        this.dateTime = dateTime;
    }
}
