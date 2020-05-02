package net.marcoreis.commons.lang;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileExtractorUtil {

	private static final Logger logger =
			Logger.getLogger(FileExtractorUtil.class);
	private static final long ONE_MEGA_BYTES = 1024 * 1024;
	private String serverUrlOcr = "http://localhost:9998";
	private String serverUrl = "http://localhost:9997";
	private TikaServerUtil tikaServer;
	public Set<String> mimeTypes = new HashSet<>();

	private enum TikaSource {
		DO_OCR, DO_NOT_OCR
	}

	public FileExtractorUtil(TikaSource type) {
		if (type == TikaSource.DO_OCR) {
			this.tikaServer = new TikaServerUtil(serverUrlOcr);
		} else if (type == TikaSource.DO_NOT_OCR) {
			this.tikaServer = new TikaServerUtil(serverUrl);
		}
	}

	public void walk(File path) {

		if (path.isFile()) {
			processFile(path);
		} else {
			logger.info(path);
			if (path.toString().contains("/."))
				return;
			File files[] = path.listFiles();
			for (File dirOrFile : files) {
				walk(dirOrFile);
			}
		}
	}

	private void processFile(File file) {
		try {
			long inicio = System.currentTimeMillis();
			logger.info("Starting extraction: " + file);
			byte[] bytes = IOUtils
					.toByteArray(new FileInputStream(file));
			// Opitionally, you can identify the mime type.
			String mimeType =
					this.tikaServer.identifyMimeType(bytes);
			this.mimeTypes.add(mimeType);
			logger.info("Mime: " + mimeType);
			if (mimeType.startsWith("image")
					&& file.length() > ONE_MEGA_BYTES) {
				logger.warn(
						"A large image is slow to the OCR process");
			}
			String text = this.tikaServer.extractText(bytes,
					mimeType);
			logger.info(MessageFormat.format(
					"Extraction finished: {0}. Size: {1} bytes. Time: {2} ms",
					file, file.length(),
					(System.currentTimeMillis() - inicio)));
			// Do something with the text
		} catch (Exception e) {
			logger.error(MessageFormat.format(
					"Exception in file {0}: {1}", file,
					e.getMessage()));
		}
	}

	public static void main(String[] args) {
		try {
			logger.info("Start walking");
			FileExtractorUtil util =
					new FileExtractorUtil(TikaSource.DO_OCR);
			util.walk(new File(
					"/home/marco/Dropbox/literature/"));
			logger.info(util.mimeTypes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
