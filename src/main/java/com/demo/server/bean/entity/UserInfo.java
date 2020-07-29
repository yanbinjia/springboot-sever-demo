package com.demo.server.bean.entity;

import com.demo.server.bean.base.EncryptStr;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 通用Mapper中，默认是将实体类字段按照驼峰转下划线形式的表名列名进行转换。
 * <p>
 * 例如: 实体类的userName映射到表的user_name字段上。
 * <p>
 * 通用Mapper中，可以使用@Table，@Column配置映射物理表和实体类映射关系，如不配置按默认规则映射(驼峰-下划线)。
 * <p>
 * 文档 https://github.com/abel533/Mapper/wiki/2.2-mapping
 */

@Table(name = "user_info")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "user_name")
    private String userName;

    @NotBlank
    @Column(name = "password")
    private String password;

    private String email;

    private String mobile;

    @Column(name = "mobile_x")
    private EncryptStr mobileX;

    private Integer status;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Transient // @Transient, 非数据库表中字段
    private String otherInfo;

}
