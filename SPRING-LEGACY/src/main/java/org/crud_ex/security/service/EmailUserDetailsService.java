package org.crud_ex.security.service;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.crud_ex.security.principal.CustomUserDetails;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service("emailUserDetailsService")
@RequiredArgsConstructor
public class EmailUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        MemberVO member = memberMapper.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("No member with email: " + email);
        }

        return new CustomUserDetails(member);
    }
}
