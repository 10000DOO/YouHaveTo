package com.yht.exerciseassist.admin.accuse.service;

import com.yht.exerciseassist.admin.accuse.dto.AccuseListWithSliceDto;
import com.yht.exerciseassist.admin.accuse.repository.AdminAccuseRepository;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.factory.AccuseFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class AdminAccuseServiceTest {

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    AdminAccuseService adminAccuseService;
    @Autowired
    EntityManager em;
    @MockBean
    private AdminAccuseRepository adminAccuseRepository;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        adminAccuseService = new AdminAccuseService(adminAccuseRepository);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void getAccuse() throws ParseException, IllegalAccessException {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("ADMIN");

        List<String> accuseType = new ArrayList<>();
        accuseType.add("ABUSIVE_LANGUAGE_BELITTLE");

        List<String> accuseGetType = new ArrayList<>();
        accuseGetType.add("POST");

        Member testMember = MemberFactory.createTestMember();

        Post testPost = PostFactory.createTestPost(testMember);
        Accuse testAccuse = AccuseFactory.createPostAccuse(testPost);

        List<Accuse> accuseList = new ArrayList<>();
        accuseList.add(testAccuse);

        Slice<Accuse> accuses = new SliceImpl<>(accuseList);

        Pageable pageable = PageRequest.of(0, 2);
        Mockito.when(adminAccuseRepository.accuseAsAccuseType(accuseType, accuseGetType, pageable)).thenReturn(accuses);

        //when
        ResponseResult<AccuseListWithSliceDto> result = adminAccuseService.getAccuse(accuseType, accuseGetType, pageable);

        //then
        assertThat(result.getData().getAccuseListDto().get(0).getAccuseType()).isEqualTo(accuseList.get(0).getAccuseType().getMessage());
        assertThat(result.getData().getAccuseListDto().get(0).getAccuseGetType()).isEqualTo(accuseList.get(0).getAccuseGetType().getMessage());
        assertThat(result.getData().getAccuseListDto().get(0).getPostId()).isEqualTo(accuseList.get(0).getPost().getId());
        assertThat(result.getData().getAccuseListDto().get(0).getContent()).isEqualTo(accuseList.get(0).getContent());
        assertThat(result.getData().getAccuseListDto().get(0).getCreatedAt()).isEqualTo(accuseList.get(0).getDateTime().getCreatedAt().split(" ")[0]);
        assertThat(result.getData().getAccuseListDto().get(0).getCommentId()).isEqualTo(null);
        assertThat(result.getData().getAccuseListDto().size()).isEqualTo(1);
        assertThat(result.getData().getIsFirst()).isTrue();
        assertThat(result.getData().getHasNext()).isFalse();
    }

    @Test
    public void deleteAccuse() throws IllegalAccessException {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("ADMIN");

        Member member = MemberFactory.createTestMember();

        Post testPost = PostFactory.createTestPost(member);

        Accuse testAccuse = AccuseFactory.createPostAccuse(testPost);

        testAccuse.setAccuseIdUsedOnlyTest(1L);

        Mockito.when(adminAccuseRepository.findByNotDeletedId(1L)).thenReturn(Optional.of(testAccuse));
        ResponseResult<Long> mockResult = new ResponseResult<>(HttpStatus.OK.value(), 1L);

        //when
        ResponseResult<Long> responseResult = adminAccuseService.deleteAccuse(1L);

        //then
        assertThat(responseResult).isEqualTo(mockResult);
    }
}