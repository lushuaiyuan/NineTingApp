package com.zzti.lsy.ninetingapp.utils;

import com.google.gson.Gson;

/**
 * 解析json的工具类
 * 
 * @author lsy
 *
 */
public class ParseUtils {
	private static Gson gson;

	public static <T> T parseJson(String json, Class<T> clazz) {
		if (gson == null) {
			gson = new Gson();
		}
		T t = gson.fromJson(json, clazz);
		return t;
	}
}
