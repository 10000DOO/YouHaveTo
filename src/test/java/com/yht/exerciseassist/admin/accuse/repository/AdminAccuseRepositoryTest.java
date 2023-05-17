package com.yht.exerciseassist.admin.accuse.repository;

import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.factory.AccuseFactory;
import com.yht.exerciseassist.domain.factory.CommentFactory;
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
class AdminAccuseRepositoryTest {

    @Autowired
    private AdminAccuseRepository adminAccuseRepository;
    @Autowired
    private EntityManager em;

    @Test
    void searchAccuseType() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Post post = PostFactory.createTestPostQA(findMember);

        em.persist(post);

        em.flush();
        em.clear();

        Post findPost = em.find(Post.class, post.getId());

        Comment comment = CommentFactory.createTestComment(findMember, findPost);

        em.persist(comment);

        em.flush();
        em.clear();

        Comment findComment = em.find(Comment.class, comment.getId());

        Accuse accusePost = AccuseFactory.createPostAccuse(findPost);
        Accuse accuseComment = AccuseFactory.createCommentAccuse(findComment);

        em.persist(accusePost);
        em.persist(accuseComment);

        em.flush();
        em.clear();

        List<String> accuseType = new ArrayList<>();
        accuseType.add("ABUSIVE_LANGUAGE_BELITTLE");

        List<String> accuseGetType = new ArrayList<>();
        accuseGetType.add("POST");
        accuseGetType.add("COMMENT");

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "created_at"));

        ///when
        Slice<Accuse> accuses = adminAccuseRepository.accuseAsAccuseType(accuseType, accuseGetType, pageRequest);

        //then
        List<Accuse> content = accuses.getContent();
        assertThat(content.size()).isEqualTo(2);
        assertThat(accuses.getNumber()).isEqualTo(0);
        assertThat(accuses.isFirst()).isTrue();
        assertThat(accuses.hasNext()).isFalse();

    }
}
