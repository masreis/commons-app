package net.marcoreis.commons.lang;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class WordPressCommentLanguageUtil {

	private String url;
	private String user;
	private String pwd;

	public WordPressCommentLanguageUtil(String url, String user,
			String pwd) {
		this.url = url;
		this.user = user;
		this.pwd = pwd;
	}

	public void parse() {
		TikaServerUtil tikaUtil =
				new TikaServerUtil("http://localhost:9998");
		try {
			WordPressCommentUtil util =
					new WordPressCommentUtil(url, user, pwd);
			Map<Long, String> map = util.readComments();
			for (Long id : map.keySet()) {
				String comment = map.get(id);
				String language = tikaUtil.identifyLanguage(
						new ByteArrayInputStream(
								comment.getBytes()));
				if (language.equals("pt")) {
					System.out.println("Comment: [" + id + "]: "
							+ comment);
					System.out.println("=============");
				} else {
					util.deleteComment(id);
				}
			}
			util.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
