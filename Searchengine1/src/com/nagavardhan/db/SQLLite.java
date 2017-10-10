package com.nagavardhan.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLLite {

	private static SQLLite dbInstance = null;
	private static Connection c = null;

	private SQLLite() {

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static SQLLite getInstance() {
		if (dbInstance == null) {
			synchronized (SQLLite.class) {
				if (dbInstance == null) {
					dbInstance = new SQLLite();
				}
			}
		}
		return dbInstance;
	}

	public void executeQuery(String sql) {

		Statement stmt = null;
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQueryResultSet(String sql) {

		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;

	}

	public static void main(String[] args) {

	}

}
