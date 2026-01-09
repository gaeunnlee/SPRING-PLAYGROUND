package org.crud_ex.config;

import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/favicon.ico",
                        "/resources/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 개발 중 회원가입 POST 편하게(나중에 켜기)
                .authorizeRequests()

                .antMatchers("/api/hello", "/error").permitAll()


                .antMatchers(HttpMethod.GET,  "/members/new").permitAll()
                .antMatchers(HttpMethod.POST, "/members").permitAll()


                .antMatchers("/login", "/loginProc").permitAll()


                .anyRequest().authenticated()
                .and()
                .formLogin()

                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .usernameParameter("email")     // 로그인 폼 input name
                .passwordParameter("password")  // 로그인 폼 input name
                .defaultSuccessUrl("/members", true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
    }

//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/hello").authenticated()
//                        .anyRequest().permitAll()
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((req, res, e) -> res.sendError(401))
//                );
//
//        return http.build();
//    }
}