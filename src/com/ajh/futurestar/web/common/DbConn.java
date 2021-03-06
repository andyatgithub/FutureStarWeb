package com.ajh.futurestar.web.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {
	public static final String DB_URL = "C:\\sqlitedb\\futurestar.db";

	// Callers are responsible for close connection.
	public static Connection getDbConnection()
			throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection("jdbc:sqlite:" + DB_URL);
		c.setAutoCommit(false);

		return c;
	}
}
