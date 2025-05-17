package org.aexitingproject.shinbunbackend.data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
public class NewsResponse {

    private String status;
    private int totalResults;

    @JsonIgnoreProperties
    private static class Article {
        private String title;
        private String url;
        private String description;
        private String author;
        private String urlToImage;
        private Date publishedAt;
        private String content;
    }
}
