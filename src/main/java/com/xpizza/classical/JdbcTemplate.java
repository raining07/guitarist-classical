package com.xpizza.classical;

import com.xpizza.bass.util.Assert;

import java.sql.*;
import java.util.*;

/**
 * 执行Jdbc操作
 */
public class JdbcTemplate {

    /**
     * 根据SQL查询，返回list
     * @Title: queryForList
     * @Description: 把resultset转list
     * @param sql
     * @return
     * @return: List<Map<String,Object>>
     */
    public List<Map<String, Object>> queryForList(String sql) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        try {
            connection = DBUtil.getConnection();
            stmt = connection.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
            list = new ArrayList<>();
            Map<String, Object> rowData = new HashMap<>();
            while (rs.next()) {
                rowData = new HashMap<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    // key取小写
                    rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(rs, stmt, connection);
        }
        System.out.println("SQL:" + sql);
        return list;
    }

    /**
     * 查出全表数据
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> findTable(String tableName) {
        return queryForList("SELECT * FROM " + tableName);
    }

    /**
     * 执行SQL
     * @param sql
     */
    public void execute(String sql) {
        Connection connection = DBUtil.getConnection();
        Statement stmt = null;
        try {
            stmt = connection.getConnection().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(stmt, connection);
        }
    }

    /**
     * 执行SQL
     * @param sql
     */
    public void update(String sql) {
        Connection connection = DBUtil.getConnection();
        Statement stmt = null;
        try {
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(stmt, connection);
        }
    }

    /**
     * 删除全表数据
     * @param tableName
     * @throws SQLException
     */
    public void deleteTable(String tableName) throws SQLException {
        Connection connection = DBUtil.getConnection();
        Statement stmt = null;
        try {
            stmt = connection.getConnection().createStatement();
            stmt.execute("DELETE FROM " + tableName);
        } catch (SQLException exp) {
            throw exp;
        } finally {
            DBUtil.release(stmt, connection);
        }
    }

    /**
     * Insert with map
     */
    public void mapInsert(Map<String, Object> valMap, String tableName) {
        Assert.isNotEmpty(valMap, "执行mapInsert时valMap不能为空");
        StringBuffer insertSql = new StringBuffer("INSERT INTO ").append(tableName).append(" (");
        StringBuffer placeholders = new StringBuffer("?");// 形参:占位符String,以","分隔
        Object[] args = new Object[valMap.size()];
        Iterator<String> it = valMap.keySet().iterator();
        String firstField = it.next();
        insertSql.append(firstField);
        args[0] = valMap.get(firstField);
        int argsIndex = 1;
        while (it.hasNext()) {
            String field = it.next();
            insertSql.append(",").append(field);
            placeholders.append(",?");
            args[argsIndex] = valMap.get(field);
            argsIndex++;
        }
        insertSql.append(") values (");
        insertSql.append(placeholders).append(")");
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DBUtil.getConnection();
            stmt = connection.getConnection().prepareStatement(insertSql.toString());
            argsIndex = 1;
            for (Object arg : args) {
                stmt.setObject(argsIndex, arg);
                argsIndex++;
            }
            stmt.execute();
            System.out.println("SQL:" + insertSql);
        } catch (SQLException exp) {
            throw new RuntimeException("插入数据库失败");
        } finally {
            DBUtil.release(stmt, connection);
        }
    }

    /**
     * Update by map
     * @param valMap
     * @param whereMap
     * @param tableName
     */
    public void mapUpdate(Map<String, Object> valMap, Map<String, Object> whereMap, String tableName) {
        Assert.isNotEmpty(valMap, "执行mapUpdate时valMap不能为空");
        StringBuffer updateSql = new StringBuffer("UPDATE ").append(tableName).append(" SET ");
        Object[] args = new Object[valMap.size() + whereMap.size()];

        Iterator<String> it = valMap.keySet().iterator();
        String firstField = it.next();
        updateSql.append(firstField).append(" = ?");
        args[0] = valMap.get(firstField);

        int argsIndex = 1;
        while (it.hasNext()) {
            String field = it.next();
            updateSql.append(",").append(field).append(" = ?");
            args[argsIndex] = valMap.get(field);
            argsIndex++;
        }

        Object[] whereArgs = appendWhereClause(updateSql, whereMap);

        for (Object arg : whereArgs) {
            args[argsIndex] = arg;
            argsIndex++;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DBUtil.getConnection();
            stmt = connection.getConnection().prepareStatement(updateSql.toString());
            argsIndex = 1;
            for (Object arg : args) {
                stmt.setObject(argsIndex, arg);
                argsIndex++;
            }
            stmt.execute();
            System.out.println("SQL:" + updateSql);
        } catch (SQLException exp) {
            throw new RuntimeException("插入数据库失败");
        } finally {
            DBUtil.release(stmt, connection);
        }
    }

    /**
     * 追加where条件
     * @param sql
     * @param map
     * @return
     */
    private Object[] appendWhereClause(StringBuffer sql, Map<String, Object> map) {
        Assert.isNotEmpty(map, "AbstractDao.appendWhereClause.map为空,无法构成WHERE从句");
        Object[] objs = new Object[map.size()];
        Iterator<String> it = map.keySet().iterator();
        String firstField = it.next();
        sql.append(" WHERE ").append(firstField).append(" = ?");
        objs[0] = map.get(firstField);
        int index = 1;
        while (it.hasNext()) {
            String field = it.next();
            sql.append(" AND ").append(field).append(" = ?");
            objs[index] = map.get(field);
            index++;
        }
        return objs;
    }

}
