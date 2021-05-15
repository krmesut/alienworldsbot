package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import entities.http.HTTPMethods;

public class HttpRequest {
	private HTTPMethods method;
	private String targetURL;
	private String contentType;

	public HttpRequest(HTTPMethods method, String targetURL, String contentType) {
		super();
		this.method = method;
		this.targetURL = targetURL;
		this.contentType = contentType;
	}

	public String execute(String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method.toString());
			connection.setRequestProperty("Content-Type", contentType);

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			System.out.println("-- sending " + method + " request");
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			System.out.println("-- getting " + method + " response\n\t\t" + connection.getResponseCode());
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+

			if (connection.getResponseCode() == 200) {
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
			}

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
