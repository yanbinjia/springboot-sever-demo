package com.demo.server.web.druid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;

@RestController
@RequestMapping("/druidstat/")
public class DruidStatController {
	@SignPass
	@TokenPass
	@GetMapping("/ds")
	public Object druidStat() {
		return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
	}
}
