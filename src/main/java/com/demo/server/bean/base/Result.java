package com.demo.server.bean.base;

public class Result<T> {

    // 返回数据对象
    private T data;

    // 返回状态码
    private int code;

    // 返回消息
    private String msg;

    // 服务器unix时间戳(秒)
    private long timestamp = System.currentTimeMillis() / 1000;

    // timestamp 可读时间
    // private String timestamp = DateUtil.getCurrentDateTimeStr();

    public Result() {
    }

    public Result(ResultCode responseCode) {
        super();
        this.code = responseCode.code;
        this.msg = responseCode.msg;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(T data) {
        this.data = data;
        this.code = ResultCode.SUCCESS.code;
        this.msg = ResultCode.SUCCESS.msg;
    }

    public Result(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public void setResultCode(ResultCode responseCode) {
        this.code = responseCode.code;
        this.msg = responseCode.msg;
    }

    public void setExtMsg(String extMsg) {
        this.msg += "(" + extMsg + ")";
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
