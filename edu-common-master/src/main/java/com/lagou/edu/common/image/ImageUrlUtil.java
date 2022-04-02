package com.lagou.edu.common.image;

/**
 * 如果图片路径没有前缀则添加前缀，有则直接返回
 * @Description:
 *
 * @author antony
 * @Date 2016-1-23 下午6:35:52
 */
public class ImageUrlUtil {

	/** 图片前缀 */
	private static final String IMAGE_PREFIX = "pic/";

	/** 前缀长度 */
	private static int IMAGE_PREFIX_LENGTH = IMAGE_PREFIX.length();

	private static String[] exchangeUrlPrefix = {"image1","image2"};

	/**
	 * 给image1和 image2开头的图片url添加前缀。前缀为：pic/<br>
	 * 如果有则不添加，如果没有则会添加前缀
	 * @param imageUrl
	 * @return
	 * @author antony
	 * @Date 2016-1-23 下午6:41:41
	 */
	public static String addPrefix(String imageUrl){
		//判断绝对路径还是相对路径
		if("http".equals(imageUrl.substring(0,4))){
			return _absoluteUrl(imageUrl);
		}else{
			return _relativeUrl(imageUrl);
		}
	}

	protected static String _absoluteUrl(String imageUrl){
		String cutStr = "com/";
		int cutIndex = imageUrl.indexOf(cutStr) + cutStr.length();
		StringBuilder bu = new StringBuilder();
		bu.append(imageUrl.subSequence(0, cutIndex));
		bu.append(_relativeUrl(imageUrl.substring(cutIndex,imageUrl.length())));
		return bu.toString();
	}

	protected static String _relativeUrl(String imageUrl){
		if(isHaveToChange(imageUrl)){
			StringBuilder bu = new StringBuilder();
			if(IMAGE_PREFIX.equals(imageUrl.substring(0,IMAGE_PREFIX_LENGTH))){
				bu.append(imageUrl);
			}else{
				bu.append(IMAGE_PREFIX);
				bu.append(imageUrl);
			}
			return bu.toString();
		}else{
			return imageUrl;
		}
	}

	private static boolean isHaveToChange(String imageUrl){
		boolean isChange = false;
		for(String s : exchangeUrlPrefix){
			if(s.equals(imageUrl.substring(0,s.length()))){
				return true;
			}
		}
		return isChange;
	}

}
