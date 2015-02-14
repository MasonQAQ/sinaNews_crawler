package com.lxf.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ���ݿ�������
 * 
 * @author �����
 * 
 */
public class DbConnection {
	

	//oracle����д��
	private static String diver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/news?user=lxf&password=lxf";
	private static String username = "lxf";
	private static String password = "lxf";

	static {
		try {
			//��������
			Class.forName(diver);
		} catch (ClassNotFoundException e) {
			System.out.println("���ز���������");
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @return connection
	 */
	public static Connection getConn() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println("���Ӵ�����������URL���û��������룡");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * �ر���Դ
	 * 
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	public static void close(Connection connection, Statement statement,
			ResultSet resultSet) {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
		} catch (SQLException e) {
			System.out.println("�ر���Դʱ���ִ���");
			e.printStackTrace();
		}
	}
}
