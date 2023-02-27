package com.yht.exerciseassist.domain.accuse;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Accuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accuse_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccuseType accuseType;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;
}
