package cn.zj.easynet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.zj.pubinfo.exception.BaseException;
import cn.zj.pubinfo.lang.Strings;
import cn.zj.pubinfo.util.HttpClientFactory;

@SuppressWarnings("deprecation")
public class SslTest {
	private static final Logger log = Logger.getLogger(SslTest.class);
	
	
	public static String httpPost(String url, String data) throws BaseException {
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", "text/xml");
			if (!Strings.isBlank(data)) {
				StringEntity myEntity = new StringEntity(data, "UTF-8");
				httppost.setEntity(myEntity);
			}

			HttpClient httpClient = HttpClientFactory.getHttpClient();
			SSLContext ctx = SSLContext.getInstance("SSL");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {

				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

			HttpResponse response = httpClient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			throw new BaseException("e.httpPost", "远程调用" + url + "失败，状态码:"
					+ statusCode);
		} catch (Throwable e) {
			log.error("调用远程接口失败:", e);
			throw new BaseException("e.httpPost", "远程调用" + url + "失败:"
					+ e.getMessage());
		}
	}
	
	public static String httpPostTwo(String url, String data)
			throws BaseException {
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", "text/xml");
			if (!Strings.isBlank(data)) {
				StringEntity myEntity = new StringEntity(data, "UTF-8");
				httppost.setEntity(myEntity);
			}

			HttpClient httpClient = HttpClientFactory.getHttpClient();
			SSLContext ctx = SSLContext.getInstance("SSL");
			String algorithm = null;
			if (algorithm == null) {
				algorithm = "SunX509";
			}

			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream(new File("D:\\yx-it-mirror-client.jks")),
						"secret".toCharArray());

				KeyManagerFactory kmf = KeyManagerFactory
						.getInstance(algorithm);
				kmf.init(ks, "secret".toCharArray());

				X509TrustManager tm =  new X509TrustManager() {
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}
					
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) {
						// Always trust - it is an example.
						// You should do something in the real world.
						// You will reach here only if you enabled client certificate auth,
						// as described in SecureChatSslContextFactory.
						System.err.println(
								"UNKNOWN CLIENT CERTIFICATE: " + chain[0].getSubjectDN());
					}
					
					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) {
						// Always trust - it is an example.
						// You should do something in the real world.
						System.err.println(
								"UNKNOWN SERVER CERTIFICATE: " + chain[0].getSubjectDN());
					}
				} ;
				
				ctx.init(kmf.getKeyManagers(), new TrustManager[] { tm }, null);
			} catch (Exception e) {
				throw new Error(
						"Failed to initialize the server-side SSLContext", e);
			}
			
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

			HttpResponse response = httpClient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Throwable e) {
			log.error("调用远程接口失败:", e);
			throw new BaseException("e.httpPost", "远程调用" + url + "失败:"
					+ e.getMessage());
		}
		return "";
	}
	
	public static String httpPostThree(String url, String data){
		try {
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			DefaultHttpClient client = new DefaultHttpClient();
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory
					.getSocketFactory();
			socketFactory
					.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(
					client.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
					client.getParams());
			// Set verifier
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

			HttpUriRequest http = new HttpPost(url);
			HttpPost httppost = (HttpPost) http;
			httppost.addHeader("Content-Type", "text/xml");
			if (!Strings.isBlank(data)) {
				StringEntity myEntity = new StringEntity(data, "UTF-8");
				httppost.setEntity(myEntity);
			}
			HttpResponse response = httpClient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) throws BaseException, ClientProtocolException, IOException {
		String param = "{\"mobile\":\"13989455787\","
				+        "\"account\":\"\","
				+        "\"name\":\"\","
				+       "\"userId\":\"3732001\"}";
		
//		System.setProperty("javax.net.ssl.trustStore", "D:\\yx-it-mirror-client.jks");
		param = EncryptUtil.encryptFull(param);
//		String ret = httpPost("https://localhost:8888/yx-it/services/http/remote/q4", param);
//		String ret = httpPostTwo("https://192.168.21.2:8888/yx-it/services/http/remote/q4", param);
		String ret = httpPostThree("https://192.168.21.2:8888/yx-it/services/http/remote/q4", param);
//		String ret = httpPost("https://localhost/yx-it/services/http/remote/q4", param);
//		String ret = HttpClientPool.getInstance().postMethod("https://192.168.21.2:8888/yx-it/services/http/remote/q4", param);
//		String ret = HttpClientPool.getInstance().postMethod("http://it.ephonelive.com.cn/services/http/remote/q4", param);
		ret = EncryptUtil.decryptFull(ret);
		System.out.println(ret);
	}
}
