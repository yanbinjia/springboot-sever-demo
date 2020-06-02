package com.demo.server.service.security;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.server.bean.response.Result;
import com.demo.server.bean.response.ResultCode;
import com.demo.server.common.util.HashUtil;
import com.demo.server.common.util.RequestUtil;
import com.demo.server.config.SignConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignService {
	@Autowired
	SignConfig signConfig;

	/**
	 * 
	 * [API接口防止参数篡改和重放攻击(Replay Attacks)]
	 * 
	 * 签名生成的通用步骤如下(参考自微信支付开发文档)：
	 * 
	 * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
	 * 
	 * 注意规则：1.参数名ASCII码从小到大排序(字典序);2.如参数的值为空不参与签名;3.参数名区分大小写;4.传送的sign参数不参与签名;
	 * 
	 * 参数拼接：stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
	 * 
	 * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值。
	 * 
	 * 拼接密钥：stringSignTemp=stringA+"&key=192006250b4c09247ec02edce69f6a2d"
	 * 
	 * 计算签名：sign=MD5(stringSignTemp).toUpperCase()="9A0A8659F005D6984697E2CA0A9CF3B7"
	 * 
	 * 
	 * 1.!注意！ 本例采用固定秘钥，需要把秘钥分发给客户端调用者，可能造成泄漏，更安全的做法是给每个用户生成秘钥，客户端在用户认证后获取秘钥
	 * 
	 * 2.!注意！ 在验证签名的服务中，可以结合redis缓存，处理幂等性拦截，控制重复访问
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public Result<String> checkSign(HttpServletRequest request) {
		Result<String> result = new Result<>(-1, "");

		String signStr = request.getParameter("sign");
		String timestampStr = request.getParameter("timestamp");

		if (StringUtils.isBlank(signStr) || StringUtils.isBlank(timestampStr) || !StringUtils.isNumeric(timestampStr)) {
			result.setData("");
			result.setCode(ResultCode.SEC_SIGN_ERROR.code);
			result.setMsg(ResultCode.SEC_SIGN_ERROR.msg);
			return result;
		}

		// -----------------------------------------------------
		// 检查时间戳
		long timestamp = 0L;
		try {
			timestamp = Long.valueOf(timestampStr);
		} catch (Exception e) {
			log.error("", e);
		}

		if (Math.abs(System.currentTimeMillis() - timestamp) > signConfig.getTimestampExpireSecs() * 1000) {
			result.setData("请求时间戳超过" + signConfig.getTimestampExpireSecs() + "s");
			result.setCode(ResultCode.SEC_SIGN_EXPIRE.code);
			result.setMsg(ResultCode.SEC_SIGN_EXPIRE.msg);

			if (signConfig.isPrintCheckInfo()) {
				log.info("ChekSign [{}]. timestamp[{}] expire. ", "Fail", timestamp);
			}

			return result;
		}

		// -----------------------------------------------------
		// 检查签名
		StringBuffer paramStrSb = new StringBuffer();
		String paramStrByServer = "";

		// 处理请求参数,对参数按照key=value的格式，并按照参数名ASCII字典序排序
		SortedMap<String, String> paramsSortedMap = new TreeMap<>(RequestUtil.getHttpParamter(request));
		if (paramsSortedMap != null) {
			paramsSortedMap.remove("sign");
			paramsSortedMap.forEach((k, v) -> {
				if (paramStrSb.length() == 0) {
					paramStrSb.append(k + "=" + v);
				} else {
					paramStrSb.append("&" + k + "=" + v);
				}
			});
		}

		// 拼接API密钥
		paramStrSb.append("&key=" + signConfig.getSecretKey());

		// 服务端参数字符串
		paramStrByServer = paramStrSb.toString();

		// 服务端计算签名值
		String signStrByServer = HashUtil.md5(paramStrByServer).toUpperCase();

		// 验证签名并构造返回
		boolean signOk = signStr.equalsIgnoreCase(signStrByServer);

		if (signOk) {
			// 签名成功
			result.setData("");
			result.setCode(ResultCode.SUCCESS.code);
			result.setMsg(ResultCode.SUCCESS.msg);
		} else {
			// 签名失败
			result.setData("");
			result.setCode(ResultCode.SEC_SIGN_ERROR.code);
			result.setMsg(ResultCode.SEC_SIGN_ERROR.msg);
		}

		if (signConfig.isPrintCheckInfo()) {
			log.info("ChekSign [{}]", signOk ? "Success" : "Fail");
			log.info("ChekSign paramStrByServer {}", paramStrByServer);
			log.info("ChekSign timestamp=[{}],signStr=[{}],signStrByServer=[{}]", timestamp, signStr, signStrByServer);
		}

		return result;
	}
}
