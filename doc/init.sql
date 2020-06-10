-- user_info definition
CREATE TABLE `user_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL DEFAULT '' COMMENT '用户账号,用于登录,唯一键',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码MD5/SHA-256',
  `email` varchar(50) DEFAULT '',
  `mobile` varchar(20) DEFAULT '',
  `status` smallint(6) NOT NULL DEFAULT '1' COMMENT '状态:0禁用;1有效',
  `deleted` smallint(6) NOT NULL DEFAULT '0' COMMENT '0正常:1删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_un` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;