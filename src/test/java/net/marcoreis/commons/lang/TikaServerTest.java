package net.marcoreis.commons.lang;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TikaServerTest {

	private static final String TIKA_SERVER_OCR =
			"http://localhost:9998/";

	private static final String TIKA_SERVER =
			"http://localhost:9997/";

	@Test
	public final void testIdentifyLanguagePt()
			throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER_OCR);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test.txt"));
		String language = identifier.identifyLanguage(bytes);
		assertTrue("pt".equals(language));
	}

	@Test
	public final void testIdentifyLanguageEs()
			throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER_OCR);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test-es.txt"));
		String language = identifier.identifyLanguage(bytes);
		assertTrue("es".equals(language));
	}

	@Test
	public final void testExtractText() throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER_OCR);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test.pdf"));
		String text = identifier.extractText(bytes);
		assertTrue(text.length() > 0);
	}

	@Test
	public final void testExtractTextOcr() throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER_OCR);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test.png"));
		String text = identifier.extractText(bytes);
		assertTrue(text.contains("saltos unilaterais"));
	}

	@Test
	public final void testExtractTextWithoutOcr()
			throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test.png"));
		String text = identifier.extractText(bytes);
		assertTrue(text.trim().length() == 0);
	}

	@Test
	public final void testIdentifyMime() throws IOException {
		TikaServerUtil identifier =
				new TikaServerUtil(TIKA_SERVER_OCR);
		byte[] bytes = IOUtils.toByteArray(this.getClass()
				.getResourceAsStream("/test.jpg"));
		String mime = identifier.identifyMimeType(bytes);
		assertTrue("image/jpeg".equals(mime));
	}
}
