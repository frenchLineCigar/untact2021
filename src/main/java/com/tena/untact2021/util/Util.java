package com.tena.untact2021.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//유틸리티성 공용 함수
public class Util {
	
	// 현재 날짜
	public static String getNowDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); //포멧 정하고
		Date time = new Date(); //날짜 확보
		return format1.format(time);
	}
	
}
