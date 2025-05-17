package org.aexitingproject.shinbunbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aexitingproject.shinbunbackend.data.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private static final String API_KEY_PARAM = "apiKey";
    private static final String API_QUERY_PARAM = "q";
    private static final String API_PAGE_SIZE_PARAM = "pageSize";
    private static final String API_SORT_BY_PARAM = "sortBy";

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    // Constructor injection for RestTemplate
    @Autowired
    public NewsService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    public static class NewsApiException extends RuntimeException {
        public NewsApiException(String message, Throwable cause) {
            super(message, cause);
        }

        public NewsApiException(String message) {
            super(message);
        }
    }

    /**
     * Fetches news data for a given topic. Only going to be using Japanese vocab words for this, however.
     *
     * @param query    The search query we're using to collect the news.
     * @param sortBy   The order to sort the articles in. Possible actions: relevancy, popularity, publishedAt.
     * @param pageSize The number of results to return per page.
     * @throws NewsApiException Exception for when fetching news data fails.
     * @throws IllegalArgumentException if the apiUrl is malformed.
     * @return NewsResponse DTO containing articles and some metadata.
     */
    public Optional<NewsResponse> getNews(final String query, final String sortBy, final String pageSize) {
        String url;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(new URI(apiUrl))
                    .queryParam(API_KEY_PARAM, apiKey)
                    .queryParam(API_QUERY_PARAM, query)
                    .queryParam(API_SORT_BY_PARAM, sortBy)
                    .queryParam(API_PAGE_SIZE_PARAM, pageSize);

            url = builder.toUriString();
            logger.info("Requesting weather data from URL: {}", url);
        } catch (IllegalArgumentException | URISyntaxException e) {
            logger.error("Error converting apiUrl to Uri object {}", e.getMessage());
            throw new NewsApiException("Invalid API URL configuration or params: " + e.getMessage(), e);
        }

        try {

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String jsonResponse = response.getBody();
                NewsResponse newsResponse = objectMapper.readValue(jsonResponse, NewsResponse.class);

                logger.info("Number of articles: {}", newsResponse.getTotalResults());
                List<NewsResponse.Article> articles = newsResponse.getArticles();
                for (NewsResponse.Article article : articles) {
                    logger.info("Article: {}", article.getTitle());
                }
                return Optional.of(newsResponse);
            } else {
                logger.warn("Received non-OK status: {} from news API for query: '{}'", response.getStatusCode(), query);
                return Optional.empty();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error fetching news for query '{}': {} - {}", query, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new NewsApiException("Error from News API: " + e.getStatusCode() + " for query: " + query, e);
        } catch (JsonProcessingException e) {
            throw new NewsApiException("Error processing json: " + e.getMessage());
        } catch (RestClientException e) {
            logger.error("Error fetching response response {}", e.getMessage());
           throw new NewsApiException("Error with client:" + e.getMessage());
        }
    }
}

