package net.marcoreis.commons.lang;

import org.junit.Test;

public class WordPressCommentLanguageUtilTest {

	@Test
	public final void testParse() {
		String url = System.getProperty("url");
		String user = System.getProperty("user");
		String pwd = System.getProperty("pwd");
		WordPressCommentLanguageUtil util =
				new WordPressCommentLanguageUtil(url, user,
						pwd);
		util.parse();
	}

}
