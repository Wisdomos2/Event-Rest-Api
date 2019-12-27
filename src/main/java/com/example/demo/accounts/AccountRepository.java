package com.example.demo.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    // 기본 메소드 이외에 Optional로 검색하기.
    // 구현할때 findBy~~~임. By의 B대문자 잘박아줘야됨 아니면 3시간동안 에러남ㅎㅎ
    Optional<Account> findByEmail(String username);
}
