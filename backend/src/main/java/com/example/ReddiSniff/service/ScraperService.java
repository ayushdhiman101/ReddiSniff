package com.example.ReddiSniff.service;

import com.example.ReddiSniff.dto.ScrapedPost;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ScraperService {

    @Value("${reddit.client.id}")
    private String clientId;

    @Value("${reddit.client.secret}")
    private String clientSecret;

    @Value("${reddit.username}")
    private String username;

    @Value("${reddit.password}")
    private String password;

    @Value("${reddit.useragent}")
    private String userAgent;

    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ScrapedPost> fetchSubredditData(String subreddit, String listingType, Integer limit, String time, String after, String before, Integer commentLimit) {
        // First, authenticate if needed
        authenticateIfNeeded();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("User-Agent", userAgent);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Build the URL dynamically based on user input
        String url = "https://oauth.reddit.com/r/" + subreddit + "/" + listingType + "?limit=" + limit;
        if (time != null) url += "&t=" + time; // Only for `top` or `controversial`
        if (after != null) url += "&after=" + after;
        if (before != null) url += "&before=" + before;

        try {
            // Fetch posts from Reddit
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode posts = response.getBody().get("data").get("children");

                List<ScrapedPost> scrapedPosts = new ArrayList<>();

                for (JsonNode postNode : posts) {
                    JsonNode postData = postNode.get("data");

                    // Create ScrapedPost object for each post
                    ScrapedPost post = new ScrapedPost();
                    post.setTitle(postData.get("title").asText());
                    post.setAuthor(postData.get("author").asText());
                    post.setUrl(postData.get("url").asText());
                    post.setSubreddit(subreddit);
                    post.setUpvotes(postData.get("ups").asInt());

                    // Now fetch the comments for each post
                    String postId = postData.get("id").asText();
                    List<String> comments = fetchComments(subreddit, postId, commentLimit);

                    post.setComments(comments);
                    scrapedPosts.add(post);
                }

                return scrapedPosts;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Helper function to fetch comments for a post
    private List<String> fetchComments(String subreddit, String postId, int limit) {
        String commentsUrl = "https://oauth.reddit.com/r/" + subreddit + "/comments/" + postId + "?limit=" + limit;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("User-Agent", userAgent);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    commentsUrl,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode commentNodes = response.getBody().get(1).get("data").get("children");
                List<String> comments = new ArrayList<>();

                for (JsonNode commentNode : commentNodes) {
                    if (null != commentNode) {
                        JsonNode commentData = null != commentNode.get("data") ? commentNode.get("data") : null;
                        if (null != commentData && null != commentData.get("body"))
                            comments.add(null != commentData.get("body").asText() ? commentData.get("body").asText() : ""); // Extract the comment body
                    }
                }

                return comments;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private void authenticateIfNeeded() {
        if (this.accessToken == null) {
            System.out.println("Access token not found. Authenticating...");
            authenticate();
        } else {
            System.out.println("Access token already present. Skipping authentication.");
        }
    }
    @PostConstruct
    public void authenticate() {
        System.out.println("Authenticating with Reddit API...");
        try {
            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("User-Agent", userAgent);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("username", username);
            body.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://www.reddit.com/api/v1/access_token",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                accessToken = (String) response.getBody().get("access_token");
                System.out.println("Authentication successful. Access token obtained.");
            } else {
                System.out.println("Authentication failed. Response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
