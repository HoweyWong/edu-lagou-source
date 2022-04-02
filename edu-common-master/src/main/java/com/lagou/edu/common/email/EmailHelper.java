package com.lagou.edu.common.email;

import com.lagou.edu.common.string.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Description: 邮件辅助 
 * @Author Cookie
 * @Date 2014年11月24日 下午2:08:03
 */
public class EmailHelper {

    private final static Logger logger = LoggerFactory.getLogger(EmailHelper.class);
    
    private final static String STR_AT = "@";

    private static final Properties mails = new Properties();
    private static Set<String> commonEmailSuffix = new HashSet<String>();
    private static final Properties common_email_suffix_properties = new Properties();

    static {
        InputStream mailStr = EmailHelper.class.getResourceAsStream("/mail.properties");
        InputStream common_email_suffix = EmailHelper.class.getResourceAsStream("/common_email_suffix.properties");
        try {
            mails.load(mailStr);
            common_email_suffix_properties.load(common_email_suffix);
            Set<Object> commonSuffixs = common_email_suffix_properties.keySet();
            if(commonSuffixs != null){
            	for (Object commonSuffix : commonSuffixs) {
            		commonEmailSuffix.add((String) commonSuffix);
    			}
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    //根据搜索词的KEY得到VALUE
    public static String mapping(String wordCode) {
        if (!StrUtils.isEmpty(wordCode)) {
            return mails.getProperty(wordCode);
        }
        return null;
    }

    /**
     * 根据邮箱后缀得到
     * @param suffix   如 @qq.com
     * @return
     */
    public static String getQuickLoginUrlBySuffix(String suffix) {
        if (!StrUtils.isEmpty(suffix)) {
            return mails.getProperty(suffix);
        }
        return null;
    }

    /**
     * 是否能快速登录url 
     * 例如XXXX@163.com 就可以直接登录网易的邮箱
     * 在mail.properties 里面的都可以快速登录
     */
    public static boolean canQuickLogin(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        String emailSuffix = getSuffixOfEmail(email);
        String commonEmail = getQuickLoginUrlBySuffix(emailSuffix);
        if (StringUtils.isBlank(commonEmail)) {
            return false;
        }
        return true;
    }

    /**
     * 获取快速登录的url
     * @param email
     * @return
     */
    public static String getQuickLoginUrl(String email) {
        if (!canQuickLogin(email)) {
            return StringUtils.EMPTY;
        }
        String emailSuffix = getSuffixOfEmail(email);
        return getQuickLoginUrlBySuffix(emailSuffix);
    }
    
    /**
     * 根据给定的邮箱获取邮箱后缀
	 * 以@开头
	 * 如果没有@，则返回原串
     * @param email
     * @return
     */
    public static String getSuffixOfEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return StringUtils.EMPTY;
        }
        int index = email.lastIndexOf(STR_AT);
        if (index == -1){
            return email;
        }
        return email.substring(index);
    }
    
    /**
     * 根据给定的邮箱获取邮箱前缀
     * 不含@
     * @param email
     * @return
     */
    public static String getPerfixOfEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return StringUtils.EMPTY;
        }
        int index = email.lastIndexOf(STR_AT);
        if (index == -1){
            return null;
        }
        return email.substring(0, index);
    }
    
    /**
     * 查询是否包含该host对应的记录
     * @return
     */
    public static boolean containsEmailHost(String host){
    	Enumeration<Object> elements = mails.elements();
    	if(elements == null || StringUtils.isBlank(host)){
    		return false;
    	}
    	while(elements.hasMoreElements()){
    		String key = (String) elements.nextElement();
    		if(key == null){
    			continue;
    		}
    		if(key.contains(host)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 普通邮箱后缀
     */
    public static Set<String> getCommonEmailSuffix() {
        return Collections.unmodifiableSet(commonEmailSuffix);
    }

    /**
     * 判断邮箱是否为普通邮箱
     * @param email email地址 vivi@lagou.com
     * @return 普通邮箱返回true，企业邮箱或参数不是邮箱则返回false
     * @author Oliver
     * @version 2015年2月26日 上午11:46:56
     */
    public static boolean isCommonEmail(String email) {
        String emailSuffix = getSuffixOfEmail(email);
        if (StringUtils.isBlank(emailSuffix)) {
            return false;
        }
        return commonEmailSuffix.contains(emailSuffix.toLowerCase());
    }
}
