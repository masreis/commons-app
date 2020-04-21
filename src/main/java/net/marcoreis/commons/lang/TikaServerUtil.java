package net.marcoreis.commons.lang;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TikaServerUtil {
	private String serverUrl;
	private String urlLanguage = "/language/string";
	private String tikaUrl = "/tika";

	public TikaServerUtil(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String extractText(InputStream is) {
		try {
			CloseableHttpClient httpClient =
					HttpClientBuilder.create().build();
			HttpPut request =
					new HttpPut(this.serverUrl + tikaUrl);
			HttpEntity entity = new InputStreamEntity(is);
			request.setEntity(entity);
			CloseableHttpResponse response =
					httpClient.execute(request);
			String text;
			if (response.getStatusLine()
					.getStatusCode() == 200) {
				text = EntityUtils
						.toString(response.getEntity());
			} else {
				System.out.println(response.getStatusLine()
						.getReasonPhrase());
				text = "Not identified";
			}
			// post.addHeader("","");
			httpClient.close();
			response.close();
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public String identifyLanguage(InputStream is) {
		try {
			CloseableHttpClient httpClient =
					HttpClientBuilder.create().build();
			HttpPut request =
					new HttpPut(this.serverUrl + urlLanguage);
			HttpEntity entity = new InputStreamEntity(is);
			request.setEntity(entity);
			CloseableHttpResponse response =
					httpClient.execute(request);
			String language;
			if (response.getStatusLine()
					.getStatusCode() == 200) {
				language = EntityUtils
						.toString(response.getEntity());
			} else {
				System.out.println(response.getStatusLine()
						.getReasonPhrase());
				language = "Not identified";
			}
			// post.addHeader("","");
			httpClient.close();
			response.close();
			return language;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
