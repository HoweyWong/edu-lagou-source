package com.lagou.edu.common.string;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 身份证号、银行卡号
 */
public final class ValidateUtil {

    //验证银行卡号格式
    public static boolean checkBankCard(String bankNum) {
        char bit = getCheckBank(bankNum.substring(0, bankNum.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankNum.charAt(bankNum.length() - 1) == bit;
    }


    //银行卡卡号采用Luhm校验算法获得校验位
    private static char getCheckBank(String checkBankNum) {
        if (checkBankNum == null || checkBankNum.length() == 0
                || !checkBankNum.matches("\\d+")) {
            return 'N';
        }
        char[] ch = checkBankNum.trim().toCharArray();
        int luhmSum = 0;
        for (int i = ch.length - 1, j = 0; i >= 0; i--, j++) {
            int k = ch[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    //验证身份证号格式
    public static boolean checkIdCard(String idCard) {
        idCard = idCard.toLowerCase();
        boolean flag = true;
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        //身份证号长度为15或18位
        if (idCard.length() != 15 && idCard.length() != 18) {
            flag = false;
            return flag;
        }
        //除最后一位都为数字
        if (idCard.length() == 18) {
            Ai = idCard.substring(0, 17);
        } else {
            Ai = idCard.substring(0, 6) + "19" + idCard.substring(6, 15);
        }
        if (isNumber(Ai) == false) {
            flag = false;
            return flag;
        }

        //判断出生年月是否有效
        String year = Ai.substring(6, 10);//年
        String month = Ai.substring(10, 12);//月
        String day = Ai.substring(12, 14);//日
        if (isDateFormat(year + "-" + month + "-" + day) == false) {
            flag = false;
            return flag;
        }

        //判断最后一位的值
        int num = 0;
        for (int i = 0; i < 17; i++) {
            num = num + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = num % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        if (idCard.length() == 18) {
            if (Ai.equals(idCard) == false) {
                flag = false;
                return flag;
            }
        } else {
            return flag;
        }

        return flag;
    }

    //判断是否为数字
    private static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    //判断是否是日期格式YYYY-MM-DD
    private static boolean isDateFormat(String str) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher isNo = pattern.matcher(str);
        if (isNo.matches()) {
            return true;
        }
        return false;
    }

    //判断姓名是否为中文 (必须为中文,且大于一个字符)
    public static Boolean realNameIsChinese(String realName){

        if(StringUtils.isNotBlank(realName)){
            if(realName.length()<2){
                return false;
            }
            String regEx = "[\u4e00-\u9fa5]+";
            boolean isMatch =  Pattern.matches(regEx, realName);
            return isMatch;
        }else{
            return false;
        }

    }

    /**
     * 名称为汉字并且包含逗号
     * @param realName
     * @return
     */
    public static Boolean realNameIsChineseAndComma(String realName){

        if(StringUtils.isNotBlank(realName)){
            if(realName.length()<2){
                return false;
            }
            String regEx = "[\u4e00-\u9fa5]*[·]*[\u4e00-\u9fa5]+";
            boolean isMatch =  Pattern.matches(regEx, realName);
            return isMatch;
        }else{
            return false;
        }

    }

    //通过身份证号校验是否大于18岁
    public static Boolean idCardNoGreaterThanEighteen(String idCardNo){
        if(StringUtils.isNotBlank(idCardNo)&&idCardNo.length()>14){
            String subString = idCardNo.substring(6,14);
            String birthYear = subString.substring(0,4);
            String birthMonth = subString.substring(4,6);
            String birthDay = subString.substring(6,8);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String now = simpleDateFormat.format(calendar.getTime());
            String nowYear = now.substring(0,now.indexOf("-"));
            String nowMonth = now.substring(now.indexOf("-")+1,now.lastIndexOf("-"));
            String nowToday = now.substring(now.lastIndexOf("-")+1,now.length());

            if(Integer.parseInt(nowYear)-Integer.parseInt(birthYear)<18){
                //小于18 岁
                return false;
            }else if(Integer.parseInt(nowYear)-Integer.parseInt(birthYear)==18){
                //如果等于 18  判断 月份 和日期
                if(Integer.parseInt(nowMonth)>Integer.parseInt(birthMonth)){
                    //如果大于 月份
                    return true;
                }else if(Integer.parseInt(nowMonth)<Integer.parseInt(birthMonth)){
                    //月份不够，不满 18 岁
                    return false;
                }else{
                    //出生月份与当前月份相等，判断日期
                    if(Integer.parseInt(nowToday)>=Integer.parseInt(birthDay)){
                        //大于或等于出生日期，
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                return true;
            }
        }else{
            return false;
        }
    }


}
