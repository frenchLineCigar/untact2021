package com.tena.untact2021.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
		String ext = fileName.substring(pos + 1).toLowerCase(); // 소문자로

		switch (ext) {
			case "jpeg":
				return "jpg";
			case "htm":
				return "html";
		}

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
			case "jfif":
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
			case "htm":
			case "html":
				return "html";
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

	/**
	 * Example of using "," as a delimiter
	 * "1, 2" -> [1, 2]
	 */
	public static List<Integer> getIdsToList(String ids, String delimiter) {
		return Arrays.asList(ids.split(delimiter))
				.stream()
				.map(id -> Integer.parseInt(id.trim()))
				.collect(Collectors.toList());
	}

	/**
	 * delimiter 미입력 시 기본값은 콤마(,)
	 */
	public static List<Integer> getIdsToList(String ids) {
		return getIdsToList(ids, ",");
	}

	/* 지정된 경로의 파일 삭제 */
	public static boolean deleteFile(String filePath) {
		java.io.File ioFile = new java.io.File(filePath);
		if (ioFile.exists()) {
			return ioFile.delete();
		}

		return false;
	}

	/**
	 * byte 용량을 환산하여 반환
	 * 용량의 크기에 따라 MB, KB, byte 단위로 환산함
	 * @param fileSize  byte 값
	 * @param fixed     환산된 용량의 소수점 자릿수
	 * @return {String}
	 */
	public static String byteSizeTo(float fileSize, Integer fixed) {
		String str;

		//MB 단위 이상일때 MB 단위로 환산
		if (fileSize >= 1024 * 1024) {
			fileSize = fileSize / (1024 * 1024);
			str = formatNumberWithComma(fileSize, fixed) + " MB";
			//str = toFixed(fileSize, fixed) + " MB";
		}
		//KB 단위 이상일때 KB 단위로 환산
		else if (fileSize >= 1024) {
			fileSize = fileSize / 1024;
			str = formatNumberWithComma(fileSize, fixed) + " KB";
		}
		//KB 단위보다 작을때 byte 단위로 환산
		else {
			str = formatNumberWithComma(fileSize, fixed) + " byte";
		}
		return str;
	}

	public static String byteSizeTo(float fileSize) {
		return byteSizeTo(fileSize, null);
	}

//	// 소수점 자릿수 표기
//	public static String toFixed(float fileSize, Integer fixed) {
//
//		if (fixed == null || fixed <= 0) {
//			return String.valueOf(fileSize);
//		}
//
//		return String.format("%." + fixed + "f", fileSize);
//	}

	/**
	 * 숫자를 콤마(,)로 구분된 문자열로 포맷해서 반환
	 * @param num  변환할 숫자
	 * @param fixed  출력할 소수점 자릿수
	 * @return {String}
	 */
	public static String formatNumberWithComma(double num, Integer fixed) {
		// 기본 콤마 표기
		DecimalFormat df = new DecimalFormat("###,###,###");

		// 소수점 자릿수 표기
		// if value of 'fixed' is 2, then it is like using 'DecimalFormat df = new DecimalFormat("###,###,###.00");'
		if (fixed != null && fixed > 0) {
			df.setMaximumFractionDigits(fixed);
		}

		return df.format(num);
	}

	public static String formatNumberWithComma(String numStr, Integer fixed) {
		return formatNumberWithComma(Double.parseDouble(numStr), fixed);
	}

	public static String formatNumberWithComma(double num) {
		return formatNumberWithComma(num, null);
	}

	public static String formatNumberWithComma(String numStr) {
		return formatNumberWithComma(Double.parseDouble(numStr));
	}

	// 문자열이 모두 숫자로 구성됐는지 여부
	public static boolean allNumberString(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return true;
		}

		for (int i = 0; i < str.length(); i++) {
			if (! Character.isDigit(str.charAt(i))) { //문자열을 구성하는 문자가 하나라도 숫자가 아닐 경우
				return false;
			}
		}

		return true;
	}

	// 문자열이 숫자로 시작하는지 여부
	public static boolean startsWithNumber(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return false;
		}

		return Character.isDigit(str.charAt(0)); // 문자열의 시작 문자가 숫자인지
	}

	// 로그인 아이디 유효성 체크
	public static boolean isStandardLoginIdString(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return false;
		}

		// 조건
		// 5자 이상, 20자 이하로 구성
		// 숫자로 시작 금지
		// _, 알파벳, 숫자로만 구성
		String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$"; // 문자열의 시작은 영문자 1자로 출발해야 하고, 그 다음부터는 '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 4 ~ 19자 이하 (총 5~20자)

		return Pattern.matches(regex, str);
	}

	/*
		URI의 쿼리 스트링에서 입력한 이름의 파라미터를 제거한다
		Ex) Util.getNewUrlRemoved("http://www.naver.com/?search=123&type=abc", "search");
		-> http://www.naver.com/?type=abc
	 */
	public static String getNewUriRemoved(String uri, String paramName) {
		String deleteStrStarts = paramName + "=";
		int delStartPos = uri.indexOf(deleteStrStarts);

		if (delStartPos != -1) {
			int delEndPos = uri.indexOf("&", delStartPos);

			if (delEndPos != -1) {
				delEndPos++;
				uri = uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length());
			} else {
				uri = uri.substring(0, delStartPos);
			}
		}

		if (uri.charAt(uri.length() - 1) == '?') {
			uri = uri.substring(0, uri.length() - 1);
		}

		if (uri.charAt(uri.length() - 1) == '&') {
			uri = uri.substring(0, uri.length() - 1);
		}

		return uri;
	}

	/*
		URI의 쿼리 스트링에서 해당 파라미터의 값을 변경한다
		Ex) Util.getNewUri("http://www.naver.com/?search=123&type=abc&page=4", "page", 3);
		-> http://www.naver.com/?search=123&type=abc&page=3
	 */
	public static String getNewUri(String uri, String paramName, String paramValue) {
		uri = getNewUriRemoved(uri, paramName);

		if (uri.contains("?")) {
			uri += "&" + paramName + "=" + paramValue;
		} else {
			uri += "?" + paramName + "=" + paramValue;
		}

		uri = uri.replace("?&", "?");

		return uri;
	}

	/*
		URI의 쿼리 스트링에서 해당 파라미터의 값을 변경후 인코딩까지
	 */
	public static String getNewUriEncoded(String uri, String paramName, String pramValue) {
		return getUriEncodedAsUTF8(getNewUri(uri, paramName, pramValue));
	}

}
