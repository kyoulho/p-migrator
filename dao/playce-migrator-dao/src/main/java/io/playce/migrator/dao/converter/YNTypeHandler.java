package io.playce.migrator.dao.converter;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YNTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter ? "Y":"N");
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String obj = rs.getString(columnName);
        if(obj == null) return false;
        return obj.equals("Y");
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String obj = rs.getString(columnIndex);
        if(obj == null) return false;
        return obj.equals("Y");
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String obj = cs.getString(columnIndex);
        if(obj == null) return false;
        return obj.equals("Y");
    }
}
