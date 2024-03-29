package com.hig.hwangingyu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.hig.hwangingyu.domain.Article;
import com.hig.hwangingyu.repository.ArticleRepository;

public class ArticleService {
    protected final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {this.articleRepository = articleRepository;}

    @Transactional
    public Long uploadArticle(Article article) {
        Long ret =articleRepository.create(article);
        return ret;
    }

    @Transactional
    public boolean updateArticle(Article article) {
        return articleRepository.update(article);
    }
    
    @Transactional
    public boolean deleleArticle(Long id) {
        return articleRepository.delete(id);
    }

    public Optional<Article> read(Long id) {
        return articleRepository.findbyId(id);
    }


    public Optional<Article> findById(Long id) {
        return articleRepository.findbyId(id);
    }
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
    public Page<Article> findAll(Pageable page) {
        return articleRepository.findAll(page);
    }
    public Page<Article> searchArticle(Pageable page, String query) {
        return articleRepository.search(page, query);
    }
}
