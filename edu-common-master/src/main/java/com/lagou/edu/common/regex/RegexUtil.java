package com.lagou.edu.common.regex;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 正则表达式工具
 */
public class RegexUtil {

	/**
	 * 邮箱验证
	 */
	public static final String EMAILREGULAR = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))$";
	/**
	 * 手机验证
	 */
	// public static final String PHONEREGULAR = "(^1[3,4,5,7,8]{1}[0-9]{9}$)";
	// public static final String PHONEREGULAR = "(^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$)";
	// public static final String PHONEREGULAR = "(^(0|86|17951)?((13[0-9]|15[012356789]|17[0135678]|18[0-9]|14[57])[0-9]{8})$)";	
	public static final String PHONEREGULAR = "(^(0|86|17951)?((13[0-9]|15[012356789]|16[2567]|17[012345678]|18[0-9]|14[01456789]|19[012356789])[0-9]{8})$)";
	
	/**
	 * 电话号码验证
	 */
	public static final String TELREGULAR = "(^[0-9]{3,4}\\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^[0-9]{3,4}\\-[0-9]{7,8}\\-[0-9]{3,5}$)|(^[0-9]{7,8}\\-[0-9]{3,5}$)|(^\\([0-9]{3,4}\\)[0-9]{7,8}$)|(^\\([0-9]{3,4}\\)[0-9]{7,8}\\-[0-9]{3,5}$)|(^1[3,4,5,7,8,9]{1}[0-9]{9}$)";

	private static final Pattern EMAIL_REGX_PATTERN = Pattern.compile(EMAILREGULAR, Pattern.DOTALL);
	private static final Pattern PHONE_REGX_PATTERN = Pattern.compile(PHONEREGULAR, Pattern.DOTALL);
	private static final Pattern TEL_REGX_PATTERN = Pattern.compile(TELREGULAR, Pattern.DOTALL);

	/**
	 * 判断是否是邮箱
	 * 
	 * @param email
	 *            邮箱
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		email = email.toLowerCase().trim();
		return isMatch(email, EMAIL_REGX_PATTERN);
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param phone
	 *            手机号
	 * @return
	 */
	public static boolean isPhone(String phone) {
		return isMatch(phone, PHONE_REGX_PATTERN);
	}

	/**
	 * 判断是否是电话号码
	 * 
	 * @param tel
	 *            电话号码
	 * @return
	 */
	public static boolean isTel(String tel) {
		return isMatch(tel, TEL_REGX_PATTERN);
	}

	public static boolean isMatch(String str, String regxStr) {
		Pattern regx = Pattern.compile(regxStr, Pattern.DOTALL);
		return isMatch(str, regx);
	}

	public static boolean isMatch(String str, Pattern regx) {
		Matcher matcher = regx.matcher(str);
		return matcher.matches();
	}
}