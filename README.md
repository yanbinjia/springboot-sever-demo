# springboot-sever-demo
Web server application demo with springboot and some common functions.  
It is a simple work,just put the pieces together for developing application faster.  
(Spring boot version:2.1.4.RELEASE; Jdk version: 1.8)  

## 工程结构
- bean: entity,vo,dto等java bean
- common: 通用工具:工具类,异常,常量等
- config: spring web config 和一些应用配置
- dao: 数据访问层，与底层DB数据交互
- interceptor: 拦截器以及相关实现
- manager: 通用业务处理层，三方封装、Service通用能力下沉、组合dao (来源于Alibaba《Java开发手册》)
- service: 相对具体的业务逻辑服务
- web: RestController

## 集成功能
- [logback](http://logback.qos.ch/)
- [mybatis-3](https://mybatis.org/mybatis-3/)
- [tk.mybatis, 通用Mapper](https://github.com/abel533/Mapper )
- [Mybatis-PageHelper, Mybatis通用分页插件](https://github.com/pagehelper/Mybatis-PageHelper)
- [druid连接池](https://github.com/alibaba/druid)
- schedule quartz
- [本地缓存 caffeine cache](https://github.com/ben-manes/caffeine)
- jwt token认证实现(access&refresh)
- 接口参数签名和验证实现
- 基于@ControllerAdvice，异常处理和日志打印，Controller请求和响应日志打印
- 常用工具类:EnumUtil、HashUtil、RandomUtil、RequestUtil、DateUtil、Base64Util等





