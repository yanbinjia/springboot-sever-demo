package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.vo.ValidateTestParam;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test/validate")
@Validated// 在@RequestParam上开启validate能力
public class ValidateTestController {
    @TokenPass
    @SignPass
    @GetMapping("/test")
    @ResponseBody
    public Result<ValidateTestParam> test(@Valid @RequestBody ValidateTestParam validateTestParam) {
        Result<ValidateTestParam> result = new Result<>(ResultCode.SUCCESS);
        result.setData(validateTestParam);
        return result;
    }

    /**
     * 验证不通过抛出org.springframework.web.bind.MethodArgumentNotValidException异常.
     * ExceptionAdvice统一异常处理
     * <p>
     * 测试数据
     * {"userId":"12222","email":"ssss@ssss.com","phone":"13466799355","idCard":"120224198810025212","inSchoolDate":"2020-10-10 10:12:12"}
     * <p>
     * curl --location --request GET 'http://127.0.0.1:6673/validate/test' \
     * --header 'Content-Type: application/json' \
     * --header 'Cookie: JSESSIONID=91F81B31739E7E1C17387701184133B5' \
     * --data-raw '{"userId":"12222","email":"ssss@ssss.com","phone":"13466799355","idCard":"120224198810025212","inSchoolDate":"2020-10-10 10:12:12"}'
     */

    @TokenPass
    @SignPass
    @GetMapping("/test2")
    @ResponseBody
    public Result<ValidateTestParam> test2(@Range(min = 1, max = 99, message = "只能从1-99")
                                           @RequestParam(name = "id", required = true) int id,
                                           @Length(min = 2, max = 10, message = "长度必须>2且<10")
                                           @NotEmpty(message = "不能为空")
                                           @RequestParam String name) {
        Result<ValidateTestParam> result = new Result<>(ResultCode.SUCCESS);
        return result;
    }
    /**
     * Controller上加@Validated注解, 在@RequestParam上开启validate能力.
     * 验证不通过时,抛出ConstraintViolationException异常,
     * ExceptionAdvice统一异常处理
     *
     * 测试数据
     * curl --location --request GET 'http://127.0.0.1:6673/validate/test2?id=12345&name=1a22e232e3ew3' \
     * --header 'Cookie: JSESSIONID=91F81B31739E7E1C17387701184133B5'
     *
     */
}
