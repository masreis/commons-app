package net.marcoreis.commons.lang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WordPressCommentUtil {

	private Connection connection;

	public WordPressCommentUtil(String url, String user,
			String pwd) throws SQLException {
		connection =
				DriverManager.getConnection(url, user, pwd);
		connection.setAutoCommit(false);
	}

	public Map<Long, String> readComments()
			throws SQLException {
		Map<Long, String> map = new HashMap<>();
		String sql =
				"select comment_id, comment_content from wp_comments";
		PreparedStatement stmt =
				connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			map.put(rs.getLong("comment_id"),
					rs.getString("comment_content"));
		}
		rs.close();
		stmt.close();
		return map;
	}

	public void deleteComment(Long id) {
		try {
			String sql =
					"delete from wp_comments where comment_id = ?";
			PreparedStatement pstmt =
					connection.prepareStatement(sql);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			this.connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
