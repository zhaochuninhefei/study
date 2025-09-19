package com.czhao.test.mod2;

import com.czhao.test.mod1.Employee;

import java.sql.*;

/**
 * @author zhaochun
 */
public class Mod2Main {
    public static void main(String[] args) {
        Employee employee = new Employee();
        employee.setName("张三");
        employee.setLevel(1);
        System.out.println(employee);

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_jdk11_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "root",
                "overseas");
             PreparedStatement ps = connection.prepareStatement("select * from tb_employee")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("employee_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
