package com.demo.server.bean.base;

import lombok.Data;

@Data
public class EncryptStr {
    String value = "";
    String encrypt = "";

    public EncryptStr(String value) {
        this.value = value;
    }

}
