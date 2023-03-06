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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void serchType() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post1 = PostFactory.createTestPostQA(findMember);
        Post post2 = PostFactory.createTestPostQB(findMember);
        Post post3 = PostFactory.createTestPostQC(findMember);
        Post post4 = PostFactory.createTestPost(findMember);
        Post post5 = PostFactory.createTestPost(findMember);
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);
        em.persist(post4);
        em.persist(post5);

        em.flush();
        em.clear();

        List<String> postTypeTest = new ArrayList<>();
        postTypeTest.add("KNOWLEDGE");
        postTypeTest.add("COMPETITION");

        List<String> workOutCategoryTest = new ArrayList<>();
        workOutCategoryTest.add("HEALTH");
        workOutCategoryTest.add("YOGA");
        String username = "member1";
        //when
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "created_at"));
        PageRequest pageRequest2 = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "created_at"));
        PageRequest pageRequest3 = PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "created_at"));

        Slice<Post> posts = postRepository.postAsSearchType("USER", postTypeTest, workOutCategoryTest, username, pageRequest);
        Slice<Post> posts2 = postRepository.postAsSearchType("USER", postTypeTest, workOutCategoryTest, username, pageRequest2);
        Slice<Post> posts3 = postRepository.postAsSearchType("USER", postTypeTest, workOutCategoryTest, username, pageRequest3);

        //then
        List<Post> content = posts.getContent();
        assertThat(content.size()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
    }
}
