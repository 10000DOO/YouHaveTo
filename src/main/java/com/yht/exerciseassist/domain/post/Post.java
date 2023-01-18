package com.yht.exerciseassist.domain.post;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member postWriter;

    @OneToMany(mappedBy = "post")
    private List<Media> mediaList = new ArrayList<>();

    private Long views;

    private String likeAndHate;

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}