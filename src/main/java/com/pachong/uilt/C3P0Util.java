package com.pachong.uilt;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Util {
	static ComboPooledDataSource cpds = null;
	static {
		// 这里有个优点，写好配置文件，想换数据库，简单
		// cpds = new ComboPooledDataSource("oracle");//这是oracle数据库
		cpds = new ComboPooledDataSource("mysql");// 这是mysql数据库
		cpds.setUser("root");// 用户姓名
		cpds.setPassword("12345");// 用户密码
		cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/pachong?useUnicode=true&characterEncoding=utf-8");// MySQL数据库连接url
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获得数据库连接
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数据库关闭操作
	 * 
	 * @param conn
	 * @param st
	 * @param pst
	 * @param rs
	 */
	public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
 	}

	/**
	 * 测试DBUtil类
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = getConnection();
		System.out.println(conn.getClass().getName());
		close(conn, null, null);
	}
}
