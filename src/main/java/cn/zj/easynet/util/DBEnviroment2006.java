package cn.zj.easynet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBEnviroment2006{
	// 是否使用查询服务器
	private boolean m_useLW = false;

	// 数据库host
	private String m_host = ConfigUtil.M_HOST;

	private String m_user = ConfigUtil.M_USER;

	private String m_password = ConfigUtil.M_PASSWORD;

	private String m_confpath = null;

	private String m_keypath = ConfigUtil.M_KEYPATH;

	private String m_logdir = ConfigUtil.M_LOGDIR;

	private String m_url = null;

	private Connection connection = null;

	public static Logger m_logger = Logger.getLogger(DBEnviroment2006.class);

	public DBEnviroment2006() throws Exception {
		makeURL();
		createConn();
	}
	
	public DBEnviroment2006(boolean useLW, String dbhost, String confpath,
			String keypath, String user, String password) throws Exception {
		m_useLW = useLW;
		m_host = dbhost;
		m_confpath = confpath;
		m_keypath = keypath;
		m_user = user;
		m_password = password;

		makeURL();
		createConn();
	}

	private void makeURL() throws Exception {
		// 是否使用查询服务器
		if (m_useLW) {
			Class.forName("com.netease.ddb.lightweight.LWDriver");
			m_url = "jdbc:netease:lightweight://" + m_confpath;
		} else {
			Class.forName("com.netease.backend.db.DBDriver");
			m_url = m_host;
		}

		m_url += "?user=" + m_user;
		m_url += "&password=" + m_password;
		m_url += "&key=" + m_keypath;

		if (!m_useLW) {
			if (m_logdir != null && !m_logdir.isEmpty())
				m_url += "&logdir=" + m_logdir;
		}
		
		m_logger.debug("url:" + m_url);
	}

	private void createConn() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}

		connection = DriverManager.getConnection(m_url);
		m_logger.info("url:" + m_url);
		m_logger.info("connect to ddb successful!");
	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			createConn();
		}

		return connection;
	}

	public boolean isDuplicate(SQLException ex) {
		if (16001 == ex.getErrorCode() || 1062 == ex.getErrorCode())
			return true;
		return false;
	}

	public void close() throws SQLException {
		if (null != connection) {
			connection.close();
			connection = null;
		}
	}
}
