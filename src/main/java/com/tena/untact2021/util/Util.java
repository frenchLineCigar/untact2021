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

	public static <T> T ifNull(T data, T fallback) {
		return data != null ? data : fallback;
	}

	@SuppressWarnings("unchecked")
	public static <T> T reqAttr(HttpServletRequest req, String attrName, T fallback) {
		return (T) ifNull(req.getAttribute(attrName), fallback);
	}

	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		}

		if (data instanceof String) {
			String strData = (String) data;

			return strData.trim().length() == 0;
		}

		if (data instanceof Integer) {
			Integer integerData = (Integer) data;

			return integerData == 0;
		}

		if (data instanceof List) {
			List<?> listData = (List<?>) data;

			return listData.isEmpty();
		}

		if (data instanceof Map) {
			Map<?, ?> mapData = (Map<?, ?>) data;

			return mapData.isEmpty();
		}

		return false;
	}

	public static <T> T ifEmpty(T data, T fallback) {
		return !isEmpty(data) ? data : fallback;
	}

	/* 파일 이름에서 확장자 추출 */
	public static String getFileExtFromFileName(String fileName) {
		// newFile.jpg
		int pos = fileName.lastIndexOf(".");
		String ext = fileName.substring(pos + 1).toLowerCase();

		return ext;
	}

	/* 파일 규격 (상위 규격) */
	public static String getFileExtTypeCodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName);

		switch (ext) {
			case "jpeg":
			case "jpg":
			case "gif":
			case "png":
				return "img";
			case "mp4":
			case "avi":
			case "mov":
				return "video";
			case "mp3":
				return "audio";
		}

		return "etc";
	}

	/* 파일 규격2 (하위 규격) */
	public static String getFileExtType2CodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName);

		switch (ext) {
			case "jpeg":
			case "jpg":
				return "jpg";
			case "gif":
			case "png":
			case "mp4":
			case "mov":
			case "avi":
			case "mp3":
				return ext;
		}

		return "etc";
	}

	/**
	 * fileDir 생성용
	 * - 리눅스에서 폴더 하나에 파일을 몇만개 이상 담을 수 없기 때문에 생성년월로 폴더 분리
	 * - 현재 년월을 yyyy_MM 문자열 포맷으로 리턴
	 */
	public static String getNowYearMonthDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM");

		String dateStr = format1.format(System.currentTimeMillis());

		return dateStr;
	}

}
