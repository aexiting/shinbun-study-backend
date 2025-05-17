package org.aexitingproject.shinbunbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aexitingproject.shinbunbackend.data.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final RestTemplate restTemplate;

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    // Constructor injection for RestTemplate
    @Autowired
    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches news data for a given topic. Only going to be using Japanese vocab words for this, however.
     *
     * @param query    The search query we're using to collect the news.
     * @param sortBy   The order to sort the articles in. Possible actions: relevancy, popularity, publishedAt.
     * @param pageSize The number of results to return per page.
     * @return NewsResponse DTO containing articles and some metadata, or null if an error occurs.
     */
    public NewsResponse getNews(final String query, final String sortBy, final String pageSize) {
        String url;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(new URI(apiUrl))
                    .queryParam("apiKey", apiKey)
                    .queryParam("q", query)
                    .queryParam("sortBy", sortBy)
                    .queryParam("pageSize", pageSize);

            url = builder.toUriString();
            logger.info("Requesting weather data from URL: {}", url);
        } catch (URISyntaxException ex) {
            logger.error("Error converting apiUrl to Uri object {}", ex.getMessage());
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

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
                return newsResponse;
            }

        } catch (RestClientException e) {
            logger.error("Error fetching response response {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Some generic error was thrown {}", e.getMessage());
            return null;
        }

        return null;
    }
}

