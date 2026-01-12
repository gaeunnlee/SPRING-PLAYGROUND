package org.crud_ex.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberIdentityVO {
    private String memberIdentityId;         // UUID
    private String memberId;           // FK
    private String provider;           // LOCAL/KAKAO/GOOGLE
    private String providerUserId;     // oauth_id or email
    private String emailAtProvider;
    private LocalDateTime createdAt;
}
