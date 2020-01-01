package com.example.demo.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username에 해당하는 Email을 찾는다. 없으면 orElseThrow로 Exception을 보기좋게 출력함.
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        //account 객체를 Spring Security가 이해할 수 있는 UserDatails로 타입을 바꿔줌.
        //return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
        return new AccountAdapter(account);

    }

//    //account 객체를 Spring Security가 이해할 수 있는 UserDatails로 타입을 바꿔줌.
//    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
//        return roles.stream()
//                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
//                .collect(Collectors.toSet());
//    }
}
