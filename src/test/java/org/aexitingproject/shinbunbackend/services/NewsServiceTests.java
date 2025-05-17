package org.aexitingproject.shinbunbackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aexitingproject.shinbunbackend.data.NewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NewsService newsService;

    private static final String TEST_API_URL = "testAPIUrl";
    private static final String TEST_API_KEY = "testAPIKey";
    private static final String TEST_QUERY = "test";
    private static final String TEST_SORT_BY = "relevance";
    private static final String TEST_PAGE_SIZE = "10";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(newsService, "apiUrl", TEST_API_URL);
        ReflectionTestUtils.setField(newsService, "testAPIKey", TEST_API_KEY);
    }

    @Test
    public void getNewsSuccessShouldReturnNewsResponse() throws Exception {
        final String mockResponse = "{\"totalResults\":1,\"articles\":[{\"title\":\"Test Article\"}]}";
        final NewsResponse mockNewsResponse = new NewsResponse();
        mockNewsResponse.setTotalResults(1);
        final NewsResponse.Article testArticle = new NewsResponse.Article();
        testArticle.setTitle("Test Article");
        mockNewsResponse.setArticles(List.of(testArticle));
        Optional<NewsResponse> getNewsResponse = newsService.getNews(TEST_QUERY, TEST_SORT_BY, TEST_PAGE_SIZE);
    }
}
