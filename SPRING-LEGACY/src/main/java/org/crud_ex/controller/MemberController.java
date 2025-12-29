package org.crud_ex.controller;

import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberMapper memberMapper;

    public MemberController(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @GetMapping({ "", "/" })
    public String list(Model model) {
        model.addAttribute("members", memberMapper.findAll());
        return "member/list";
    }

    @GetMapping("/{userno}")
    public String detail(@PathVariable int userno, Model model) {
        model.addAttribute("member", memberMapper.findByUserno(userno));
        return "member/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("member", new MemberVO());
        return "member/new";
    }

    @PostMapping
    public String insert(@ModelAttribute MemberVO member) {
        memberMapper.insert(member);
        return "redirect:/members";
    }

    @GetMapping("/{userno}/edit")
    public String editForm(@PathVariable int userno, Model model) {
        model.addAttribute("member", memberMapper.findByUserno(userno));
        return "member/edit";
    }

    @PostMapping("/{userno}/edit")
    public String update(@PathVariable int userno, @ModelAttribute MemberVO member) {
        member.setUserno(userno);
        memberMapper.update(member);
        return "redirect:/members/" + userno;
    }

    @PostMapping("/{userno}/delete")
    public String delete(@PathVariable int userno) {
        memberMapper.delete(userno);
        return "redirect:/members";
    }
}
