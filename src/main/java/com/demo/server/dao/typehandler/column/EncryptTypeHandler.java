package com.demo.server.dao.typehandler.column;

import com.demo.server.common.utils.AesUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptTypeHandler extends BaseTypeHandler<String> {

    /**
     * 1.select:<br/>
     * <resultMap><result typeHandler="com.x.EncryptTypeHandler"/></resultMap><br/>
     * resultMap 字段映射上配置 typeHandler 属性，针对该字段进行处理<br/>
     * 2.where/insert/update:<br/>
     * 在#{}中，指定#{javaField,typeHandler=com.x.EncryptTypeHandler},注意无引号
     */

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
