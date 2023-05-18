package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.admin.accuse.repository.AdminAccuseRepositoryImpl;
import com.yht.exerciseassist.domain.comment.repository.CommentRepositoryImpl;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.factory.DiaryFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.repository.PostRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;
    @MockBean
    private PostRepositoryImpl postRepositoryImpl;
    @MockBean
    private CommentRepositoryImpl commentRepositoryImpl;
    @MockBean
    private AdminAccuseRepositoryImpl adminAccuseRepositoryImpl;

    @Autowired
    private EntityManager em;

    @Test
    public void saveDiary() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Diary diary = DiaryFactory.createTestDiary(member);
        //when
        Diary savedDiary = diaryRepository.save(diary);
        //then
        assertThat(savedDiary).isEqualTo(diary);
    }

    @Test
    public void findDiariesByUsername() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Diary diary = DiaryFactory.createTestDiary(findMember);

        diaryRepository.save(diary);
        em.flush();
        em.clear();
        //when
        List<Diary> diariesByUsername = diaryRepository.findDiariesByUsername(findMember.getUsername(), "2023-01");
        //then
        assertThat(diariesByUsername.get(0)).isEqualTo(diary);
    }

    @Test
    public void findDiaryDetailsByUsername() {
        //given
        Member member = MemberFactory.createTestMember();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Diary diary = DiaryFactory.createTestDiary(findMember);

        diaryRepository.save(diary);
        //when
        Optional<Diary> diaryDetailsByUsername = diaryRepository.findDiaryDetailsByUsername(findMember.getUsername(), "2023-01-30");
        //then
        assertThat(diaryDetailsByUsername.get()).isEqualTo(diary);
    }
}
