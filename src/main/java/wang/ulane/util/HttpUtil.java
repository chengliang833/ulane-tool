package wang.ulane.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	private static CloseableHttpClient httpclient;
	
	private static RequestConfig requestConfig;

	public HttpUtil(){
		init(60*1000);
	}
	public HttpUtil(int timeout){
		init(timeout);
	}
	public void init(int timeout) {
		/*
		 * setConnectTimeout：设置连接超时时间，单位毫秒。
		 * setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 * setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		
		if(requestConfig==null){
			requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)  
			        .setSocketTimeout(timeout).build(); 
		}
		
		if(httpclient==null){
			//采用绕过验证的方式处理https请求  
		    SSLContext sslcontext;
			try {
			   sslcontext = createIgnoreVerifySSL();
			   // 设置协议http和https对应的处理socket链接工厂的对象  
		       Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
		           .register("http", PlainConnectionSocketFactory.INSTANCE)  
		           .register("https", new SSLConnectionSocketFactory(sslcontext
                           , SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
		           .build();  
		       PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
		       cm.setMaxTotal(300);//连接池最大并发连接数
			   cm.setDefaultMaxPerRoute(300);//单路由最大并发数
			   httpclient = HttpClients.custom()
					   .setConnectionManager(cm)
					   .setDefaultRequestConfig(requestConfig)
					   .build();
			} catch (KeyManagementException e) {
				log.error(e.getMessage(), e);
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage(), e);
			}  
		}

	}
	
	/** 
	 * 绕过验证 
	 *   
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 */  
	public SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	    SSLContext sc = SSLContext.getInstance("TLSv1.2");  
	  
	    // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        	if("errortemp...".equals(paramString)){
	        		throw new CertificateException();
	        	}
	        }  
	  
	        @Override  
	        public void checkServerTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        	if("errortemp...".equals(paramString)){
	        		throw new CertificateException();
	        	}
	        }  
	  
	        @Override  
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	  
	    sc.init(null, new TrustManager[] { trustManager }, null);  
	    return sc;  
	}  
	
	public String doGet(String url) {
		return doGet(url, null);
	}
	
	public String doGet(String url, Map<String, String> header) {
		// 创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
		HttpGet httpRequest = new HttpGet(url);
		if(header != null) {
			for (String key : header.keySet()) {  
				httpRequest.addHeader(key, header.get(key));
			}  
		}
		return httpExecute(httpRequest);
	}
	
	public byte[] doGetForIS(String url, Map<String, String> header) throws Exception {
		// 创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象。
		HttpGet httpRequst = new HttpGet(url);
		if(header != null) {
			for (String key : header.keySet()) {
				httpRequst.addHeader(key, header.get(key));
			}
		}
		CloseableHttpResponse  httpResponse =null;
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			httpResponse = httpclient.execute(httpRequst);// 其中HttpGet是HttpUriRequst的子类
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				httpEntity.writeTo(bos);
				return bos.toByteArray();
			} else {
				httpRequst.abort();
			}
		} catch (Exception e) {
			throw e;
		}finally{
			if(httpRequst!=null){
				httpRequst.releaseConnection();
			}
		}
		throw new RuntimeException("request error...");
	}
	
	public String doPost(String url, String params) {
		return doPost(url, params, null);
	}
	public String doPost(String url, String params, Map<String, String> header) {
		HttpPost post = new HttpPost(url);// 创建HttpPost对象
		post.addHeader("Content-Type", "application/json");
		if(header != null) {
			for (String key : header.keySet()) {  
				post.addHeader(key, header.get(key));
			}  
		}
		StringEntity s = new StringEntity(params,"utf-8");
		post.setEntity(s);
		return httpExecute(post); 
	}
	
	public String httpExecute(HttpRequestBase httpRequest){
		String result = "";
		CloseableHttpResponse  httpResponse =null;
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			httpResponse = httpclient.execute(httpRequest);// 其中HttpGet是HttpUriRequst的子类
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity, "UTF-8");// 取出应答字符串
			} else{
				result = "http status:"+httpResponse.getStatusLine().getStatusCode();
				httpRequest.abort();
			}
		} catch (ClientProtocolException e) {
			log.error(e.getMessage(), e);
			result = e.getMessage().toString();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			result = e.getMessage().toString();
		}finally{
			if(httpRequest!=null){
				httpRequest.releaseConnection();
			}
		}
		return result;
	}
	
	
	
	
//	/**
//	 * 模拟浏览器post提交
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public static HttpPost getPostMethod(String url) {
//		HttpPost pmethod = new HttpPost(url); // 设置响应头信息
//		pmethod.addHeader("Connection", "keep-alive");
//		pmethod.addHeader("Accept", "*/*");
//		pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		pmethod.addHeader("Host", "api.mch.weixin.qq.com");
//		pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
//		pmethod.addHeader("Cache-Control", "max-age=0");
//		pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//		return pmethod;
//	}
//
//	/**
//	 * 模拟浏览器GET提交
//	 * @param url
//	 * @return
//	 */
//	public static HttpGet getGetMethod(String url) {
//		HttpGet pmethod = new HttpGet(url);
//		// 设置响应头信息
//		pmethod.addHeader("Connection", "keep-alive");
//		pmethod.addHeader("Cache-Control", "max-age=0");
//		pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//		pmethod.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");
//		return pmethod;
//	}

}