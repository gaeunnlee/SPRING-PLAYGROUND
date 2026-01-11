package org.crud_ex.security.principal;

import org.crud_ex.domain.MemberVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final MemberVO member;

    public CustomUserDetails(MemberVO member) {
        this.member = member;
    }

    public MemberVO getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return member.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        // status가 BLOCKED면 잠금 처리
        return !"BLOCKED".equalsIgnoreCase(member.getStatus());
    }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        // status가 ACTIVE일 때만 활성
        return "ACTIVE".equalsIgnoreCase(member.getStatus());
    }
}
