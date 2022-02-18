package com.xibeicz.jdbc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionUtil {
	
	private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://47.97.110.81:3306/gp?useUnicode=true&characterEncoding=utf-8";
 // MySQL配置时的用户名
    private static String user = "gp";
    // MySQL配置时的密码
    private static String password = "34AY5TpmKx6zLrbx";
    /**
     * 初始化
     * @param driver2	com.mysql.jdbc.Driver
     * @param url2	数据库连接
     * 	示例：jdbc:mysql://47.97.110.81:3306/gp?useUnicode=true&characterEncoding=utf-8
     * @param user2		数据库用户
     * @param password2	数据库密码
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void init(String driver2, String url2, String user2, String password2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	driver = driver2;
    	url = url2;
    	user = user2;
    	password = password2;
    	Class.forName(driver).newInstance();
    }
    
    /**
     * 获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConn() throws SQLException {
    	return DriverManager.getConnection(url, user, password); //获取连接
    }
}
