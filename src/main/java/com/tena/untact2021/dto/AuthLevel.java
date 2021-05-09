package com.tena.untact2021.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
public enum AuthLevel {

	ADMIN(7, "관리자"), USER(3, "일반회원"), EMPTY(0, "없음");

	private final int value;
	private final String name;

	AuthLevel(int value, String name) {
		this.value = value;
		this.name = name;
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
