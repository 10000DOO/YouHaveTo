package com.yht.exerciseassist.domain.comment;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member commentWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Setter
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    private String commentContent;

    @Embedded
    private DateTime dateTime;

    @Builder
    public Comment(Post post, Member commentWriter, String commentContent, DateTime dateTime) {
        this.post = post;
        this.commentWriter = commentWriter;
        this.commentContent = commentContent;
        this.dateTime = dateTime;
    }

    public void connectChildParent(Comment parent) {
        this.parent = parent;
        parent.child.add(this);
    }
}
