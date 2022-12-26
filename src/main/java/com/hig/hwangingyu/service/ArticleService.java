package com.hig.hwangingyu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Article;
import com.hig.hwangingyu.repository.ArticleRepository;

public class ArticleService {
    protected final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {this.articleRepository = articleRepository;}

    public Long uploadArticle(Article article) {
        Long ret =articleRepository.create(article);
        return ret;
    }

    public Optional<Article> read(Long id) {
        return articleRepository.findbyId(id);
    }
    
    public void deleleArticle(Long id) {
        articleRepository.delete(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }
    public Page<Article> findAll(Pageable page) {
        return articleRepository.findAll(page);
    }
}
