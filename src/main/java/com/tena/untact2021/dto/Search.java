package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
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
                    .stream(SearchKeywordType.values())
                    .anyMatch(s -> s.getKey().equals(searchKeywordType.toUpperCase()));
        }

        public static boolean isNotDefined(String searchKeywordType) {
            return !isDefined(searchKeywordType);
        }

        /**
         ** <p>Convert Before Biding Parameter (using Setter) annotated with @RequestBody</p>
         ** <p>- 클라이언트 쪽에서 대소문자 신경쓰지 않고 사용하도록 서버 측에서 처리함</p>
         ** <p>- Request Body가 JSON 타입이면 Converter는 무용지물이므로, @JsonCreator 사용해 파싱</p>
         */
        @JsonCreator
        public static SearchKeywordType from(String source) {
            log.debug("SearchKeywordType.from");
            log.debug("source: {}", source);

            return SearchKeywordType.valueOf(source.toUpperCase());
        }
    }

}
