package com.demo.server.web.druid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.demo.server.interceptor.SignPass;
import com.demo.server.interceptor.TokenPass;

@RestController
@RequestMapping("/druid")
public class DruidStatController {
	@SignPass
	@TokenPass
	@GetMapping("/stat")
	public Object druidStat() {
		return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
	}
}
