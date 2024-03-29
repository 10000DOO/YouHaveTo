package com.yht.exerciseassist.domain.post;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.likeCount.LikeCount;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member postWriter;

    @OneToMany(mappedBy = "post")
    private List<Media> mediaList = new ArrayList<>();

    private Long views;

    @OneToMany(mappedBy = "post")
    private List<LikeCount> likeCount = new ArrayList<>();

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content, Member postWriter, Long views, DateTime dateTime, PostType postType, WorkOutCategory workOutCategory) {
        this.title = title;
        this.content = content;
        this.postWriter = postWriter;
        this.views = views;
        this.dateTime = dateTime;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
    }

    public void linkToMedia(List<Media> mediaList) {
        this.mediaList = mediaList;
        for (Media media : mediaList) {
            media.linkToPost(this);
        }
    }

    public void updateLikeCount(LikeCount likeCount) {
        this.likeCount.remove(likeCount);
        this.likeCount.add(likeCount);
    }

    public void editPost(String title, String content, PostType postType, WorkOutCategory workOutCategory) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
    }

    public void pulsViews() {
        views += 1;
    }

    public void setPostIdUsedOnlyTest(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Post that = (Post) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
