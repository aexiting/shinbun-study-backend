package org.aexitingproject.shinbunbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final RestTemplate restTemplate;

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url.api.url}")
    private String apiUrl;

    // Constructor injection for RestTemplate
    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
