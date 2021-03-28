package com.tena.untact2021.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.StringUtils.trimWhitespace;

@Slf4j
@Data
public class Search {

    private String searchKeywordType = "titleAndBody";
    private String searchKeyword;

    public void setSearchKeywordType(String searchKeywordType) {
        log.debug("Search.setSearchKeywordType");
        log.debug("searchKeywordType: {}", searchKeywordType);

        if (isNotBlank(searchKeywordType)) //Not Empty (""), Not Null and Not Whitespace Only
            this.searchKeywordType = searchKeywordType;
    }

    public void setSearchKeyword(String searchKeyword) {
            this.searchKeyword = trimWhitespace(searchKeyword);
    }

//    public void setSearchKeyword(String searchKeyword) {
//        if (isNotBlank(searchKeyword))
//            this.searchKeyword = trimWhitespace(searchKeyword);
//        this.searchKeyword = trimToNull(searchKeyword);
//    }

}
