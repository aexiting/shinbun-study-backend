package org.aexitingproject.shinbunbackend.controllers;

import org.aexitingproject.shinbunbackend.NewsService;
import org.aexitingproject.shinbunbackend.data.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    @Autowired
    public NewsController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public ResponseEntity<NewsResponse> getNews(@RequestParam final String query,
                                                @RequestParam(defaultValue = "relevance") final String sortBy,
                                                @RequestParam(defaultValue = "50") final String pageSize) {
        logger.info("getNews query={}, sortBy={}, pageSize={}", query, sortBy, pageSize);

        if (query == null || query.isEmpty()) {
            logger.warn("getNews query is empty");
            return ResponseEntity.badRequest().build();
        }

        try {
            Optional<NewsResponse> news = newsService.getNews(query, sortBy, pageSize);

            if (news.isPresent()) {
                return ResponseEntity.ok(news.get());
            } else {
                logger.warn("getNews returned null");
                return ResponseEntity.notFound().build();
            }
        } catch (final Exception e) {
            logger.warn("getNews failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
