package com.tena.untact2021.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private String version;
	private String title;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tena.untact2021.controller"))
				.paths(PathSelectors.ant("/user/**")).build()
				.apiInfo(apiInfo());
		}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Spring Community 2021 API").description("회원가입, 게시물 작성 등의 기능을 제공합니다.")
				.contact(new Contact("제작자", "https://github.com/frenchLineCigar/untact2021", "admin@untact21.com"))
				.version("0.1").build();
	}

	@Bean
	public Docket apiV1() {
		version = "1.0";
		title = "Spring Community 2021 API";

		return new Docket(DocumentationType.SWAGGER_2)
				//.useDefaultResponseMessages(false)
				.groupName(version)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tena.untact2021.controller"))
				//.paths(PathSelectors.any())
				//.paths(PathSelectors.ant("/admin/**").or(PathSelectors.ant("/user/**")).or(PathSelectors.ant("/common/**")))
				.paths(PathSelectors.ant("/demo/**").negate())
				.paths(PathSelectors.ant("/test/**").negate())
				.build()
				.apiInfo(apiInfo(title, version));
	}

	@Bean
	public Docket apiV2() {
		version = "2.0";
		title = "Spring Community 2021 API";

		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName(version)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tena.untact2021.controller"))
				//.paths(PathSelectors.any())
				//.paths(PathSelectors.ant("/admin/**").or(PathSelectors.ant("/user/**")).or(PathSelectors.ant("/common/**")))
				.paths(PathSelectors.ant("/demo/**").negate())
				.paths(PathSelectors.ant("/test/**").negate())
				.build()
				.apiInfo(apiInfo(title, version));
	}

	private ApiInfo apiInfo(String title, String version) {
		return new ApiInfoBuilder()
				.title(title)
				.version(version)
				.description("회원가입, 게시물 작성 등의 기능을 제공합니다.")
				.termsOfServiceUrl("https://www.example.com")
				.contact(new Contact("Contact Me", "https://www.example.com", "admin@email.com"))
				.license("API Licence")
				.licenseUrl("http://www.example.com")
				.build();
	}

//	private ApiInfo apiInfo() {
//		return new ApiInfo(
//				"title",
//				"description",
//				"version",
//				"https://www.example.com/",
//				new Contact("Contact Me", "https://www.example.com", "admin@email.com"),
//				"Licenses",
//				"https://",
//				new ArrayList<>()
//		);
//	}


	/* 3.0.0 이상 사용시 */
	/*@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tena.untact2021.controller"))
                // 지정 패키지 경로의 RequestMapping 으로 할당된 모든 URL 리스트 지정
				// .paths(PathSelectors.any())

				// [참고] 3.0.0 이후 버전은 paths 메서드의 파라미터 타입이 java.util.function.Predicate<T> 타입으로 변경되어, 한번에 패턴 조합이 가능함

                // Case 1. 포함할 것만 지정하던가
				// -> Ex) "/admin", "/user", "/common" 으로 시작하는 URL 경로만 지정
				// .paths(PathSelectors.ant("/admin/**").or(PathSelectors.ant("/user/**")).or(PathSelectors.ant("/common/**")))

                // Case 2. 제외할 것만 지정하던가
				// -> Ex) "/test" 또는 "/demo" 로 시작하는 URL 경로만 제외
				// 2-1) 한번에 명시하는 스타일
				// .paths(PathSelectors.ant("/demo/**").or(PathSelectors.ant("/test/**")).negate())
				// 2-2) 각각 명시하는 스타일
				.paths(PathSelectors.ant("/demo/**").negate())
				.paths(PathSelectors.ant("/test/**").negate())
                // 2-3) 정규식 패턴을 활용하는 스타일
                // .paths(PathSelectors.regex("^(?!\\/test|\\/demo)(.*)$"))
				.build();
	}*/


	/* 3.0.0 이전 사용시 */
	/*@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tena.untact2021.controller"))
				// 지정 패키지 경로의 RequestMapping 으로 할당된 모든 URL 리스트 지정
				// .paths(PathSelectors.any())

	 			// [참고] 3.0.0 이전 버전은 paths 메서드의 파라미터 타입이 com.google.common.base.Predicate<String> 이고, 타입 캐스팅을 해도 오류나므로, Guava 를 방식을 고수해야 함

				// Case 1. 포함할 것만 지정하던가 -> Ex) "/admin", "/user", "/common" 으로 시작하는 URL 경로만 지정
				// .paths(Predicates.or(PathSelectors.ant("/admin/**"), PathSelectors.ant("/user/**"), PathSelectors.ant("/common/**")))

				// Case 2. 제외할 것만 지정하던가 -> Ex) "/test" 또는 "/demo" 로 시작하는 URL 경로만 제외
				.paths(Predicates.not(PathSelectors.ant("/demo/**")))
				.paths(Predicates.not(PathSelectors.ant("/test/**")))
				.build();
	}*/

}