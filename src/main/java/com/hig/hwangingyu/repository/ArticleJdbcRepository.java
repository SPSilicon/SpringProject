package com.hig.hwangingyu.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.hig.hwangingyu.domain.Article;

public class ArticleJdbcRepository implements ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ArticleJdbcRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RowMapper<Article> articleRowMapper() {
        return (rs, rowNum) -> {
            Article article = new Article.Builder()
                    .setAuthor(rs.getString("author"))
                    .setBody(rs.getString("body"))
                    .setTitle(rs.getString("title"))
                    .setWdate(rs.getTimestamp("wdate"))
                    .setId(rs.getLong("id"))
                    .build();
            return article;
        };
    }

    @Override
    public Long create(Article article) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("article").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("title", article.getTitle());
        params.put("author", article.getAuthor());
        params.put("body", article.getBody());
        params.put("wdate", java.sql.Timestamp.valueOf(LocalDateTime.now()));
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        article.setId(key.longValue());
        return key.longValue();
    }

    @Override
    public boolean delete(long id) {
        int rows = jdbcTemplate.update("delete from article where id =?", id);
        return rows>0;
    }

    @Override
    public Optional<Article> findbyId(long id) {
        List<Article> ret = jdbcTemplate.query("select * from article where id=?", articleRowMapper(), id);
        return ret.stream().findFirst();
    }

    @Override
    public boolean update(Article article) {
        int rows=0;
        rows += jdbcTemplate.update("update article set body = ? where id =?", article.getBody(), article.getId());
        rows += jdbcTemplate.update("update article set title = ? where id =?", article.getTitle(), article.getId());
        return rows>0;
    }

    @Override
    public List<Article> findAll() {
        List<Article> ret = jdbcTemplate.query("select * from article", articleRowMapper());
        return ret;
    }

    
    public int getCount() {
        Optional<Integer> count = Optional.ofNullable(jdbcTemplate.queryForObject("select count(*) from article",Integer.class));
        return count.orElse(0);
    }

    public Page<Article> findAll(Pageable page) {
        List<Article> ret = jdbcTemplate.query("select * from article order by id DESC limit "+page.getPageSize() + " offset "+page.getOffset(), articleRowMapper());
        return new PageImpl<Article>(ret, page, getCount());
    }

}
