package com.hig.hwangingyu.repository;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import com.hig.hwangingyu.domain.Member;

public interface MemberRepository {
    void register(UserDetails member);

    Optional<Member> findByName(String name);

    List<String> getAuthoritiesByName(String name);

    List<Member> findAll();
}
