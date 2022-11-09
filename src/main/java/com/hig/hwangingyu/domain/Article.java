package com.hig.hwangingyu.domain;

import java.sql.Date;

public class Article {
    private Long id;
    private String title;
    private String body;
    private String author;
    private Date wdate;
    private Integer views;

    public Date getWdate() {
        return wdate;
    }

    public Integer getViews() {
        return views;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    private Article(Long id, String title, String body, String author, Date wdate, Integer views) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.wdate = wdate;
        this.views = views;
    }

    public static class Builder {
        Long id;
        String title;
        String body;
        String author;
        Date wdate;
        Integer views;

        public Builder() {
            id = 0L;
            title = "제목";
            body = "내용";
            author = "글쓴이";
        }

        public Builder setWdate(Date wdate) {
            this.wdate = wdate;
            return this;
        }

        public Builder setViews(Integer views) {
            this.views = views;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Article build() {
            return new Article(id, title, body, author, wdate, views);
        }

    }

}
