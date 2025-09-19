package com.czhao.test.jdbc;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * @author zhaochun
 */
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JDBCTester {

    private static final String JDBC_URL_MYSQL = "jdbc:mysql://d3-brood-mysql8:3307/db_web_pm?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true";
    private static final String JDBC_URL_MARIA = "jdbc:mariadb://d3-brood-mysql8:3308/db_web_pm?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true";
    private static final String JDBC_URL_MARIA_REMOTE = "jdbc:mariadb://192.168.60.60:3308/db_web_pm?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true";
    private static final String JDBC_USER = "zhaochun1";
    private static final String JDBC_PASSWORD = "zhaochun@GITHUB";

    private static final String SQL_SELECT = "SELECT * FROM accounts";
    private static final String SQL_TRUCATE = "truncate table accounts";
    private static final String SQL_INSERT = """
            INSERT INTO accounts
            (created_at, updated_at, deleted_at, act_name, act_pwd, act_nick_name, act_introduction, act_status, act_register_date)
            VALUES(?, ?, NULL, ?, ?, ?, ?, 0, ?)
            """;

    private final String jdbcUrl;

    public JDBCTester(int jdbcType) {
        this.jdbcUrl = switch (jdbcType) {
            case 1 -> JDBC_URL_MYSQL;
            case 2 -> JDBC_URL_MARIA;
            case 3 -> JDBC_URL_MARIA_REMOTE;
            default -> throw new IllegalArgumentException("JDBC type is not valid");
        };
    }

    // queryBySql 使用jdbc执行传入的select sql
    @SuppressWarnings("StringTemplateMigration")
    public void queryBySql() {
        try (
                Connection conn = DriverManager.getConnection(jdbcUrl, JDBC_USER, JDBC_PASSWORD);
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
                Connection conn = DriverManager.getConnection(jdbcUrl, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(SQL_TRUCATE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTbl() {
        try (
                Connection conn = DriverManager.getConnection(jdbcUrl, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            for (int i = 0; i < 100; i++) {
                // 获取当前时间 LocalDateTime 并转为 Timestamp
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                for (int j = 0; j < 1000; j++) {
                    ps.setTimestamp(1, timestamp);
                    ps.setTimestamp(2, timestamp);
                    ps.setString(3, "libai");
                    ps.setString(4, "libai@DATANG");
                    ps.setString(5, "诗仙太白");
                    ps.setString(6, "李白，唐朝诗人，字太白，号青莲居士，世称诗仙。");
                    ps.setTimestamp(7, timestamp);
                    ps.addBatch();
                }
                ps.executeBatch();
                System.out.println("insert 1000 rows");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
