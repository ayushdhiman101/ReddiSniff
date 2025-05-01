# Reddit Scraper API 📊

Welcome to the **Reddit Scraper API**! This tool allows you to fetch data from Reddit subreddits, including posts and their associated comments, using the Reddit API. Whether you're building an analytics dashboard, content aggregation tool, or anything in between, this API provides an easy way to pull information from Reddit.

## Features 🚀
- Fetch Reddit posts from any subreddit.
- Filter posts by listing type (hot, new, top, controversial).
- Retrieve posts along with their comments.
---

## Table of Contents 📚
1. [Prerequisites](#prerequisites)
2. [How It Works](#how-it-works)
3. [Process](#Process)
4. [Input Parameters](#input-parameters)
5. [JSON Output Parameters](#json-output-parameters)

---

## Prerequisites 🛠️

Before you start, ensure that you have the following:
- **Java Development Kit (JDK)**: Ensure you have JDK 11 or higher installed on your system.
- **Spring Boot**: This project is built with Spring Boot, so you'll need to have that setup.
- **Reddit API Access**: This tool interacts with Reddit's API, so you'll need a Reddit account and a registered app to obtain an `accessToken` (OAuth token). You can use your personal Reddit `accessToken` for authentication.

---

## How It Works 🤖

The **Reddit Scraper API** uses the Reddit API to fetch data from subreddits. This data is retrieved through a **GET** request to Reddit's API endpoint.

### Process:
1. **Authentication**: This version uses a **personal access token** for authentication, so there's no OAuth flow.
2. **Requesting Data**: A dynamically constructed URL is used to fetch posts based on the parameters passed to the function.
3. **Parsing JSON Response**: The response is parsed into a list of `ScrapedPost` objects, which include details about the posts and comments.

---

## Input Parameters 📝

Below is a table explaining the input parameters that you need to pass to the `fetchSubredditData` method:

### Input Parameters

| **Parameter**   | **Description**                                                                 |
|-----------------|---------------------------------------------------------------------------------|
| `subreddit`     | The name of the subreddit to fetch data from (e.g., `java`, `python`, `funny`). |
| `listingType`   | Type of post listing (e.g., `hot`, `new`, `top`, `controversial`).              |
| `limit`         | The maximum number of posts to fetch (e.g., 10, 25, 50).                        |
| `time`          | (Optional) Filters posts by time (only for `top` or `controversial`).           |
| `commentLimit`  | The maximum number of comments to fetch for each post.                          |

---

## JSON Output Parameters 📡

The `fetchSubredditData` function returns a list of `ScrapedPost` objects. Here are the fields that you will get in each post's data:

### JSON Output Parameters

| **Parameter**   | **Description**                                                               |
|-----------------|-------------------------------------------------------------------------------|
| `title`         | The title of the Reddit post.                                                 |
| `author`        | The username of the post's author.                                            |
| `url`           | The direct URL to the Reddit post.                                            |
| `subreddit`     | The name of the subreddit the post belongs to.                                |
| `ups`           | The number of upvotes the post has received.                                  |
| `selftext`      | The body (text content) of the post if it is a text post; otherwise, it is empty.|
| `comments`      | A list of comments for the post (up to the specified `commentLimit`).         |

---

Made with ❤️ by Ayush Dhiman
