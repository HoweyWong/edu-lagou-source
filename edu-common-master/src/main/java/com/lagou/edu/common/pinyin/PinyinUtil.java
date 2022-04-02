package com.lagou.edu.common.pinyin;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 获取汉语文字的拼音
 * 
 * @author Everest
 * @version 1.0 23/07/2010
 * 
 */
public class PinyinUtil {

    /** 汉语拼音格式化工具类 */
    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    /**
     * 获取字符串内的所有汉字的汉语拼音
     * 
     * @param src
     * @return
     */
    public static String spell(String src) {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 小写拼音字母
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不加语调标识
        format.setVCharType(HanyuPinyinVCharType.WITH_V); // u:的声母替换为v

        StringBuffer sb = new StringBuffer();
        int strLength = src.length();
        try {
            for (int i = 0; i < strLength; i++) {
                // 对英文字母的处理：小写字母转换为大写，大写的直接返回
                char ch = src.charAt(i);
                if (ch >= 'a' && ch <= 'z')
                    sb.append((char) (ch - 'a' + 'A'));
                if (ch >= 'A' && ch <= 'Z')
                    sb.append(ch);
                // 对汉语的处理
                String[] arr = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (arr != null && arr.length > 0)
                    sb.append(arr[0]).append(" ");
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取字符串内的所有汉字的汉语拼音并大写每个字的首字母
     * 
     * @param src
     * @return
     */
    public static String spellWithTone(String src) {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);// 标声调
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);// u:的声母

        if (src == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                // 对英文字母的处理：小写字母转换为大写，大写的直接返回
                char ch = src.charAt(i);
                if (ch >= 'a' && ch <= 'z')
                    sb.append((char) (ch - 'a' + 'A'));
                if (ch >= 'A' && ch <= 'Z')
                    sb.append(ch);
                // 对汉语的处理
                String[] arr = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (arr == null || arr.length == 0) {
                    continue;
                }
                String s = arr[0];// 不管多音字,只取第一个
                char c = s.charAt(0);// 大写第一个字母
                String pinyin = String.valueOf(c).toUpperCase().concat(s.substring(1));
                sb.append(pinyin).append(" ");
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字符串内的所有汉字的汉语拼音并大写每个字的首字母
     * 
     * @param src
     * @return
     */
    public static String spellNoneTone(String src) {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 标声调
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);// u:的声母

        if (src == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                // 对英文字母的处理：小写字母转换为大写，大写的直接返回
                char ch = src.charAt(i);
                if (ch >= 'a' && ch <= 'z')
                    sb.append((char) (ch - 'a' + 'A'));
                if (ch >= 'A' && ch <= 'Z')
                    sb.append(ch);
                // 对汉语的处理
                String[] arr = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (arr == null || arr.length == 0) {
                    continue;
                }
                String s = arr[0];// 不管多音字,只取第一个
                char c = s.charAt(0);// 大写第一个字母
                String pinyin = String.valueOf(c).toUpperCase().concat(s.substring(1));
                sb.append(pinyin).append("");
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取汉语第一个字的首英文字母
     * 
     * @param src
     * @return
     */
    public static String getTerm(String src) {
        String res = spell(src);
        if (res != null && res.length() > 0) {
            return res.toUpperCase().charAt(0) + "";
        } else {
            return "OT";
        }
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     * 
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        if (chines == null)
            return "";
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] s = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (s != null) {
                        if (StringUtils.isNotBlank(s[0]))
                            pinyinName += s[0].charAt(0);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<String> aa = new ArrayList<String>();
        List<String> bb = new ArrayList<String>();
        aa.add("aa");
        aa.add("bb");
        aa.add("cc");
        aa.add("dd");

        bb.add("cc");
        bb.add("bb");

        aa.remove(bb);
        for (String kk : aa) {
            System.out.println(kk);
        }

        // System.out.println("---------------------spellWithTone");
        // System.out.println(spellWithTone("English"));
        // System.out.println(spellWithTone("有志者事竟成，阿斯顿佛"));
        // System.out.println(spellWithTone("中华人民共和国"));
        // System.out.println(spellWithTone("单丽丽"));

        //        System.out.println("-----------------------------spell");
        //        System.out.println(spell("English"));
        //        System.out.println(spell("有志者事竟成，阿斯顿佛"));
        //        System.out.println(spell("中华人民共和国"));
        //        System.out.println(spell("音乐"));
        System.out.println(converterToFirstSpell("邹少云。!s？"));

        //    	converterToFirstSpell("我没空");

        System.out.println("----------------------spellNoneTone");
        System.out.println(spellNoneTone("English"));
        System.out.println(spellNoneTone("有志者事竟成，阿斯顿佛"));
        System.out.println(spellNoneTone("中华人民共和国"));
        System.out.println(spellNoneTone("单丽丽13"));

        System.out.println("---------------------------getTerm");
        //         System.out.println(getTerm("English"));
        //         System.out.println(getTerm("有志者事竟成，阿斯顿佛"));
        //         System.out.println(getTerm("中华人民共和国"));
        //         System.out.println(getTerm("单丽丽"));

    }

    /**
     * 获取字符串内的所有汉字的汉语拼音
     * 
     * @param src
     * @return
     */
    public static String spellPinyin(String src) {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 标声调
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);// u:的声母

        if (src == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                // 对英文字母的处理：小写字母转换为大写，大写的直接返回
                char ch = src.charAt(i);
                //                if((ch>='a'&&ch<='z')
                //                		|| (ch>='A'&&ch<='Z')){
                //                	sb.append(ch);
                //                }
                // 对汉语的处理
                String[] arr = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (arr == null || arr.length == 0) {
                    sb.append(ch);
                    continue;
                }
                String s = arr[0];// 不管多音字,只取第一个
                char c = s.charAt(0);// 大写第一个字母
                String pinyin = String.valueOf(c).concat(s.substring(1));
                sb.append(pinyin).append("");
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return null;
    }
}
