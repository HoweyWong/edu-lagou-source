package com.lagou.edu.common.string;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.lagou.edu.common.date.DateUtil;

public class GsonUtil {

	private static final GsonBuilder GSON_BUILDER = new GsonBuilder().disableHtmlEscaping().setLongSerializationPolicy(LongSerializationPolicy.STRING)
			.setDateFormat(DateUtil.SDF_YYYY_MM_DD_HH_MM_SS);
	private static final Gson gson = GSON_BUILDER.create();

	public static String toJson(Object o) {
		return gson.toJson(o);
	}

	public static String toJson(Object o, String dateFormat) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setLongSerializationPolicy(LongSerializationPolicy.STRING).setDateFormat(dateFormat)
				.create();
		return gson.toJson(o);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}

	public static Gson getGson() {
		return gson;
	}
}