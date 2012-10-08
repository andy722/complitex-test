package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.TaskState;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Stores enum values as uppercase.
 *
 * @author andy
 */
public class TaskStateTypeHandler extends EnumTypeHandler<TaskState> {

    public TaskStateTypeHandler(Class<TaskState> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, TaskState parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.name().toUpperCase());
        } else {
            ps.setObject(i, parameter.name().toUpperCase(), jdbcType.TYPE_CODE);
        }
    }

}
