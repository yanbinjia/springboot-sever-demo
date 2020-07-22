package com.demo.study.testclass.spi;

import java.sql.*;

public class JdbcTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/bg_admin";
        String user = "root";
        String pwd = "1234ZXCV";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = (Connection) DriverManager.getConnection(url, user, pwd);
        PreparedStatement preparedStatement = conn.prepareStatement("select count(1) as count from user_info");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet != null && resultSet.next()) {
            System.out.println("count=" + resultSet.getString("count"));
        }

    }
}
