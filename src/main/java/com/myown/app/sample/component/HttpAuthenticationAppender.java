package com.myown.app.sample.component;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provide basic http authentication.
 *
 * @author Thiago Diniz da Silveira<thiagods.ti@gmail.com>
 *
 */
public class HttpAuthenticationAppender extends HttpAppenderAbstract {



	@SuppressWarnings("restriction")
	@Override
	public void start() {
		super.start();


		openConnection();
		addInfo("Using Basic Authentication");
	}

	@Override
	protected HttpURLConnection openConnection() {
		HttpURLConnection conn = null;
		try {
			URL urlObj = new URL(protocol, url, port, path);
			conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("POST");
			return conn;
		} catch (Exception e) {
			addError("Error to open connection Exception: ", e);
			return null;
		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				addError("Error to open connection Exception: ", e);
				return null;
			}
		}
	}



}
