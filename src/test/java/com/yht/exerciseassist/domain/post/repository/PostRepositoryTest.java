package com.yht.exerciseassist.domain.post.repository;

import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@Rollback
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void savePost() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPost(findMember);
        //when
        Post savedPost = postRepository.save(post);
        //then
        assertThat(savedPost).isEqualTo(post);
    }

    @Test
    public void findByIdWithRole() {
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
        //when
        Post testPost = postRepository.findByIdWithRole(findPost.getId(), "USER").get();
        //then
        assertThat(findPost).isEqualTo(testPost);
    }
}
