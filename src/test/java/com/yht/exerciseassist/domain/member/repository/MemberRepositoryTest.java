package com.yht.exerciseassist.domain.member.repository;

import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.repository.PostRepositoryImpl;
import com.yht.exerciseassist.domain.refreshToken.RefreshToken;
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
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @MockBean
    private PostRepositoryImpl postRepositoryImpl;
    @Autowired
    private EntityManager em;

    @Test
    public void saveMember() {
        //given
        Member member = MemberFactory.createTestMember();
        //when
        Member saveMember = memberRepository.save(member);
        //then
        assertThat(saveMember).isEqualTo(member);
    }

    @Test
    public void findMember() {
        //given
        Member member = MemberFactory.createTestMember();
        RefreshToken refreshToken = new RefreshToken("refreshToken");
        member.updateRefreshToken(refreshToken);

        Member saveMember = memberRepository.save(member);
        em.flush();
        em.clear();
        //when
        Optional<Member> byLoginId = memberRepository.findByLoginId(member.getLoginId());
        Optional<Member> byUsername = memberRepository.findByUsername(member.getUsername());
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        Optional<Member> byRefreshToken = memberRepository.findByRefreshToken(member.getRefreshToken().getRefreshToken());
        //then
        assertThat(byLoginId.get()).isEqualTo(saveMember);
        assertThat(byEmail.get()).isEqualTo(saveMember);
        assertThat(byUsername.get()).isEqualTo(saveMember);
        assertThat(byRefreshToken.get()).isEqualTo(saveMember);
    }
}
