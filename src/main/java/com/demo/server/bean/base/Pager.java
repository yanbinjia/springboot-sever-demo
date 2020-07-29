package com.demo.server.bean.base;

import lombok.Data;

/**
 * 查询参数类继承Pager，Pager为分页参数基础类
 */
@Data
public class Pager {
    private Integer pageSize = 20;
    private Integer pageNum = 1;
}
