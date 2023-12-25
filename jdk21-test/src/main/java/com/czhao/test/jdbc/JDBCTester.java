package com.czhao.test.jdbc;

import java.sql.*;

/**
 * @author zhaochun
 */
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JDBCTester {

    //    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://d3-brood-mysql8:3307/db_web_pm?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String JDBC_USER = "zhaochun1";
    private static final String JDBC_PASSWORD = "zhaochun@GITHUB";

    private static final String SQL_SELECT = "SELECT * FROM accounts";
    private static final String SQL_TRUCATE = "truncate table accounts";
    private static final String SQL_INSERT = """
            INSERT INTO accounts
            (created_at, updated_at, deleted_at, act_name, act_pwd, act_nick_name, act_introduction, act_status, act_register_date)
            VALUES('2022-12-28 15:41:24.181', '2022-12-28 15:41:24.181', NULL, 'libai', 'libai@DATANG', '诗仙太白', '李白，唐朝诗人，字太白，号青莲居士，世称诗仙。', 0, '2022-12-28 15:41:24.180')
            """;

    // queryBySql 使用jdbc执行传入的select sql
    public void queryBySql() {
        try (
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(SQL_SELECT)) {
            if (rs.next()) {
                System.out.println(rs.getInt("id") + ":" + rs.getString("act_introduction"));
                rs.last();
                System.out.println(rs.getInt("id") + ":" + rs.getString("act_introduction"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearTbl() {
        try (
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(SQL_TRUCATE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTbl() {
        try (
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            for (int i = 0; i < 10000; i++) {
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        JDBCTester me = new JDBCTester();
        me.queryBySql();
//        me.clearTbl();
//        me.insertTbl();
    }
}
