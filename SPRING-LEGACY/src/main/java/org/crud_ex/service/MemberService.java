package org.crud_ex.service;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.MemberIdentityVO;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberIdentityMapper;
import org.crud_ex.mapper.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final MemberIdentityMapper identityMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerLocal(MemberVO member) {
        String encodedPassword = passwordEncoder.encode(member.getPasswordHash());
        member.setMemberId(UUID.randomUUID().toString());
        member.setPasswordHash(encodedPassword);
        member.setStatus("ACTIVE");

        memberMapper.insert(member);

        MemberIdentityVO identity = MemberIdentityVO.builder()
                .memberIdentityId(UUID.randomUUID().toString())
                .memberId(member.getMemberId())
                .provider("LOCAL")
                .providerUserId(member.getEmail())
                .build();

        identityMapper.insert(identity);
    }

}
