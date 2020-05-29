package com.demo.server.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.stat.DruidStatManagerFacade;

@RestController
public class DruidStatController {
	@GetMapping("/druid/stat")
	public Object druidStat() {
		return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
	}
}
