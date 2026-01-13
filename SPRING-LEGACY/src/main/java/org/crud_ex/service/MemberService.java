package org.crud_ex.service;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.KakaoUserInfo;
import org.crud_ex.domain.MemberIdentityVO;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberIdentityMapper;
import org.crud_ex.mapper.MemberMapper;
import org.springframework.dao.DuplicateKeyException;
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
        member.setMemberId(UUID.randomUUID().toString());
        member.setPasswordHash(passwordEncoder.encode(member.getPasswordHash()));
        member.setStatus("ACTIVE");

        memberMapper.insert(member);

        MemberIdentityVO identity = MemberIdentityVO.builder()
                .memberIdentityId(UUID.randomUUID().toString())
                .memberId(member.getMemberId())
                .provider("LOCAL")
                .providerUserId(member.getEmail())
                .emailAtProvider(member.getEmail())
                .build();

        identityMapper.insert(identity);
    }

    @Transactional
    public MemberVO upsertKakaoUser(KakaoUserInfo info) {
        String provider = "KAKAO";
        String providerUserId = String.valueOf(info.getId());

        MemberVO existing = identityMapper.findMemberByProviderAndUserId(provider, providerUserId);
        if (existing != null) return existing;

        try {
            MemberVO created = new MemberVO();
            created.setMemberId(UUID.randomUUID().toString());
            created.setEmail(
                    info.getKakao_account() != null ? info.getKakao_account().getEmail() : null
            );
            created.setName(
                    info.getKakao_account() != null ? info.getKakao_account().getProfile().getNickname() : null
            );
            created.setStatus("ACTIVE");

            memberMapper.insert(created);

            MemberIdentityVO identity = MemberIdentityVO.builder()
                    .memberIdentityId(UUID.randomUUID().toString())
                    .memberId(created.getMemberId())
                    .provider(provider)
                    .providerUserId(providerUserId)
                    .emailAtProvider(created.getEmail())
                    .build();

            identityMapper.insert(identity);

            return created;

        } catch (DuplicateKeyException e) {
            MemberVO race = identityMapper.findMemberByProviderAndUserId(provider, providerUserId);
            if (race != null) return race;
            throw e;
        }
    }
}
