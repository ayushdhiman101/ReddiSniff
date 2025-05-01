package com.example.ReddiSniff.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class ScrapedPost {

    private UUID id;
    private String subreddit;
    private String title;
    private String author;
    private String permalink;
    private int upvotes;
    private Instant createdAt;
    private String url;
    private List<String> comments;
    private String selfText;
}
