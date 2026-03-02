package com.library.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.sql.SQLException;

// 숨겨둔 DB접속 정보 읽어오기
public class DBUtil {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (
                InputStream dbconn = DBUtil.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties")) {
            Properties proper = new Properties();
            proper.load(dbconn);
            URL = proper.getProperty("db.url");
            USER = proper.getProperty("db.user");
            PASSWORD = proper.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getConnection() 메서드 호출시 예외처리
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
