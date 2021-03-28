package com.tena.untact2021.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static org.springframework.util.StringUtils.trimWhitespace;
import static com.tena.untact2021.dto.Search.SearchKeywordType.*;

@Slf4j
@Data
public class Search {

    private SearchKeywordType searchKeywordType = TITLEANDBODY;
    private String searchKeyword;

    public void setSearchKeywordType(SearchKeywordType searchKeywordType) {
        log.debug("Search.setSearchKeywordType");
        log.debug("searchKeywordType: {}", searchKeywordType);

        this.searchKeywordType = searchKeywordType;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = trimWhitespace(searchKeyword);
    }

    public enum SearchKeywordType {
        TITLE("title"), BODY("body"), TITLEANDBODY("titleAndBody");

        private final String value;

        SearchKeywordType(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return value;
        }

        public static boolean isDefined(String searchKeywordType) {
            return Arrays
                    .stream(values())
                    .anyMatch(s -> s.getKey().equals(searchKeywordType.toUpperCase()));
        }

        public static boolean isNotDefined(String searchKeywordType) {
            return !isDefined(searchKeywordType);
        }
    }

}
