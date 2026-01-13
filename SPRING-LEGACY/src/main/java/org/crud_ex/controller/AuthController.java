package org.crud_ex.controller;

import lombok.RequiredArgsConstructor;
import org.crud_ex.domain.KakaoToken;
import org.crud_ex.domain.KakaoUserInfo;
import org.crud_ex.domain.MemberVO;
import org.crud_ex.mapper.MemberMapper;
import org.crud_ex.security.jwt.JwtProvider;
import org.crud_ex.security.service.MemberIdUserDetailsService;
import org.crud_ex.service.KakaoOAuthService;
import org.crud_ex.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtProvider jwtProvider;
    private final MemberIdUserDetailsService memberIdUserDetailsService;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;


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

    @GetMapping("/auth/kakao")
    public void kakaoLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String state = UUID.randomUUID().toString();
        req.getSession().setAttribute("KAKAO_OAUTH_STATE", state);

        String redirectUri = URLEncoder.encode(kakaoRedirectUri, StandardCharsets.UTF_8);
        String authorizeUrl =
                "https://kauth.kakao.com/oauth/authorize"
                        + "?response_type=code"
                        + "&client_id=" + kakaoClientId
                        + "&redirect_uri=" + redirectUri
                        + "&state=" + state;

        res.sendRedirect(authorizeUrl);
    }

    @GetMapping("/auth/kakao/callback")
    public void kakaoCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {
        HttpSession pre = req.getSession(false);
        String saved = (pre != null) ? (String) pre.getAttribute("KAKAO_OAUTH_STATE") : null;

        if (!Objects.equals(saved, state)) {
            res.setStatus(400);
            res.getWriter().write("Invalid state");
            return;
        }

        pre.removeAttribute("KAKAO_OAUTH_STATE");

        KakaoToken token = kakaoOAuthService.exchangeCodeForToken(code);
        KakaoUserInfo userInfo = kakaoOAuthService.getUserInfo(token.getAccessToken());

        MemberVO member = memberService.upsertKakaoUser(userInfo);

        UserDetails userDetails =
                memberIdUserDetailsService.loadUserByUsername(member.getMemberId());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        pre.invalidate();
        HttpSession session = req.getSession(true);

        HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
        repo.saveContext(context, req, res);

        String jwt = jwtProvider.createAccessToken(member.getMemberId());
        Cookie cookie = new Cookie("accessToken", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        // cookie.setSecure(true); // https면 true
        res.addCookie(cookie);

        res.sendRedirect(req.getContextPath() + "/");
    }


}
