package com.demo.server.bean.enums;

public enum UserStatus {
    NULL(-1, "未知"), //
    INVALID(0, "禁用"), //
    VALID(1, "有效");//

    private Integer code;
    private String msg;

    UserStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String findMsgByCode(Integer code) {
        String msg = "未知";
        for (UserStatus element : UserStatus.values()) {
            if (element.code == code) {
                msg = element.getMsg();
            }
        }
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
