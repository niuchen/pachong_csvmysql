//package com.pachong;
//
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import java.nio.charset.Charset;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.http.Header;
//import org.apache.http.HeaderElement;
//import org.apache.http.HeaderIterator;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.CookieSpecs;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.params.CookiePolicy;
//import org.apache.http.client.params.HttpClientParams;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.client.utils.URIUtils;
//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.entity.BufferedHttpEntity;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
//import org.apache.http.impl.client.HttpClients;
//
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.message.BasicHttpResponse;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.util.EntityUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
///**
// *  niuchen
// *  链接池的方式  进行远程http接口调用.
// * @author Nan 2015-11
// */
//public class HttpClientUtil {
//    private static PoolingHttpClientConnectionManager cm;
//    private static String EMPTY_STR = "";
//    private static String UTF_8 = "UTF-8";
//    private HttpClientUtil() {}
//    public static Log logger = LogFactory.getLog(HttpClientUtil.class);  //日志
//    private static void init() {
//
//			try {
//			     if (cm == null) {
//			           // cm = new PoolingHttpClientConnectionManager();
//				          SSLContext sslcontext;
//				sslcontext = SSLContexts.custom().loadTrustMaterial(null,
//				  new TrustSelfSignedStrategy()) .build();
//				HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//				  sslcontext,hostnameVerifier);
//				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//				  .register("http", PlainConnectionSocketFactory.getSocketFactory())
//				  .register("https", sslsf)
//				  .build();
//						cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
// 			            cm.setMaxTotal(100);// 整个连接池最大连接数
//			            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
//			     }
//			} catch (KeyManagementException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (KeyStoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//
//    }
//
//    /**
//     * 通过连接池获取HttpClient
//     *
//     * @return
//     */
//    public static CloseableHttpClient getHttpClient() {
//        init();
//    	RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)//标准Cookie策略
//         		.setRedirectsEnabled(false)
//         		   //与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间
//                 .setConnectTimeout(200000)
//                  .setSocketTimeout(200000)
//                      .build();
//        return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm)
//        		 .setRetryHandler(new DefaultHttpRequestRetryHandler())  //默认失败后重发3次，可用别的
//        		.build() ;
//    }
//
//
//    public static CloseableHttpResponse httpGetRequest(CloseableHttpClient httpclient,HttpGet httpget) {
//    	int i = 0;
//    	  while (true) {
//    	        CloseableHttpResponse response =null;
//    	        try{
//    	           response = httpclient.execute(httpget);
//    	        }catch(Exception e){
//    	            if(i>3){
//    	            	logger.error(httpget.getURI().toString()+" 链接异常,重拾链接第"+i+"次:依然失败,返回null");
//    	            	 return null;
//    	            }
//     	            logger.error(httpget.getURI().toString()+" 链接异常,重拾链接第"+i+"次:"+e);
//    	            i++;
//    	        }
//    	       if (response!= null ) {
//    	    	   return response;
//    	       }
//    	  }
//
//    }
//
//    public static CloseableHttpResponse httpPostRequest(CloseableHttpClient httpclient,HttpPost httpPost) {
//    	int i = 0;
//    	  while (true) {
//    		  CloseableHttpResponse response =null;
//    	        try{
//    	           response = httpclient.execute(httpPost);
//    	        }catch(Exception e){
//    	            if(i>3){
//    	            	logger.error(httpPost.getURI().getPath()+" 链接异常,重拾链接第"+i+"次:依然失败,返回null");
//    	            	 return null;
//    	            }
//    	            logger.error(httpPost.getURI().getPath()+" 链接异常,重拾链接第"+i+"次:"+e);
//    	            i++;
//    	        }
//    	       if (response!= null ) {
//
//     	           return response;
//    	       }
//    	  }
//
//    }
//    /**
//     *
//     *
//     *
//     * @param url
//     * @return
//     */
//    public static String httpGetRequest(String url) {
//        HttpGet httpGet = new HttpGet(url);
//        return getResult(httpGet);
//    }
//
//    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
//        URIBuilder ub = new URIBuilder();
//        ub.setPath(url);
//
//        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
//        ub.setParameters(pairs);
//
//        HttpGet httpGet = new HttpGet(ub.build());
//        return getResult(httpGet);
//    }
//
//    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
//            throws URISyntaxException {
//        URIBuilder ub = new URIBuilder();
//        ub.setPath(url);
//
//        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
//        ub.setParameters(pairs);
//
//        HttpGet httpGet = new HttpGet(ub.build());
//        for (Map.Entry<String, Object> param : headers.entrySet()) {
//            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
//        }
//        return getResult(httpGet);
//    }
//
//    public static String httpPostRequest(String url) {
//        HttpPost httpPost = new HttpPost(url);
//        return getResult(httpPost);
//    }
//
//    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
//        HttpPost httpPost = new HttpPost(url);
//        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
//        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
//        return getResult(httpPost);
//    }
//
//    /**httpPost传送json格式数据   **/
//    public static String httpPostJsonRequest(String url,String  jsonStr) throws Exception {
//    //	HttpClient hc=HttpClients.createDefault();
//    	HttpPost post=new HttpPost(url);
//    	List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
//    	param.add(new BasicNameValuePair("jsonStr", jsonStr));
//    	UrlEncodedFormEntity he;
//    	he = new UrlEncodedFormEntity(param,"UTF-8");
//    	post.setEntity(he);
////    	HttpResponse res=hc.execute(post);
////    	HttpEntity entity=res.getEntity();
////    	String msg=EntityUtils.toString(entity,"UTF-8");
////    	System.out.println(msg);
//
//
//        return getResult(post);
//    }
//
//    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
//            throws UnsupportedEncodingException {
//        HttpPost httpPost = new HttpPost(url);
//
//        for (Map.Entry<String, Object> param : headers.entrySet()) {
//            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
//        }
//
//        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
//        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
//
//        return getResult(httpPost);
//    }
//
//    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
//        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        for (Map.Entry<String, Object> param : params.entrySet()) {
//            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
//        }
//
//        return pairs;
//    }
//
//    /**
//     * 处理Http请求
//     *
//     * @param request
//     * @return
//     */
//    private static String getResult(HttpRequestBase request) {
//        // CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpClient httpClient = getHttpClient();
//        try {
//            CloseableHttpResponse response = httpClient.execute(request);
//            // response.getStatusLine().getStatusCode();
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                // long len = entity.getContentLength();// -1 表示长度未知
//                String result = EntityUtils.toString(entity);
//                response.close();
//                // httpClient.close();
//                return result;
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//
//        return EMPTY_STR;
//    }
//    public static void printResponse(HttpResponse httpResponse)
//    	      throws ParseException, IOException {
//    	    // 获取响应消息实体
//    	    HttpEntity entity = httpResponse.getEntity();
//    	    // 响应状态
//    	    System.out.println("status:" + httpResponse.getStatusLine());
//    	    System.out.println("headers:");
//    	    HeaderIterator iterator = httpResponse.headerIterator();
//    	    while (iterator.hasNext()) {
//    	      System.out.println("\t" + iterator.next());
//    	    }
//    	    // 判断响应实体是否为空
//    	    if (entity != null) {
//    	      String responseString = EntityUtils.toString(entity);
//    	      System.out.println("response length:" + responseString.length());
//    	      System.out.println("response content:"
//    	          + responseString.replace("\r\n", ""));
//    	    }
//    	  }
//
//    public static void main(String []d){
//    	if(1==1){
//    		System.out.println("终止了. 防止误操作 废弃的main");
//    		return;
//    	}
////		client_id=FsYXds0gouXuOtlRFnYsdAjF8nysbRcp
////				connection=Username-Password-Authentication
////				password=3
////				popup=false
////
////
////
////
////
////
//
//
//	//	https://rapaport.auth0.com/usernamepassword/login
////?client_id=FsYXds0gouXuOtlRFnYsdAjF8nysbRcp&connection=Username-Password-Authentication&password=3&popup=false&redirect_uri=https%3A%2F%2Fmember.rapnet.com%2FLogin%2FLoginPage.aspx&response_type=code&scope=openid%20email&sso=false&tenant=rapaport&username=3
//		String urlstr="https://rapaport.auth0.com/usernamepassword/login?"
//				+ "client_id=FsYXds0gouXuOtlRFnYsdAjF8nysbRcp&connection=Username-Password-Authentication&"
//				+ "password=3&popup=false&redirect_uri=https%3A%2F%2Fmember.rapnet.com%2FLogin%2FLoginPage.aspx"
//				+ "&response_type=code&scope=openid%20email&sso=false&tenant=rapaport&username=3";
//		 // urlstr="https://rapaport.auth0.com/usernamepassword/login";
//
//		try {
//			//Header header=new Header() ;;
//
//			HttpPost post=new HttpPost(urlstr);
//	    	List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
//	    	param.add(new BasicNameValuePair("client_id", "FsYXds0gouXuOtlRFnYsdAjF8nysbRcp"));
//	    	param.add(new BasicNameValuePair("connection", "Username-Password-Authentication"));
//	    	param.add(new BasicNameValuePair("password", "335808!@#sh"));
//	     	param.add(new BasicNameValuePair("username", "90341"));
//	    	param.add(new BasicNameValuePair("popup", "false"));
//	    	param.add(new BasicNameValuePair("redirect_uri", "https://member.rapnet.com/Login/LoginPage.aspx"));
//	    	param.add(new BasicNameValuePair("response_type", "code"));
//	    	param.add(new BasicNameValuePair("scope", "openid email"));
//	    	param.add(new BasicNameValuePair("sso", "false"));
//	    	param.add(new BasicNameValuePair("tenant", "rapaport"));
//	    	UrlEncodedFormEntity he = new UrlEncodedFormEntity(param,"UTF-8");
//	    	post.setEntity(he);
//	    	post.addHeader("Accept", "text/html");
//	    	post.addHeader("Accept-Encoding", "gzip, deflate, br");
//	    	post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//	    //	post.addHeader("Auth0-Client", "eyJuYW1lIjoibG9jay5qcyIsInZlcnNpb24iOiIxMC44LjEiLCJsaWJfdmVyc2lvbiI6IjcuNi4xIn0");
//	    	post.addHeader("Connection", "keep-alive");
//	    //	post.addHeader("Content-Length", "254");
//	    	post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//	    	post.addHeader("Host", "rapaport.auth0.com");
//	    	post.addHeader("Origin", "https://member.rapnet.com");
//	    	post.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx");
//	    	post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
//
//	   // 	HttpClient hc=HttpClients.createDefault();
//	       	RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();//标准Cookie策略
//	    	CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();//设置进去
//
//	    //	HttpClientParams.setCookiePolicy(hc.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
//
//	    	HttpResponse response=httpClient.execute(post);
//	  // 	String set_cookie = response.getFirstHeader("Set-Cookie").getValue();
//	    	HttpEntity entity = response.getEntity();
//
//
//	    //	entity.getContent()
//	    	String html=EntityUtils.toString(entity);
//
//	        System.out.println("第一次链接开始---------------");
//	    	System.out.println(html);
//	        System.out.println("第一次链接结束---------------");
//
//	        Document doc = Jsoup.parse(html);
//	        String wa=doc.select("input[name=wa]").val();
//	        String wresult=doc.select("input[name=wresult]").val();
//	        String wctx=doc.select("input[name=wctx]").val();
//	        String formaction=doc.select("form[name=hiddenform]").get(0).attr("action");
//
//
//	        //创建一个新的链接
//	        HttpPost formpost=new HttpPost(formaction);
//	      	//List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
//	      //  param.clear();
//	        System.out.println("第2次链接kashi ---------------");
//	    	List<BasicNameValuePair> param2=new ArrayList<BasicNameValuePair>();
//	    	param2.add(new BasicNameValuePair("wa",wa));
//	    	param2.add(new BasicNameValuePair("wresult", wresult));
//	    	param2.add(new BasicNameValuePair("wctx", wctx));
//	    	UrlEncodedFormEntity he2 = new UrlEncodedFormEntity(param2,"UTF-8");
// 	    	formpost.setEntity(he2);
//
// 	    	formpost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
// 	    	formpost.addHeader("Accept-Encoding", "gzip, deflate, br");
// 	    	formpost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//	    //	post.addHeader("Auth0-Client", "eyJuYW1lIjoibG9jay5qcyIsInZlcnNpb24iOiIxMC44LjEiLCJsaWJfdmVyc2lvbiI6IjcuNi4xIn0");
// 	    	formpost.addHeader("Connection", "keep-alive");
//	    //	post.addHeader("Content-Length", "254");
// 	    	formpost.addHeader("Content-Type", "application/x-www-form-urlencoded");
// 	    	formpost.addHeader("Host", "rapaport.auth0.com");
// 	    	formpost.addHeader("Upgrade-Insecure-Requests", "1");
// 	    	formpost.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx?ReturnUrl=%2fRapNet%2fPriceGrid%2fGridResults.aspx");
// 	    	formpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
//
//	        HttpResponse formresponse2=httpClient.execute(formpost);
//
//
//	      System.out.println("第2次链接结束---------------");
//
//	     String loginpagahtml= EntityUtils.toString(formresponse2.getEntity());
//	     System.out.println(loginpagahtml);
//	     Document doc2 = Jsoup.parse(loginpagahtml);
//	     String href=doc2.select("a").get(0).html();
// System.out.println(href);
// // 使用jsoup将html里面的a标签里面的数据全部读取出来（假如想读取其他标签，直接将a改为其他标签名称即可，例如"img"）
//
// //GET /Login/LoginPage.aspx?code=tcexbqBj0dOARuSp HTTP/1.1
////		 Host: member.rapnet.com
////		 User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0
////		 Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
////		 Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3
////		 Accept-Encoding: gzip, deflate, br
////		 Referer: https://member.rapnet.com/Login/LoginPage.aspx
////		 Cookie: ASP.NET_Rapnet=0apbrmftgwoihoxwma4ztzh5; __qca=P0-122770300-1509960495037; __hstc=205636236.7f827a1691990dc18a92379ad24c4f0d.1509960557170.1510017595873.1510023285780.4; __hssrc=1; hubspotutk=7f827a1691990dc18a92379ad24c4f0d; CookieDeviceNumber=5130606; SnapABugHistory=1#; SnapABugVisit=7#1509962311; __hs_opt_out=no; SnapABugRef=https%3A%2F%2Fmember.rapnet.com%2FRapNet%2FPriceGrid%2FGridResults.aspx%20https%3A%2F%2Fmember.rapnet.com%2FRapNet%2FPriceGrid%2FGridResults.aspx; __hssc=205636236.1.1510023285780
////		 Connection: keep-alive
////		 Upgrade-Insecure-Requests: 1
//
//
//	        //创建一个新的链接
//	       HttpGet loginpagapost=new HttpGet(href);
//	       loginpagapost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//	       loginpagapost.addHeader("Accept-Encoding", "gzip, deflate, br");
//	       loginpagapost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
// 	       loginpagapost.addHeader("Connection", "keep-alive");
// 	 //     loginpagapost.addHeader("Content-Type", "application/x-www-form-urlencoded");
// 	     loginpagapost.addHeader("Host", "member.rapnet.com");
// 	    loginpagapost.addHeader("Upgrade-Insecure-Requests", "1");
// 	   loginpagapost.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx");
// 	  loginpagapost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
//
//	       HttpResponse loginpagaResponse=httpClient.execute(loginpagapost);
//
//	       String  loginpagaposttml= EntityUtils.toString(loginpagaResponse.getEntity());
//		     System.out.println("---输出第三次链接");
//		     File f2=new File("d:\\sys1.html");
//	         FileWriter fileWriter2 = new FileWriter(f2);
// 	            fileWriter2.write(loginpagaposttml);
// 	           fileWriter2.close();
//	      System.out.println("第4次链接开始--------------");
//	      HttpGet get=new HttpGet(new URI("https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx"));
//
//	      get.addHeader("Host", "member.rapnet.com");
//	      get.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/56.0");
//	      get.addHeader("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
//	      get.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//	      get.addHeader("Accept-Encoding", "gzip, deflate, br");
//	      get.addHeader("Referer", "https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx");
//	      get.addHeader("Upgrade-Insecure-Requests", "1");
//	      get.addHeader("Connection", "keep-alive");
//	      get.addHeader("Host", "member.rapnet.com");
// //	      Cookie	   ASP.NET_Rapnet=0apbrmftgwoihox…ssc=205636236.1.1509965724699
//	   //   String set_cookie2 = response.getFirstHeader("Set-Cookie").getValue();
//	    //  	System.out.println(set_cookie2);
//	   //   DefaultHttpClient httpclient2=new DefaultHttpClient();
//	   //   httpclient2.setCookieStore(cookiestore);
//	      //把第一次请求的cookie加进去
//	   //   HttpResponse response2=httpclient2.execute(httppost2);
//
//	      HttpResponse response3=httpClient.execute(get);
//	    //  printResponse(response3);
//	      String html2=EntityUtils.toString(response3.getEntity());
//	       //  System.out.println(html2);
//	         File f=new File("d:\\sys.html");
//	         FileWriter fileWriter = new FileWriter(f);
// 	            fileWriter.write(html2);
// 	           fileWriter.close();
//	      System.out.println("第4次链接结束---------------");
//
//
//
//	   HttpGet getdata=new HttpGet(new URI("https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx"));
//	   getdata.addHeader("Host", "member.rapnet.com");
//	   getdata.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/56.0");
//	   getdata.addHeader("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
//	   getdata.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//	   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
//	   getdata.addHeader("Referer", "https://member.rapnet.com/RapNet/");
//	   getdata.addHeader("Upgrade-Insecure-Requests", "1");
//	   getdata.addHeader("Connection", "keep-alive");
//	   getdata.addHeader("Host", "member.rapnet.com");
//	   System.out.println("数据获取开始--------------");
//	   HttpResponse responsedata=httpClient.execute(getdata);
//	   String datahtml3=EntityUtils.toString(responsedata.getEntity());
//       //  System.out.println(html2);
//         File fdata=new File("d:\\sysdata.html");
//         FileWriter fileWriterfdata = new FileWriter(fdata);
//         fileWriterfdata.write(datahtml3);
//         fileWriterfdata.close();
//	  System.out.println("数据获取结束--------------");
//
//
//
//
//
//
//
//
//	      HttpGet LogOut=new HttpGet(new URI("https://member.rapnet.com/Login/LogOut.aspx"));
//	      LogOut.addHeader("Host", "member.rapnet.com");
//	      LogOut.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/56.0");
//	      LogOut.addHeader("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
//	      LogOut.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//	      LogOut.addHeader("Accept-Encoding", "gzip, deflate, br");
//	      LogOut.addHeader("Referer", "https://member.rapnet.com/RapNet/");
//	      LogOut.addHeader("Upgrade-Insecure-Requests", "1");
//	      LogOut.addHeader("Connection", "keep-alive");
//	      LogOut.addHeader("Host", "member.rapnet.com");
//	      System.out.println("退出操作开始--------------");
//	      HttpResponse responseLogOut=httpClient.execute(LogOut);
//
//	  //  printResponse(response3);
//	      String html3=EntityUtils.toString(responseLogOut.getEntity());
//	       //  System.out.println(html2);
//	         File f3=new File("d:\\sysLogOut.html");
//	         FileWriter fileWriter3 = new FileWriter(f3);
// 	            fileWriter3.write(html3);
// 	           fileWriter3.close();
// 	          System.out.println("退出操作结束--------------");
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//	}
//
//}