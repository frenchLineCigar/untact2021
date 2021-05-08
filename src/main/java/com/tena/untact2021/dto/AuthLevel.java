package com.tena.untact2021.dto;

import lombok.Getter;

@Getter
public enum AuthLevel {

	ADMIN(7), USER(3);

	private final int roleNo;

	AuthLevel(int roleNo) {
		this.roleNo = roleNo;
	}

	public String getKey() {
		return name();
	}

}
