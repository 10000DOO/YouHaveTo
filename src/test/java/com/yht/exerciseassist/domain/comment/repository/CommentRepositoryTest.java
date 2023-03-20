package com.yht.exerciseassist.domain.comment.repository;

import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.factory.CommentFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@Rollback
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @MockBean
    private PostRepositoryImpl postRepositoryImpl;
    @Autowired
    private EntityManager em;

    @Test
    public void saveComment() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment comment = CommentFactory.createTestComment(findMember, findPost);

        //when
        Comment saveComment = commentRepository.save(comment);

        //then
        assertThat(saveComment).isEqualTo(comment);
    }

    @Test
    public void findByParentComment() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment comment = CommentFactory.createTestComment(findMember, findPost);

        commentRepository.save(comment);

        //when
        Optional<Comment> findComment = commentRepository.findParentCommentByParentId(comment.getId());

        //then
        assertThat(findComment.get()).isEqualTo(comment);
    }

    @Test
    public void deleteCommentByParentId() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment parent = CommentFactory.createTestComment(findMember, findPost);
        Comment childComment = CommentFactory.createTestComment(findMember, findPost);
        childComment.connectChildParent(parent);
        em.persist(parent);
        em.persist(childComment);
        Comment findComment = em.find(Comment.class, parent.getId());
        //when
        commentRepository.deleteCommentByParentId("2023-03-11", findComment.getId());
        //then
        Comment findComment2 = em.find(Comment.class, childComment.getId());
        assertThat(findComment2.getDateTime().getCanceledAt()).isEqualTo("2023-03-11");
    }

    @Test
    public void deleteCommentByPostId() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment parent = CommentFactory.createTestComment(findMember, findPost);
        em.persist(parent);
        //when
        commentRepository.deleteCommentByPostId("2023-03-11", parent.getPost().getId());
        //then
        Comment findComment = em.find(Comment.class, parent.getId());
        assertThat(findComment.getDateTime().getCanceledAt()).isEqualTo("2023-03-11");
    }

    @Test
    public void findParentAndChildComment() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment parent = CommentFactory.createTestComment(findMember, findPost);
        Comment childComment = CommentFactory.createTestComment(findMember, findPost);
        childComment.connectChildParent(parent);
        em.persist(parent);
        em.persist(childComment);

        em.flush();
        em.clear();

        Comment findComment = em.find(Comment.class, parent.getId());
        Comment findComment2 = em.find(Comment.class, childComment.getId());

        Pageable pageable = PageRequest.of(0, 1);
        //when
        Slice<Comment> commentSlice = commentRepository.findParentAndChildComment("USER", findPost.getId(), findComment.getId(), null, pageable);
        //then
        Comment resultComment = commentSlice.getContent().get(0);
        assertThat(resultComment).isEqualTo(findComment2);
    }
}
