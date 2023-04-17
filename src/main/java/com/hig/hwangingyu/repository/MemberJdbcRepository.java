package com.hig.hwangingyu.repository;

import java.util.List;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.hig.hwangingyu.domain.Member;

public class MemberJdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public MemberJdbcRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member.Builder()
                    .setName(rs.getString("username"))
                    .setPasswd(rs.getString("password"))
                    .build();
            return member;
        };
    }

    @Override
    public void register(UserDetails member) {
        jdbcUserDetailsManager.createUser(member);
    }

    @Override
    public void withDrawl(String memberName) {
        jdbcUserDetailsManager.deleteUser(memberName);
    }

    @Override
    public List<Member> findAll() {
        List<Member> ret = jdbcTemplate.query("select * from member", memberRowMapper());
        return ret;
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> ret = jdbcTemplate.query("select * from users where username = ?", memberRowMapper(), name);
        return ret.stream().findFirst();
    }

}
