/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T15:40:58.733+08:00
 */

package com.demo.server.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class ValidateTestParam {
    @Length(min = 3, max = 40, message = "长度必须>3且<40")
    @NotEmpty(message = "不能为空") //NotEmpty 字符串，不为空字符串
    private String userId;

    @NotNull(message = "email不能为空")
    @Email(message = "email地址无效")
    private String email;

    @NotEmpty(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$", message = "请输入正确的身份证号")
    private String idCard;

    @NotEmpty(message = "手机号码不能为空")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "请输入正确的手机号码")
    private String phone;


    @NotNull(message = "请输入合法的时间")
    @Future(message = "请输入未来的时间")//Future是否是未来时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inSchoolDate;


    /**
     * Hibernate Validator Documentation
     * http://hibernate.org/validator/documentation/
     *
     * 常用注解:
     * @AssertFalse 验证注解的元素值是 false
     * @AssertTrue 验证注解的元素值是 true
     * @DecimalMax（value=x） 验证注解的元素值小于等于指定的十进制value 值
     * @DecimalMin（value=x） 验证注解的元素值大于等于指定的十进制value 值
     * @Digits(integer=整数位数, fraction=小数位数)验证注解的元素值的整数位数和小数位数上限
     * @Future 验证注解的元素值（日期类型）比当前时间晚
     * @Max（value=x） 验证注解的元素值小于等于指定的 value值
     * @Min（value=x） 验证注解的元素值大于等于指定的 value值
     * @NotNull 验证注解的元素值不是 null
     * @Null 验证注解的元素值是 null
     * @Past 验证注解的元素值（日期类型）比当前时间早
     * @Pattern(regex=正则表达式) 验证注解的元素值不指定的正则表达式匹配
     * @Size(min=最小值, max=最大值) 验证注解的元素值的在 min 和 max （包含）指定区间之内，如字符长度、集合大小
     * @Valid 该注解主要用于字段为一个包含其他对象的集合或map或数组的字段，或该字段直接为一个其他对象的引用，这样在检查当前对象的同时也会检查该字段所引用的对象。
     * @NotEmpty 验证注解的元素值不为 null 且不为空（字符串长度不为 0、集合大小不为 0）
     * @Range(min=最小值, max=最大值)验证注解的元素值在最小值和最大值之间
     * @NotBlank 验证注解的元素值不为空（不为 null、去
     * 除首位空格后长度为 0），不同于@NotEmpty， @NotBlank 只应用于字符串且在比较时会去除字符串的空格
     * @Length(min=下限, max=上限) 验证注解的元素值长度在 min 和 max 区间内
     * @Email 验证注解的元素值是 Email，也可以通过正则表达式和 flag 指定自定义的 email 格式
     */
}
