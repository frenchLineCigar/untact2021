package com.tena.untact2021.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
public enum AuthLevel {

	ADMIN(7, "관리자", "red"), USER(3, "일반회원", "gray"), EMPTY(0, "없음", "");

	private final int value;
	private final String name;
	private final String color;

	AuthLevel(int value, String name, String color) {
		this.value = value;
		this.name = name;
		this.color = color;
	}

	public static AuthLevel fromName(String name) {
		return Arrays.stream(AuthLevel.values())
				.filter(authLevel -> authLevel.name.equals(name))
				.findAny()
				.orElse(EMPTY);
	}

	public static AuthLevel fromValue(int value) {
		return Arrays.stream(AuthLevel.values())
				.filter(authLevel -> authLevel.value == value)
				.findAny()
				.orElse(EMPTY);
	}

}
