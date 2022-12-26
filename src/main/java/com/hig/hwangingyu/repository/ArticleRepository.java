package com.hig.hwangingyu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Article;

public interface ArticleRepository {
    public Long create(Article article);

    public int delete(long id);

    public void update(Article article);

    public Optional<Article> findbyId(long id);

    public List<Article> findAll();

    public Page<Article> findAll(Pageable Page);

}
