package com.example.slamstat.models;

public class NBANews {
    private String source;
    private String title;
    private String url;

    public NBANews(String source, String title, String url) {
        this.source = source;
        this.title = title;
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

