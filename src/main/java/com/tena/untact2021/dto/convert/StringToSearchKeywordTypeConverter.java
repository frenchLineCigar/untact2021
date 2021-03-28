package com.tena.untact2021.dto.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.tena.untact2021.dto.Search.SearchKeywordType;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 ** Convert Before Biding Parameter (using Setter) annotated with @ModelAttribute, @RequestParam
 */
@Slf4j
@Component
public class StringToSearchKeywordTypeConverter implements Converter<String, SearchKeywordType> {

    @Override
    public SearchKeywordType convert(String source) {
        log.debug("StringToSearchKeywordTypeConverter.convert");
        log.debug("source: {}", source);

        if (isBlank(source) || isNotDefinedAsKeywordType(source))
            return SearchKeywordType.TITLEANDBODY;

        return SearchKeywordType.valueOf(source.toUpperCase());
    }

    public static boolean isNotDefinedAsKeywordType(String source) {
        return !SearchKeywordType.isDefined(source);
    }

}
