package com.babacit.alarm.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.babacit.alarm.logger.Logger;

public class HttpClient {

	private DefaultHttpClient client = new DefaultHttpClient();

	/**
	 * http请求网络
	 * 
	 * @param url
	 * @param content
	 * @param get 是否是get还是post, get设置为true
	 *
	 * @return
	 */
	public String request(String url, String content) {
		if (url != null) {
			HttpUriRequest request = null;
			request = new HttpPost(url);

			final HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			client.setParams(params);
			try {
				if (request instanceof HttpPost) {
					ByteArrayEntity entity = new ByteArrayEntity(
							content.getBytes());
					((HttpPost) request).setEntity(entity);
				}
				final HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					final String result = EntityUtils.toString(
							response.getEntity(), "UTF_8");
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (request != null)
					request.abort();
			}
		}
		return null;
	}

//	public String upload(String url, String suffixName, byte[] bs) {
//		if (url != null) {
//			HttpUriRequest request = null;
//			request = new HttpPost(url);
//
//			final HttpParams params = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000);
//			client.setParams(params);
//			request.addHeader("suffixName", suffixName);
//			try {
//				if (request instanceof HttpPost) {
//					ByteArrayEntity entity = new ByteArrayEntity(bs);
//					((HttpPost) request).setEntity(entity);
//				}
//				final HttpResponse response = client.execute(request);
//				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//
//					final String result = EntityUtils.toString(
//							response.getEntity(), "UTF_8");
//					return result;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (request != null)
//					request.abort();
//			}
//		}
//		return null;
//	}
	
	public String upload(String url, String suffixName, byte[] bs) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("suffixName", suffixName);
			OutputStream os = conn.getOutputStream();
			os.write(bs);
			os.flush();
			os.close();
			Logger.d(bs.length+",uploadFile: "+url);
			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//读取缓存  
				byte[] buffer = new byte[512];  
				int length = 0;  
				while((length = is.read(buffer)) != -1) {  
					baos.write(buffer, 0, length);//写入输出流  
				}  
				is.close();//读取完毕，关闭输入流  
				String result = baos.toString();
				baos.close();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return null;
	}

}