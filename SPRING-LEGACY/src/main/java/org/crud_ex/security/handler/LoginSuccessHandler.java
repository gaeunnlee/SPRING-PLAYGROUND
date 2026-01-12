package org.crud_ex.security.handler;

import lombok.RequiredArgsConstructor;
import org.crud_ex.security.jwt.JwtProvider;
import org.crud_ex.security.principal.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String memberId = principal.getMemberId();
        String token = jwtProvider.createAccessToken(memberId);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 운영 HTTPS면 true
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60); // 15분 (TTL)
        response.addCookie(cookie);

        // SameSite Cookie 설정
        response.addHeader(
                "Set-Cookie",
                "accessToken=" + token + "; SameSite=Lax"
        );

        response.sendRedirect(request.getContextPath() + "/members");
    }
}
