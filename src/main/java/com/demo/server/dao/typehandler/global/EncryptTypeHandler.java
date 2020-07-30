package com.demo.server.dao.typehandler.global;

import com.demo.server.bean.base.EncryptStr;
import com.demo.server.common.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(EncryptStr.class)
@Slf4j
public class EncryptTypeHandler extends BaseTypeHandler<EncryptStr> {
    /**
     * 全局 TypeHandler
     * <p>
     * application.properties中type-handlers-package为全局加载，会自动匹配Java类型进行映射
     * <p>
     * 本例会映射 EncryptTypeHandler 类型, 完成加解密
     */
    public EncryptTypeHandler() {
        log.info(">>> Mybatis global TypeHandler -> EncryptTypeHandler init. ");
    }

    String password = "fdb2ae47d2505be9";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EncryptStr parameter, JdbcType jdbcType)
            throws SQLException {
        String columnValue = AesUtil.encrypt(parameter.getValue(), password);
        ps.setString(i, columnValue);
    }

    @Override
    public EncryptStr getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        EncryptStr encryptStr = new EncryptStr(AesUtil.decrypt(columnValue, password));
        encryptStr.setEncrypt(columnValue);
        return encryptStr;
    }

    @Override
    public EncryptStr getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        EncryptStr encryptStr = new EncryptStr(AesUtil.decrypt(columnValue, password));
        encryptStr.setEncrypt(columnValue);
        return encryptStr;
    }

    @Override
    public EncryptStr getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        EncryptStr encryptStr = new EncryptStr(AesUtil.decrypt(columnValue, password));
        encryptStr.setEncrypt(columnValue);
        return encryptStr;
    }

}
