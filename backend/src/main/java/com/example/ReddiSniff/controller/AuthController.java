package com.example.ReddiSniff.controller;

import com.example.ReddiSniff.dto.ScrapedPost;
import com.example.ReddiSniff.service.ScraperService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ScraperService scraperService;

    @PostMapping("/fetch/{subreddit}")
    public List<ScrapedPost> fetchSubredditData(
            @PathVariable String subreddit,
            @RequestParam(defaultValue = "hot") String listingType,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String after,
            @RequestParam(required = false) String before,
            @RequestParam(defaultValue = "2") Integer commentLimit) {

        int safeLimit = Math.min(limit, 100);
        int commentSafeLimit = Math.min(commentLimit, 10);
        return scraperService.fetchSubredditData(subreddit, listingType, safeLimit, time, after, before, commentSafeLimit);
    }

    @PostConstruct
    public void init() {
        System.out.println("AuthController loaded ✅");
    }

}
