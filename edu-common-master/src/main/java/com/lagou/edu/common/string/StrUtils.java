
package com.lagou.edu.common.string;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 字符串工具类
 * @Author Cookie
 * @Date 2014 上午10:30:08
 */
public class StrUtils extends StringUtils {

	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		if (text == null) {
			return "";
		}
		byte[] bytes = text.getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			} else {
				buffer.put(bytes, i, 1);
				i++;
			}
		}

		buffer.flip();
		byte[] arr = buffer.array();
		return new String(arr, "UTF-8");
	}

	public static boolean in(String needle, String... haystack) {
		for (String hay : haystack) {
			if (hay.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param url
	 * @return
	 */
	public static String cutPrefixs(String url) {
		if (StrUtils.isEmpty(url)) {
			return "";
		}
		if (url.startsWith("http") || url.startsWith("https") || url.startsWith("//")) {
			String[] arr = url.split("//");
			String tmp = arr[arr.length - 1];
			return StrUtils.substringAfter(tmp, "/");
		}
		return url;
	}

}