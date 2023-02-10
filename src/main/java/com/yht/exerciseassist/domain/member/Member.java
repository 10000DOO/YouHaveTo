package com.yht.exerciseassist.domain.member;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String email;

    private String loginId;

    private String password;

    private String field; //유저 활동 지역

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private MemberType role;

    @OneToMany(mappedBy = "postWriter")
    private List<Post> posts = new ArrayList<>();
    @Embedded //생성일 수정일 삭제일
    private DateTime dateTime;

    @OneToMany(mappedBy = "member")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "commentWriter")
    private List<Comment> comments = new ArrayList<>();

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
}
