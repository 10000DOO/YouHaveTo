package com.yht.exerciseassist.domain.likeCount.repository;

import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.likeCount.LikeCount;
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
class LikeCountRepositoryTest {

    @Autowired
    private LikeCountRepository likeCountRepository;
    @MockBean
    private PostRepositoryImpl postRepositoryImpl;
    @Autowired
    private EntityManager em;

    @Test
    void findByPost() {
        //given
        Member member = MemberFactory.createTestMember();
        em.persist(member);
        Post post = PostFactory.createTestPost(member);
        em.persist(post);
        LikeCount likeCount = new LikeCount(post, member);

        em.persist(likeCount);
        em.flush();
        em.clear();
        //when
        Optional<LikeCount> byPost = likeCountRepository.findByPost(post);
        //then
        assertThat(byPost.get()).isEqualTo(likeCount);

    }

    @Test
    void deleteByPost() {
        //given
        Member member = MemberFactory.createTestMember();
        em.persist(member);
        Post post = PostFactory.createTestPost(member);
        em.persist(post);
        LikeCount likeCount = new LikeCount(post, member);

        em.persist(likeCount);
        em.flush();
        em.clear();
        //when
        likeCountRepository.deleteByPost(post);
        //then
        Optional<LikeCount> likeCount1 = Optional.ofNullable(em.find(LikeCount.class, likeCount.getId()));
        assertThat(likeCount1.isPresent()).isFalse();
    }
}
