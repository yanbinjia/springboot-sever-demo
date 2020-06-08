package com.demo.server.bean.base;

import lombok.Data;

@Data
public class Pager {
	private Integer pageSize = 20;
	private Integer pageNum = 0;
}
