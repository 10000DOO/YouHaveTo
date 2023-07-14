package com.yht.exerciseassist.domain.member;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.likeCount.LikeCount;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.refreshToken.RefreshToken;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    @Size(max = 20)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    @Size(min = 5, max = 20)
    private String loginId;

    private String password;

    private String field; //유저 활동 지역

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private MemberType role;

    @OneToMany(mappedBy = "postWriter")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeCount> likeCount = new ArrayList<>();

    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @OneToMany(mappedBy = "member")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "commentWriter")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;

    @Builder
    public Member(String username, String email, String loginId, String password, String field, MemberType role, DateTime dateTime) {
        this.username = username;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
        this.field = field;
        this.role = role;
        this.dateTime = dateTime;
    }

    public void ChangeMedia(Media media) {
        this.media = media;
    }

    public void setMemberIdUsedOnlyTest(Long id) {
        this.id = id;
    }

    public void updateRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateLikeCount(LikeCount likeCount) {
        this.likeCount.remove(likeCount);
        this.likeCount.add(likeCount);
    }

    public void changeMemberData(String username, String password, String field, Media media) {
        this.username = username;
        this.password = password;
        this.field = field;
        this.media = media;
        this.dateTime.updatedAtUpdate();
    }

    public void changeMemberDataNoFile(String username, String password, String field) {
        this.username = username;
        this.password = password;
        this.field = field;
        this.dateTime.updatedAtUpdate();
    }

    public void changePW(String password) {
        this.password = password;
        this.dateTime.updatedAtUpdate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member that = (Member) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
