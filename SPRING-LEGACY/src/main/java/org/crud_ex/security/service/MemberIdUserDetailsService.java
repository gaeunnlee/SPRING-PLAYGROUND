package org.crud_ex.security.service;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.crud_ex.security.principal.CustomUserDetails;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service("memberIdUserDetailsService")
@RequiredArgsConstructor
public class MemberIdUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String memberId)
            throws UsernameNotFoundException {

        MemberVO member = memberMapper.findByMemberId(memberId);
        if (member == null) {
            throw new UsernameNotFoundException("No member with id: " + memberId);
        }

        return new CustomUserDetails(member);
    }
}
