package net.marcoreis.commons.lang;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TikaServerTest {

	@Test
	public final void testIdentifyLanguage() throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil("http://localhost:9998/");
		InputStream is =
				new FileInputStream("/home/marco/teste.txt");
		String language = identifier.identifyLanguage(is);
		assertTrue("pt".equals(language));
	}

	@Test
	public final void testExtractText() throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil("http://localhost:9998/");
		InputStream is =
				new FileInputStream("/home/marco/teste.pdf");
		String text = identifier.extractText(is);
		assertTrue(text.length() > 0);
	}
}
