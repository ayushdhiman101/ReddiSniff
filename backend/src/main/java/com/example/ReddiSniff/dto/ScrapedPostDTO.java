package com.example.ReddiSniff.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ScrapedPostDTO {

    private UUID id;
    private String subreddit;
    private String title;
    private String author;
    private String permalink;
    private int upvotes;
    private Instant createdAt;

}
