package com.example.demo.configs;


import com.example.demo.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //WebSecurityConfigurerAdapter 를 상속 받는 순간, SpringBoot 자동 Security 설정이 적용이 안됨.

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;


    //토큰 저장 공간
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    // AuthenticationManage을 Bean으로 노출을 시키기 위함.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // AuthenticationManager를 재정의하기 위함.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //내가 구현한 accountService에 내가 지정한 encoder를 사용하겠다.
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    //Security filter를 적용할지 말지 결정하는 곳. (Web단에서)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        //정적 Resource을 다 무시 하기 위함.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
