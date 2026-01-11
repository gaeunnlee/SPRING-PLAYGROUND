package org.crud_ex.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    private String memberId;
    private String passwordHash;

    private String name;
    private String email;
    private int age;

    private String status; // ACTIVE / BLOCKED

    private String oauthProvider;
    private String oauthId;
}