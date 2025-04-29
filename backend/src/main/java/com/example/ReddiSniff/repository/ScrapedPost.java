package com.example.ReddiSniff.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SCRAPED_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String subreddit;
    private String title;
    private String author;
    private String permalink;
    private int upvotes;
    private Instant createdAt;
    private String url;
    private List<String> comments;
}
