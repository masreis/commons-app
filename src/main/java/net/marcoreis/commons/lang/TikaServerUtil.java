package net.marcoreis.commons.lang;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.text.MessageFormat;

public class TikaServerUtil {
	private static final Logger logger =
			Logger.getLogger(TikaServerUtil.class);
	private String serverUrl;
	private String urlLanguage = "/language/string";
	private String urlDetect = "/detect/stream";
	private String urlTika = "/tika";

	public TikaServerUtil(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String extractText(byte[] bytes) {
		return extractText(bytes, null);
	}

	public String extractText(byte[] bytes,
			String contentType) {
		try {
			String text = executeRequestPutReturnString(bytes,
					this.urlTika, contentType);
			return text;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public String identifyLanguage(byte[] bytes,
			String contentType) {
		try {
			String language = executeRequestPutReturnString(
					bytes, urlLanguage, contentType);
			return language;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public String identifyMimeType(byte[] bytes) {
		try {
			String language = executeRequestPutReturnString(
					bytes, urlDetect, null);
			return language;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public String executeRequestPutReturnString(byte[] bytes,
			String service, String contentType) {
		try {
			CloseableHttpClient httpClient =
					HttpClientBuilder.create().build();
			HttpPut request =
					new HttpPut(this.serverUrl + service);
			if (contentType != null) {
				request.addHeader("Content-type", contentType);
			}
			// If you want to change the OCR default language (eng) to
			// portuguese (por)
			request.addHeader("X-Tika-OCRLanguage", "por");
			HttpEntity entity = new ByteArrayEntity(bytes);
			request.setEntity(entity);
			CloseableHttpResponse response =
					httpClient.execute(request);
			String result;
			if (response.getStatusLine()
					.getStatusCode() == 200) {
				result = EntityUtils
						.toString(response.getEntity());
			} else {
				logger.error(MessageFormat.format("{0} - {1}",
						response.getStatusLine()
								.getStatusCode(),
						response.getStatusLine()
								.getReasonPhrase()));
				result = "Not identified";
			}
			httpClient.close();
			response.close();
			return result;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public String identifyLanguage(byte[] bytes) {
		return identifyLanguage(bytes, null);
	}
}
