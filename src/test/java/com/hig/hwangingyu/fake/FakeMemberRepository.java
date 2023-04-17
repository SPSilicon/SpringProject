package com.hig.hwangingyu.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.repository.MemberRepository;

public class FakeMemberRepository implements MemberRepository{
    static final Map<String,Member> dataSource = new HashMap<>();

    public FakeMemberRepository() {};
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(dataSource.values());
    }

    @Override
    public void withDrawl(String memberName) {
        dataSource.remove(memberName);
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.ofNullable(dataSource.get(name));
    }

    @Override
    public void register(UserDetails member) {
        Member input = new Member(member.getUsername(),member.getPassword());
        dataSource.put(input.getName(),input);
       
    }
}
