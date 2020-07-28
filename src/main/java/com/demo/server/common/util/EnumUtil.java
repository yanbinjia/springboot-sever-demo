package com.demo.server.common.util;

import com.demo.server.bean.enums.UserDeleted;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Method;

public class EnumUtil {

    public static <E extends Enum<E>> E getByCode(Class<E> enumClass, String getCodeMethodName, Object code) {
        E result = null;
        try {
            E[] arr = enumClass.getEnumConstants();
            Method targetMethod = enumClass.getDeclaredMethod(getCodeMethodName);
            Object typeNameVal = null;
            for (E entity : arr) {
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

    public static <E extends Enum<E>> E getByCode(Class<E> enumClass, Object code) {
        return EnumUtil.getByCode(enumClass, "getCode", code);
    }

    public static <E extends Enum<E>> E getByName(final Class<E> enumClass, final String enumName) {
        if (enumName == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, enumName);
        } catch (final IllegalArgumentException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        UserDeleted enumObj = EnumUtil.getByCode(UserDeleted.class, 1);
        System.out.println(enumObj.getCode() + ":" + enumObj.getMsg());

        enumObj = EnumUtil.getByCode(UserDeleted.class, 0);
        System.out.println(enumObj.getCode() + ":" + enumObj.getMsg());

        enumObj = EnumUtil.getByName(UserDeleted.class, "DELETED");
        System.out.println(enumObj.getCode() + ":" + enumObj.getMsg());

        System.out.println(EnumUtils.getEnumMap(UserDeleted.class).toString());

    }
}
