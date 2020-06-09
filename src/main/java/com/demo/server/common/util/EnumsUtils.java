package com.demo.server.common.util;

import java.lang.reflect.Method;

import com.demo.server.bean.enums.UserDeleted;

public class EnumsUtils {

	public static <T extends Enum<T>> T getByCode(Class<T> clazz, String getTypeNameMethodName, Object code) {
		T result = null;
		try {
			T[] arr = clazz.getEnumConstants();
			Method targetMethod = clazz.getDeclaredMethod(getTypeNameMethodName);
			Object typeNameVal = null;
			for (T entity : arr) {
				typeNameVal = targetMethod.invoke(entity);
				if (typeNameVal.equals(code)) {
					result = entity;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T extends Enum<T>> T getByCode(Class<T> clazz, Object code) {
		return EnumsUtils.getByCode(clazz, "getCode", code);
	}

	public static void main(String[] args) {
		UserDeleted enumObj = EnumsUtils.getByCode(UserDeleted.class, 1);
		System.out.println(enumObj.getCode() + ":" + enumObj.getMsg());

		enumObj = EnumsUtils.getByCode(UserDeleted.class, 0);
		System.out.println(enumObj.getCode() + ":" + enumObj.getMsg());

	}
}
