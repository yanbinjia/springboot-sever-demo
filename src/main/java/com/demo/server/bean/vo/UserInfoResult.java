package com.demo.server.bean.vo;

import com.demo.server.bean.entity.UserInfo;
import lombok.Data;

@Data
public class UserInfoResult extends UserInfo {
    // 状态(名称)
    private String statusName;
    // 删除(名称)
    private String deletedName;
    // groupName
    private String groupName;

}
