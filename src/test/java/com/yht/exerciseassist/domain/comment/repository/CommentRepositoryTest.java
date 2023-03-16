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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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
}