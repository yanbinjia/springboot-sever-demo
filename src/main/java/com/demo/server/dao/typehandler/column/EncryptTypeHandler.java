package com.demo.server.dao.typehandler.column;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.demo.server.common.util.AesUtil;

public class EncryptTypeHandler extends BaseTypeHandler<String> {
	// xml写法: typeHandler="com.x.typehandler.column.EncryptTypeHandler"
	// sql写法: #{mobile, typeHandler=com.x.typehandler.column.EncryptTypeHandler}
	// resultMap 字段映射上配置 typeHandler 属性，针对该字段进行处理
	// column 的 typeHandler，比较灵活，建议字段加解密使用

	String password = "fdb2ae47d2505be9";

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		String encrypt = AesUtil.encrypt(parameter, password);
		ps.setString(i, encrypt);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {

		String value = rs.getString(columnName);
		String decrypt = AesUtil.decrypt(value, password);

		return decrypt;
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return AesUtil.decrypt(rs.getString(columnIndex), password);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return AesUtil.decrypt(cs.getString(columnIndex), password);
	}

}
