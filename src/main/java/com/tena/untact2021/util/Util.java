package com.tena.untact2021.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

//유틸리티성 공용 함수
public class Util {

	// 현재 날짜
	public static String getNowDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); //포멧 정하고
		Date time = new Date(); //날짜 확보
		return format1.format(time);
	}

	// mapOf
	public static Map<String, Object> mapOf(Object... args) {
		if ( args.length % 2 != 0 ) {
			throw new IllegalArgumentException("인자를 짝수개 입력해주세요.");
		}

		int size = args.length / 2;

		Map<String, Object> map = new LinkedHashMap<>();
		//Map<String, Object> map = new HashMap<>();
		//Map<String, Object> map = new ConcurrentHashMap<>();

		for ( int i = 0; i < size; i++ ) {

			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;

			String key;
			Object value;

			try {
				key = (String) args[keyIndex];
			}
			catch ( ClassCastException e) {
				throw new IllegalArgumentException("Key는 String으로 입력해야 합니다."  + e.getMessage()
												   + "\n" + (keyIndex + 1) + "번째 파라미터의 유효하지 못한 입력값: " + args[keyIndex]);
			}

			value = args[valueIndex];
			map.put(key, value);
		}
		return map;
	}

	@SafeVarargs
	public static Map<String, Object> mapOf(Map<String, Object>... args) {

		if ( args.length == 0 ) {
			throw new IllegalArgumentException("인자를 입력해주세요.");
		}

		Map<String, Object> result = new LinkedHashMap<>();

		for(Map<String, Object> map : args) {
			for(String key : map.keySet()) {
				result.put(key, map.get(key));
			}
		}
		return result;
	}

	// Obj to int
	public static int getAsInt(Object object, int defaultValue) {
		if (object instanceof BigInteger) {
			return ((BigInteger) object).intValue();
		} else if (object instanceof Double) {
			//return ((Double) object).intValue();
			return (int) Math.floor((double) object); //소수점 이하는 버릴거임
		} else if (object instanceof Float) {
			return (int) Math.floor((float) object); //소수점 이하는 버릴거임
		} else if (object instanceof Long) {
			return (int) object;
		} else if (object instanceof Integer) {
			return (int) object;
		} else if (object instanceof String) {
			return Integer.parseInt((String) object);
		}

		return defaultValue;
	}

	public static int getAsInt(Object object) {
		final int DEFAULT_VALUE = 0;
		return getAsInt(object, DEFAULT_VALUE);
	}

//    // toMap : Object -> Map 으로 변환
//    public static Map<String, Object> toMap(Object target) {
//
//        Field[] fields = target.getClass().getDeclaredFields();
//        Map<String, Object> result = new HashMap<String, Object>();
//
//        for (Field field : fields) {
//            try {
//                // 현재 필드
//                field.setAccessible(true); //private 필드에 접근 가능하게 한다
//                result.put(field.getName(), field.get(target));
//            } catch (IllegalArgumentException | IllegalAccessException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//
//        return result;
//    }

    /**
     * getParamMap : 요청 파라미터들을 모두 추출해 Map 형태로 파싱
     */
    public static Map<String, Object> getParamMap(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            Object paramValue = request.getParameter(paramName);

            param.put(paramName, paramValue);
        }

        return param;
    }

    /**
     * toJsonStr : Map 객체를 JSON 형태로 파싱
     */
    public static String toJsonStr(Map<String, Object> param) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /* 지정 포맷으로 문자열 인코딩 */
	public static String getUriEncodedByCharset(String str, String charset) {
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/* 지정 포맷으로 문자열 인코딩 */
    public static String getUriEncodedByCharset(String str, Charset charset) {
        return URLEncoder.encode(str, charset);
    }

    /* UTF-8 문자열 인코딩 */
	public static String getUriEncodedAsUTF8(String str) {
		return getUriEncodedByCharset(str, StandardCharsets.UTF_8);
	}

}
