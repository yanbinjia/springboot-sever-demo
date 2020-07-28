package com.demo.server.service.base.security;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.util.HashUtil;
import com.demo.server.common.util.RequestUtil;
import com.demo.server.config.SignConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
@Slf4j
public class SignService {
    @Autowired
    SignConfig signConfig;

    @PostConstruct // 初始化执行顺序:构造方法->@Autowired->@PostConstruct
    public void init() {
    }

    /**
     * [API接口防止参数篡改和重放攻击(Replay Attacks)]
     * <p>
     * 签名生成的通用步骤如下(参考了"微信支付V2版开发文档-安全规范")：
     * <p>
     * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
     * <p>
     * 注意规则：1.参数名ASCII码从小到大排序(字典序);2.如参数的值为空不参与签名;3.参数名区分大小写;4.传送的sign参数不参与签名;
     * <p>
     * 参数拼接：stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
     * <p>
     * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值。
     * <p>
     * 拼接密钥：stringSignTemp=stringA+"&key=192006250b4c09247ec02edce69f6a2d"
     * <p>
     * 计算签名：sign=MD5(stringSignTemp).toUpperCase()="9A0A8659F005D6984697E2CA0A9CF3B7"
     * <p>
     * !注意！
     * 1.本例采用固定秘钥，需把秘钥分发给客户端调用者，可能造成泄漏，更安全的做法是给每个用户生成秘钥(每个调用者分配appId和securetKey)，客户端在用户认证后获取秘钥
     * <p>
     * 2.在验证签名的服务中，可以结合redis缓存，处理幂等性拦截，控制重复访问
     * <p>
     * 3.nonce，随机字符串，不长于32位，主要用来保证签名不可预测，调用随机函数生成，例如:ibuaiVcKdpRxkhJA，可以根据需求在接口协议设计中使用
     *
     * @param request
     * @return
     */
    public Result<String> checkSign(HttpServletRequest request) {
        Result<String> result = new Result<>(ResultCode.SEC_SIGN_ERROR);

        String uri = request.getRequestURI();

        String signStr = request.getParameter("sign");
        String timestampStr = request.getParameter("timestamp");

        if (StringUtils.isAnyBlank(signStr, timestampStr) || !StringUtils.isNumeric(timestampStr)) {
            result.setResultCode(ResultCode.SEC_SIGN_ERROR);
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

        if (Math.abs(System.currentTimeMillis() - timestamp) > signConfig.getTimestampExpireSecs() * 1000L) {

            result.setData("时间戳超过" + signConfig.getTimestampExpireSecs() + "s");
            result.setResultCode(ResultCode.SEC_SIGN_EXPIRE);

            if (signConfig.isPrintCheckInfo()) {
                log.info("ChekSign Fail. sign={}, timestamp={}, expire over {}s.", "Fail", signStr, timestampStr,
                        signConfig.getTimestampExpireSecs());
            }

            return result;
        }

        // -----------------------------------------------------
        // 检查重放,
        // 使用redis, key=请求特征(URL&Param),expire设置参考signConfig.getTimestampExpireSecs()
        // ...

        // -----------------------------------------------------
        // 检查签名
        StringBuffer paramStrSb = new StringBuffer();
        String paramStrByServer = "";

        // 处理请求参数,对参数按照key=value的格式，并按照参数名ASCII字典序排序
        SortedMap<String, String> paramsSortedMap = new TreeMap<>(RequestUtil.getParameterMap(request));
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
        String signStrByServer = "";

        String algorithm = signConfig.getAlgorithm();
        algorithm = StringUtils.isBlank(algorithm) ? "md5" : algorithm;

        if (algorithm.equalsIgnoreCase("sha256")) {
            // SHA256
            signStrByServer = HashUtil.sha256(paramStrByServer).toUpperCase();
        } else {
            // 默认MD5
            signStrByServer = HashUtil.md5(paramStrByServer).toUpperCase();
        }

        // 验证签名并构造返回
        boolean signOk = signStr.equalsIgnoreCase(signStrByServer);

        if (signOk) {
            // 签名成功
            result.setResultCode(ResultCode.SUCCESS);
        } else {
            // 签名失败
            result.setResultCode(ResultCode.SEC_SIGN_ERROR);
        }

        if (signConfig.isPrintCheckInfo()) {
            log.info("ChekSign {}. URI:{}", signOk ? "Success" : "Fail", uri);
            log.info("ChekSign paramStrByServer:{}", paramStrByServer);
            log.info("ChekSign timestamp={},signStr={},signStrByServer={}", timestamp, signStr, signStrByServer);
        }

        return result;
    }
}
