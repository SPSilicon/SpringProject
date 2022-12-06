package com.hig.hwangingyu.repository;

import java.util.List;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.hig.hwangingyu.domain.Member;

public class MemberjdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public MemberjdbcRepository(DataSource dataSource) {
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

    //private RowMapper<String> authRowMapper() {
    //    return (rs, rowNum) -> {
    //        String ret = rs.getString("name");
    //        return ret;
     //   };
    //}

    @Override
    public void register(UserDetails member) {
        jdbcUserDetailsManager.createUser(member);
    }

   // @Override
    //public List<String> getAuthoritiesByName(String name) {
    //    List<String> ret = jdbcTemplate.query("select authority from authorities where username=?", authRowMapper(),
   //             name);
   //     return ret;
   // }

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
