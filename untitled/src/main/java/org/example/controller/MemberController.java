package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.DTO.LoginDto;
import org.example.domain.DTO.MemberDto;
import org.example.domain.entity.Member;
import org.example.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
public class MemberController {

    private final MemberServiceImpl memberService;

    @Autowired
    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }
    @GetMapping("/joinMember")
    public String showSighUpForm(Model  model) {
        model.addAttribute("memberDto", new MemberDto());
        return "joinMember";
    }

    @PostMapping("/joinMember")
    public String saveMember(@Valid @ModelAttribute("memberDto") MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "joinMember";
        }

        memberService.saveDTO(memberDto);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }

    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {

        // 현재 페이지를 세션에 저장
        String referer = request.getHeader("referer");
        request.getSession().setAttribute("prevPage", referer);
        log.info("uri={}", referer);
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("login") LoginDto loginDto,HttpServletRequest request,HttpSession session, Model model) {
        boolean login = memberService.login(loginDto);

        if (login) {
            String username = loginDto.getUsername();
            Member member = memberService.findByUsername(username);
            session.setAttribute("loginMember", member);

            String prevPage = (String) request.getSession().getAttribute("prevPage");
            request.getSession().removeAttribute("prevPage");

            return "redirect:" + (prevPage != null ? prevPage : "/");
        }

        model.addAttribute("error", "비밀번호 또는 아이디가 올바르지 않습니다.");
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginMember");
        return "redirect:/";
    }

}
