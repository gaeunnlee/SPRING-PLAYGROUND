package org.crud_ex.controller;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping({ "", "/" })
    public String list(Model model) {
        model.addAttribute("members", memberMapper.findAll());
        return "member/list";
    }

    @GetMapping("/{memberId}")
    public String detail(@PathVariable String memberId, Model model) {
        model.addAttribute("member", memberMapper.findByMemberId(memberId));
        return "member/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("member", new MemberVO());
        return "new";
    }

    @PostMapping
    public String insert(@ModelAttribute MemberVO member) {
        String encodedPassword = passwordEncoder.encode(member.getPasswordHash());
        member.setMemberId(UUID.randomUUID().toString());
        member.setPasswordHash(encodedPassword);
        member.setStatus("ACTIVE");
        memberMapper.insert(member);

        return "redirect:/members";
    }

    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable String memberId, Model model) {
        model.addAttribute("member", memberMapper.findByMemberId(memberId));
        return "member/edit";
    }

    @PostMapping("/{memberId}/edit")
    public String update(@PathVariable String memberId, @ModelAttribute MemberVO member) {
        member.setMemberId(memberId);
        memberMapper.update(member);
        return "redirect:/members/" + memberId;
    }

    @PostMapping("/{memberId}/delete")
    public String delete(@PathVariable String memberId) {
        memberMapper.delete(memberId);
        return "redirect:/members";
    }
}
