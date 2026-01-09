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
    private int userno;
    private String name;
    private int age;
    private String status; // ACTIVE / BLOCKED
    private String passwordHash;
    private String email;
}