package org.crud_ex.controller;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "0") int age
    ) {
        MemberVO vo = MemberVO.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .name(name)
                .age(age) 
                .status("ACTIVE")
                .build();

        memberMapper.insert(vo);
        return "redirect:/login";
    }
}
