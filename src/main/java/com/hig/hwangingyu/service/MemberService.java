package com.hig.hwangingyu.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.repository.MemberRepository;

public class MemberService /* implements UserDetailsService */ {

    private final MemberRepository memberRepository;


    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void register(Member member) {

        try{
            validateDup(member);
        } catch(IllegalStateException e) {
            throw e;
        }
        
        UserDetails user = User.builder()
                .username(member.getName())
                .password(passwordEncoder.encode(member.getPasswd()))
                .roles("USER")
                .build();
        memberRepository.register(user);
    }
    
    @Transactional
    public Optional<Member> findMember(String username) {
        return memberRepository.findByName(username);
    }

    @Transactional
    public void withDrawl(String username) {
        memberRepository.withDrawl(username);
    }


    public void validateDup(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(i -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    /*
     * @Override
     * public UserDetails loadUserByUsername(String username) throws
     * UsernameNotFoundException {
     * Member member = memberRepository.findByName(username).get();
     * if(member == null) {
     * throw new UsernameNotFoundException(username+"is not found");
     * }
     * return new MemberDetails(member);
     * }
     * 
     * class MemberDetails extends Member implements UserDetails {
     * 
     * public MemberDetails(Member member) {
     * super(member.getName(), member.getPasswd());
     * }
     * 
     * @Override
     * public Collection<? extends GrantedAuthority> getAuthorities() {
     * List<String> list = memberRepository.getAuthoritiesByName(getName());
     * List<GrantedAuthority> authorities = new ArrayList<>();
     * for(String auth : list) {
     * authorities.add(new SimpleGrantedAuthority(auth));
     * }
     * return authorities;
     * }
     * 
     * @Override
     * public String getPassword() {
     * return getPasswd();
     * }
     * 
     * @Override
     * public String getUsername() {
     * return getName();
     * }
     * 
     * @Override
     * public boolean isAccountNonExpired() {
     * return false;
     * }
     * 
     * @Override
     * public boolean isAccountNonLocked() {
     * return false;
     * }
     * 
     * @Override
     * public boolean isCredentialsNonExpired() {
     * return false;
     * }
     * 
     * @Override
     * public boolean isEnabled() {
     * return false;
     * }
     * 
     * }
     */
}
