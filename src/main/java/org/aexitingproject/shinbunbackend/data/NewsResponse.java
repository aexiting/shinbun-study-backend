package org.aexitingproject.shinbunbackend.data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResponse {

    private String status;
    private int totalResults;
    private List<Article> articles;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        private String title;
        private String url;
        private String description;
        private String author;
        private String urlToImage;
        private OffsetDateTime publishedAt;
        private String content;
    }
}
