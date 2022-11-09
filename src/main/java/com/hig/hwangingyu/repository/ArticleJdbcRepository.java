package com.hig.hwangingyu.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

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
        params.put("body", article.getBody());
        params.put("author", article.getAuthor());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        article.setId(key.longValue());
        return key.longValue();
    }

    @Override
    public int delete(long id) {
        int rows = jdbcTemplate.update("delete from article where id =?", id);
        return rows;
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Article> findbyId(long id) {
        List<Article> ret = jdbcTemplate.query("select * from article where id=?", articleRowMapper(), id);
        // TODO Auto-generated method stub
        return ret.stream().findFirst();
    }

    @Override
    public void update(Article article) {
        jdbcTemplate.update("update article set body = ? where id =?", article.getBody(), article.getId());
        jdbcTemplate.update("update article set title = ? where id =?", article.getBody(), article.getId());
        // TODO Auto-generated method stub

    }

    @Override
    public List<Article> findAll() {
        List<Article> ret = jdbcTemplate.query("select * from article", articleRowMapper());
        // TODO Auto-generated method stub
        return ret;
    }

}
