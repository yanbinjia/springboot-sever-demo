package com.demo.server.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.AliasFor;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
	@AliasFor("qps")
	long permitsPerSecond() default 5000;

	@AliasFor("permitsPerSecond")
	long qps() default 5000;

	long timeout() default 20;

	// 处理特殊的uri,或者有其他需要特殊标记的情况,如uri中有参数的情况,/user/{id}，配置 uri="/user"
	String uri() default "";

	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

	String errMsg() default "请求频率超限.";

}
