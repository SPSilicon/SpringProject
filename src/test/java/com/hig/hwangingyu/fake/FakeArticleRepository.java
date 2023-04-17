package com.hig.hwangingyu.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.hig.hwangingyu.domain.Article;
import com.hig.hwangingyu.repository.ArticleRepository;

public class FakeArticleRepository implements ArticleRepository{
    static final Map<Long,Article> dataSource = new HashMap<>();
    static Long count = 1L;

    public FakeArticleRepository() {}
    @Override
    public Long create(Article article) {
        dataSource.put(count,article);
        article.setId(count);
        return count++;
    }

    @Override
    public boolean delete(long id) {
        dataSource.remove(id);
        return true;
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(dataSource.values());
    }

    @Override
    public Page<Article> findAll(Pageable page) {
        List<Article> ret = new ArrayList<>(dataSource.values());
        return new PageImpl<Article>(ret,page,ret.size());
    }

    @Override
    public Optional<Article> findbyId(long id) {
        // TODO Auto-generated method stub
        return Optional.ofNullable(dataSource.get(id));
    }

    @Override
    public Page<Article> search(Pageable page, String query) {
        List<Article> ret = new ArrayList<>();
        for(Map.Entry<Long,Article> entry : dataSource.entrySet()) {
            if(entry.getValue().getTitle().contains(query)) {
                ret.add(entry.getValue());
            }
        }
        // TODO Auto-generated method stub
        return new PageImpl<Article>(ret,page,ret.size());
    }

    @Override
    public boolean update(Article article) {
        dataSource.put(article.getId(), article);
        // TODO Auto-generated method stub
        return true;
    }
    
}
