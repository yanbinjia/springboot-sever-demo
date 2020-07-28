package com.demo.server.bean.vo;

import lombok.Data;

@Data
public class Token {
    public String userId;
    public String accessToken;
    public String refreshToken;
}
