package org.crud_ex.config;

import lombok.RequiredArgsConstructor;
import org.crud_ex.security.JwtAuthFilter;
import org.crud_ex.security.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DB 기반 로그인(formLogin)용
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/favicon.ico",
                "/resources/**",
                "/css/**",
                "/js/**",
                "/images/**"
        );
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtProvider, userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 개발 중만 disable, 운영에서는 경로별로 켜는 걸 권장
                .csrf().disable()

                .authorizeRequests()

                .antMatchers("/error").permitAll()
                .antMatchers("/login", "/loginProc").permitAll()


                .antMatchers(HttpMethod.GET, "/members/new").permitAll()
                .antMatchers(HttpMethod.POST, "/members").permitAll()


                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/auth/**").permitAll()

                .antMatchers("/api/**").authenticated()

                .anyRequest().authenticated()
                .and()

                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)


                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/members", true)
                .permitAll()
                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "accessToken")
                .permitAll();
    }
}
