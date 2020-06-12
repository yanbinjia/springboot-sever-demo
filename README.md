# springboot-sever-demo
Web server application demo with springboot and some common functions.  
It is a simple work,just put the pieces together for developing faster and personal study.  
(Main version: Spring Boot 2.1.4, JDK 1.8)  

## 工程结构
- bean: entity,vo,dto等java bean
- common: 通用工具类,异常,常量等
- config: spring web config 和一些应用配置
- dao: 数据访问层
- interceptor: 拦截器相关实现
- manager: 通用业务处理层: 三方封装、Service通用能力下沉、组合dao操作 (来源于Alibaba《Java开发手册》)
- service: 具体业务逻辑服务
- web: Controller

## 集成功能
- [logback](http://logback.qos.ch/)
- [mybatis-3](https://mybatis.org/mybatis-3/)
- [tk.mybatis, 通用Mapper](https://github.com/abel533/Mapper )
- [Mybatis-PageHelper, Mybatis通用分页插件](https://github.com/pagehelper/Mybatis-PageHelper)
- [druid连接池](https://github.com/alibaba/druid)
- spring schedule, quartz
- [本地缓存 caffeine cache](https://github.com/ben-manes/caffeine)
- 基于HandlerInterceptorAdapter和jwt，实现token签发和验证(access&refresh)
- 基于HandlerInterceptorAdapter，实现接口参数签名和验证
- 基于@ControllerAdvice，异常处理和日志打印，Controller请求和响应日志打印
- RateLimitAspect, 基于guava RateLimiter接口限流，Controller方法注解 @RateLimit(qps = 50)
- 工具封装:EnumUtil、HashUtil、RandomUtil、RequestUtil、DateUtil、Base64Util等
- 好用工具:guava、commons-lang3、commons-collections4、commons-codec、jsoup、lombok






