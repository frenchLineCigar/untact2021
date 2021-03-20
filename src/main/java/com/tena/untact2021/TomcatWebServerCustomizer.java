package com.tena.untact2021;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * RFC 7230 and RFC 3986 관련 에러 처리
 * - 프론트에서 param에 특수문자 encodeURI 처리 안하고 넘길 경우 대비
 * Ex) http://localhost:8021/user/article/list?searchKeyword=2\
 * java.lang.IllegalArgumentException: Invalid character found in the request target.
 * The valid characters are defined in RFC 7230 and RFC 3986
 */
@Configuration
public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}"));
	}
}