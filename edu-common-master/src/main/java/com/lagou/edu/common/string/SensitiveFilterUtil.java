package com.lagou.edu.common.string;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SensitiveFilterUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SensitiveFilterUtil.class);
	
	private static final SensitiveFilterUtil filter = new SensitiveFilterUtil();
	
	private static final String SENSITIVE_PATH = "sensitive.properties";
	
	private SensitiveFilterUtil() {
		initFilterCode();
	}

	public static SensitiveFilterUtil getInstance(){
		return filter;
	}
	
	/** 直接禁止的 */
	private HashMap keysMap = new HashMap(2048, 0.6f);

	public void addKeywords(List<String> keywords) {
		for (int i = 0; i < keywords.size(); i++) {
			String key = keywords.get(i).trim();
			HashMap nowhash = null;
			nowhash = keysMap;
			for (int j = 0; j < key.length(); j++) {
				char word = key.charAt(j);
				Object wordMap = nowhash.get(word);
				if (wordMap != null) {
					nowhash = (HashMap) wordMap;
				} else {
					HashMap<String, String> newWordHash = new HashMap<String, String>();
					newWordHash.put("isEnd", "0");
					nowhash.put(word, newWordHash);
					nowhash = newWordHash;
				}
				if (j == key.length() - 1) {
					nowhash.put("isEnd", "1");
				}
			}
		}
	}

	/**
	 * 检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
	 * flag 1:最小长度匹配 2：最大长度匹配
	 */
	private int checkKeyWords(String txt, int begin, MatchType matchType) {
		HashMap nowhash = null;
		nowhash = keysMap;
		int maxMatchRes = 0;
		int res = 0;
		int l = txt.length();
		char word = 0;
		for (int i = begin; i < l; i++) {
			word = txt.charAt(i);
			Object wordMap = nowhash.get(word);
			if (wordMap != null) {
				res++;
				nowhash = (HashMap) wordMap;
				if (((String) nowhash.get("isEnd")).equals("1")) {
					if (MatchType.MIN_LENGTH_MATCH.equals(matchType)) {
						wordMap = null;
						nowhash = null;
						txt = null;
						return res;
					} else {
						maxMatchRes = res;
					}
				}
			} else {
				txt = null;
				nowhash = null;
				return maxMatchRes;
			}
		}
		txt = null;
		nowhash = null;
		return maxMatchRes;
	}
	
	/** 过滤敏感词，默认用'*'**/
	public String doFliter(String text){
		return doFliter(text, '*');
	}
	
	/** 过滤敏感词,用replace替换 **/
	public String doFliter(String text, char replace) {
		if(StringUtils.isBlank(text)){
			return "";
		}
		int l = text.length();
		char[] textArray = text.toCharArray();
		for (int i = 0; i < l;) {
			int len = checkKeyWords(text, i, MatchType.MAX_LENGTH_MATCH);
			if (len > 0) {
				for (int j = 0; j < len; j++) {
					textArray[i + j] = replace;
				}
				i += len;
			} else {
				i++;
			}
		}
		text = null;
		return new String(textArray);
	}

	/**
	 * 返回text中关键字的列表
	 */
	public List<String> getTxtKeyWords(String text) {
		List<String> list = new ArrayList<String>();
		int l = text.length();
		for (int i = 0; i < l;) {
			int len = checkKeyWords(text, i, MatchType.MAX_LENGTH_MATCH);
			if (len > 0) {
				String tt = "<font color='#ff0000'>" + text.substring(i, i + len) + "</font>";
				list.add(tt);
				i += len;
			} else {
				i++;
			}
		}
		text = null;
		return list;
	}

	/**
	 * 仅判断text中是否有关键字
	 */
	public boolean isContentKeyWords(String text) {
		for (int i = 0; i < text.length(); i++) {
			int len = checkKeyWords(text, i, MatchType.MIN_LENGTH_MATCH);
			if (len > 0) {
				return true;
			}
		}
		text = null;
		return false;
	}
	
	/**
	 * 初始化敏感词列表
	 */
	private void initFilterCode() {
		List<String> keywords = new ArrayList<String>();

		InputStream in = SensitiveFilterUtil.class.getClassLoader().getResourceAsStream("sensitive.properties");
		Properties pro = new Properties();
		try {
			if(in == null){
				logger.debug("敏感词配置文件不存在：{} ", SENSITIVE_PATH);
				return;
			}
			pro.load(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.warn("加载敏感词配置文件 {} , 出错 ： {}", SENSITIVE_PATH, e1);
		}
		Enumeration<String> enu = (Enumeration<String>) pro.propertyNames();
		while (enu.hasMoreElements()) {
			try {
				String dd = (String) enu.nextElement();
				dd = new String(dd.getBytes("UTF-8"), "UTF-8");
				keywords.add(dd);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.warn("加载敏感词配置文件 {} , 出错 ： {}", SENSITIVE_PATH, e);
			}
		}
		logger.info("敏感词配置文件加载结束， 共加载 {} 个词", keywords.size());
		addKeywords(keywords);
	}

	enum MatchType{
		MIN_LENGTH_MATCH, MAX_LENGTH_MATCH
	}
}