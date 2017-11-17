package com.pachong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.swing.text.AbstractDocument.BranchElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ApiurlMain implements Job{
	public static Log logger = LogFactory.getLog(ApiurlMain.class);  //日志	
	
	

	
	
	
	

	/***采集具体执行方法  int是返回执行是否成功  0失败,  1成功**/
	public static int zhixing(){
		String tuchuurl="";
		CloseableHttpClient client=null;
		int isreunt=0;
		 try {
			 client=HttpClientUtil.getHttpClient();
			 /**-----------登陆开始-------------***/
			HttpPost loginpost= Headerpachong.newloginPost1();
			CloseableHttpResponse  response=client.execute(loginpost);//登陆
			 tuchuurl=loginpost.getURI().toString();
			 String newloginPosthtml= EntityUtils.toString(response.getEntity());//获取登陆返回的回调方法数据
			 logger.error("登陆状态:"+response.getStatusLine().getStatusCode() );
			 
			 
			 //response.close();
			 //  printResponse(response);
			 //	 logger.error("第一条cookies是" + response.getFirstHeader("set-cookie"));
			 //	 Header[] hs = response.getHeaders("Set-Cookie");
 			 HttpPost callbackpost= Headerpachong.newcallback2(newloginPosthtml);//生成提交执行回调数据url
 			 /*****$$$$$$$$$登陆结束$$$$$$$$$$$***/

 			 /**-----------执行回调读取令牌      开始-------------***/
 			CloseableHttpResponse  response2=client.execute(callbackpost);//执行提交执行回调数据url
			 tuchuurl=callbackpost.getURI().toString();
			 String loginpagahtml= EntityUtils.toString(response2.getEntity());//得到paga注册code 和注册url
			 logger.error("loginpagahtml值"+loginpagahtml);
			 int statuscode = response2.getStatusLine().getStatusCode();
			 Header header=response2.getFirstHeader("location");
		 	// response2.close();
			 logger.error("回调状态:"+statuscode);
			 /**-----------执行回调读取令牌      结束-------------***/
			// printResponse(response2);
			 CloseableHttpResponse  response3=null;
			 HttpGet redirect=null;
 			 if(statuscode==302){
				// 读取新的 URL 地址 
				
				   if (header!=null){
				      String newuri=header.getValue();
				   //   callbackpost.abort();
				   //   client.
 				     // client=HttpClientUtil.getHttpClient();
				      redirect=Headerpachong.newloginpaga3(newuri);
 				     //    callbackpost.abort();  //终止端口
  				        logger.error("第一次重定向:"+newuri);
				         response3= HttpClientUtil.httpGetRequest(client, redirect) ;
				         if(response3==null){
				        	 throw new Exception("CloseableHttpResponse 多次链接失败.空");
				         }
				    //     client.execute(redirect);
				     //  printResponse(response3);		        
				   }else{
					  logger.error("偷空");
				   }
			 }else{
				logger.error("返回的不是重定向");
			 }
		 
			 /**-----------访问首页      开始-------------***/
			 /**二次重定向****/
 			
 			int rep3status=response3.getStatusLine().getStatusCode();
 			CloseableHttpResponse  redirect2response=null;
			 HttpGet redirect2 =null;
			 if(rep3status==302){
				 String rep3header=response3.getFirstHeader("location").getValue();
 		        logger.error("第二次重定向:"+rep3header);
		           redirect2 =Headerpachong.newloginpaga3(Headerpachong.root+rep3header);
		           //   redirect.abort();
		           //   client.close();
				    //  client=HttpClientUtil.getHttpClient();
		       //    response3.close();
		           redirect2response= HttpClientUtil.httpGetRequest(client, redirect2) ;
			         if(redirect2response==null){
			        	 throw new Exception("CloseableHttpResponse 多次链接失败.空");
			         }
		         //  redirect2response= client.execute(redirect2);
		          
			 }else{
				 logger.error(" 登陆失败.  第二次转发首页不是302");
				 return 0;
			 }
//  			  HttpGet pagapost=Headerpachong.newloginpaga3(loginpagahtml);//生成访问首页
//  			 HttpResponse  response3=client.execute(pagapost);//执行访问首页
// 			 tuchuurl=pagapost.getURI().toString();
 			 String pagaponse= EntityUtils.toString(redirect2response.getEntity());//得到paga注册code 和注册url
 		//	 redirect2response.close();
  			 //redirect2.abort();  //终止端口
 			 //System.out.println("pagaponse:"+pagaponse);
//			 logger.error("访问首页状态:"+response3.getStatusLine().getStatusCode()+tuchuurl );
			// Headerpachong.newfile("d:\\s1.html", pagaponse);
			 if(pagaponse.indexOf("交易屏")==-1){
				 logger.error("注册登陆失败. 访问首页 没有找到交易屏关键字    终止.");
				 if(pagaponse.indexOf("RapNetMainContent")!=-1){
					 logger.error("账号重复登录! 只能稍后等待了");
 				 }
				 return 0;
			 } 

			 
			 /**-----------访问首页      结束-------------***/
//			 Header[] header=response.getAllHeaders();
//			 for(Header hobj:header){
//				logger.error(hobj.getName()+":"+hobj.getValue());
//			 }
			 
			 /**-----------访问交易屏页面     starte-------------***/
			 HttpGet datapost1= Headerpachong.newpagedata4(tuchuurl);//数据屏的页面首页
		 
			// CloseableHttpResponse pingdateresponse4=client.execute(datapost1);//执行数据屏
			 CloseableHttpResponse pingdateresponse4= HttpClientUtil.httpGetRequest(client, datapost1) ;
	         if(pingdateresponse4==null){
	        	 throw new Exception("CloseableHttpResponse 多次链接失败.空");
	         }
			 tuchuurl=datapost1.getURI().toString();
			 logger.error("交易屏状态:"+pingdateresponse4.getStatusLine().getStatusCode() );
			 String datahrml= EntityUtils.toString(pingdateresponse4.getEntity());//得到paga注册code 和注册url
		//	 Headerpachong.newfile("d:\\s2.html", datahrml);
 			//#####
			 pingdateresponse4.close();
			 /**-----------访问交易屏页面     ent-------------***/
 
			 
			 /**-----------访问 交易屏  某一个交易单元格   打卡数据列表     starte-------------***/
			  Document doc2 = Jsoup.parse(datahrml); //交易盘的页面解析器
 			
 			  String [] tableidzi=new String []{"ctl00_cphMainContent_repGrids_ctl00_gvGrid",
					  "ctl00_cphMainContent_repGrids_ctl02_gvGrid","ctl00_cphMainContent_repGrids_ctl04_gvGrid"
					  ,"ctl00_cphMainContent_repGrids_ctl06_gvGrid","ctl00_cphMainContent_repGrids_ctl08_gvGrid"
					  ,"ctl00_cphMainContent_repGrids_ctl08_gvGrid","ctl00_cphMainContent_repGrids_ctl10_gvGrid",
					  "ctl00_cphMainContent_repGrids_ctl12_gvGrid"
			  				  };//第1个表
			  /*****这个是循环一个表的输出开始******/
			  for(int tableshu=0;tableshu<tableidzi.length;tableshu++){
  			  //String lblColor="ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl%_lblColor";//这个存储的是一行数据的集合 总共11行
 			//  String tdid="ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value";//表中一行的的一个单元格
			  String tablehtml=doc2.getElementById(tableidzi[tableshu]).html();//读取一个表的html
			  Document href0doc = Jsoup.parse(tablehtml); 
			//  for(int lbl=0;lbl<11;lbl++){
				Elements spenel=  href0doc.select("a");//获取一个表的全部单元格
			 	/*****这个是循环一个表的全部的单元格 输出开始******/
				for(Element objspen:spenel){
					String cid=objspen.attr("id");//单元格的id
					String ahref=objspen.attr("href");//单元格的链接
					String ctext=objspen.text();//单元格的内容
					if((cid+"@"+ctext).equals("ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value@9,650 -52%")){
						continue;
					}
					if((cid+"@"+ctext).equals("ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_VVS1_Value@8,125 -49%")){
						continue;
					}
  				//	String ahref=anode.attr("href");//得到第1个单元格的点击url地址
	 	    		 String ahrefurl[]=ahref.split("\\."+"\\."+"/");//要把路径的../截取掉.
	 	    		 if(ahrefurl.length!=2){
		    			 logger.error(ahrefurl.length+"--------表格里的 a标签的href链接截取错误"+ahref);
	 	    		 }
	 	    		String ahrefu="https://member.rapnet.com/RapNet/"+ahrefurl[1];//拼接出第一个表格单元格的链接
		    		System.out.println(" 一个单元格url地址:"+ahrefu);
		    		  HttpGet newpagetable= Headerpachong.newpagetable(ahrefu,tuchuurl);//访问交易屏中  表格的某个单元格  读取单元格列表
		    		  
		    		  CloseableHttpResponse pingdatapageresponse=client.execute(newpagetable);//执行数据屏一个单元格
		  			// tuchuurl=newpagetable.getURI().toString();
		  			 logger.error("访问交易屏中  表格的某个单元格  读取单元格列表状态:"+pingdatapageresponse.getStatusLine().getStatusCode() );
		  			 String newpagetablehrml= EntityUtils.toString(pingdatapageresponse.getEntity());//得到paga注册code 和注册url
		  		 	pingdatapageresponse.close();
		  			// Headerpachong.newfile("d:\\s3.html", newpagetablehrml);
			    	 /**-----------访问 交易屏  某一个交易单元格   打开数据列表   ent-------------***/ 
		  	 
		  			 
		  			 /**--------------开始i执行第一页的列表数据的解析存储。 包括 图片文件的接口地址， 产品详细数据-----------****/
		  		   Document newpagetablehrmldoc = Jsoup.parse(newpagetablehrml); 
				  String headid=newpagetablehrmldoc.select("head").get(0).attr("id").split("_")[0];//获取head的内容当前查询的ctl 是多少  表示属于那个大模块  head表情的id有一个
				  Integer pagecount=0;
				  String PageCoun=null;
				  if(newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageCount")!=null){
			  		     PageCoun=newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageCount").text();//总共有几页   如果是空证明就一页
  				  } 
	  	  		  //  String pagesun=newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageNumBottom").text();//第几页数据
	  	  		 	if(PageCoun==null||"".equals(PageCoun)){  pagecount=0;	}else{
	  	  		 		pagecount=Integer.valueOf(PageCoun);
	  	  		 		if(pagecount>5){	  pagecount=5;  //值读取分页5页内的数据.
	  	  		 		}
	  	  		 	}
	  	  		String pagesun="0";
	  	  		 	if(newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageNumBottom")!=null){
	  	  		 	/**如果文件不存在  就输出第一页的文件**/
	  	  			  pagesun=newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageNumBottom").text();//第几页数据
	  	  		 	}
	  	  		
 	  	  		String filename="d:\\spaga\\"+tableidzi[tableshu]+"\\"+cid+"@"+ctext+"\\"+pagesun+".csv";
	 			if(!new File(filename).exists()){
	 				 //这个是第一页的数据 第一页进入列表时已经有了, 不用再次读取了. 
		  			 Headerpachong.OutputPage(tableidzi[tableshu], cid+"@"+ctext, newpagetablehrmldoc, client);
	 			}else{
	 				 logger.error("文件已经存在跳过第一页:"+filename);
	 			}
	  	   
	  	  		 		
	 	  		 	
		  			 
	  	 	    	 /**----------- 执行 数据列表   翻页     starte- 从第二页开始------------***/ 
	  	  		 	for (int i = 2; i <=pagecount; i++) {
	  	  		 		try {
	  	  	  		 	/**如果文件存在  就跳过翻页. 避免重复下载.**/
 	  	   	  	  		  filename="d:\\spaga\\"+tableidzi[tableshu]+"\\"+cid+"@"+ctext+"\\"+i+".csv";
		  	  	 			if(new File(filename).exists()){
		  	  	 				 logger.error("文件已经存在跳过翻页:"+filename);
		  	  	 				 continue;
		  	  	 			}
							logger.error(i+"翻页开始");
							HttpPost newTurnthepage=Headerpachong.newTurnthepage(newpagetablehrml, tuchuurl,i);
							 tuchuurl=  newTurnthepage.getURI().toString();
							// CloseableHttpResponse responsefanye =client.execute(newTurnthepage);//执行翻页
							 CloseableHttpResponse responsefanye= HttpClientUtil.httpPostRequest(client, newTurnthepage) ;
			 	   	         if(responsefanye==null){
			 	   	        	 throw new Exception("CloseableHttpResponse 多次链接失败.空");
			 	   	         }
							 String newpagetablehrml2= EntityUtils.toString(responsefanye.getEntity());//翻页过的列表数据 得到paga注册code 和注册url
	 						 logger.error(i+"翻页状态"+ responsefanye.getStatusLine().getStatusCode()+":");
						//	 Headerpachong.newfile("d://"+i+"sas.html", newpagetablehrml2);
							   Document newfanyedoc = Jsoup.parse(newpagetablehrml2); 
							//输出分页数据的csv文件
							 Headerpachong.OutputPage(tableidzi[tableshu], cid+"@"+ctext,newfanyedoc, client);
	 						responsefanye.close();
						} catch (Exception e) {
	 						e.printStackTrace();
	 						logger.error(i+"翻页异常"+tuchuurl+e);
						}
			  			//Headerpachong.newfile("d:\\s4.html", newpagetablehrml2);
			  			//https://member.rapnet.com/RapNet/Search/Results.aspx?Code=dqkPeRM4L7zAMOV%2fIQAfkw%3d%3d&SearchSessionID=149113546
 					} /**----------- 执行 数据列表   翻页     end- 从第二页结束------------***/ 
				}	/*****这个是循环一个表的全部的单元格 输出结束******/
			//  }
			  }/*****这个是循环一个表的输出结束******/
			 isreunt=1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}finally {
			if(client!=null){
				try {
					 HttpGet logout= Headerpachong.logout99(tuchuurl);//退出登陆 方式占用不释放下次报错.
					 CloseableHttpResponse response=client.execute(logout);//执行退出登陆
					 logger.error("退出登陆:"+response.getStatusLine().getStatusCode() );
					 logger.error(""+response.toString() );
					 logger.error("");
					 logger.error("");
					 response.close();
					client.close();
				} catch (IOException e) {
					isreunt=0;
 					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
 				}
			}
		}
//		 if(isreunt!=1){
//			logger.error("递归调用 因为有没有通过的操作.");
// 			  Apiurl.zhixing();
//		 }else{
		     return isreunt;
		// }
	}
	private static void sysh(Header[] he3){
		for(Header h:he3)
		System.out.println(h.getName()+" : "+h.getValue());
	}
	/**测试main方法**/
	public static void main(String[] args) {
		ApiurlMain ap=new ApiurlMain();
		try {
	 ap.execute(null);
//			String d="../Search/Results.aspx?C";
//			String ddd[]=d.split("\\."+"\\."+"/");
//			System.out.println(ddd.length);
//			System.out.println(ddd[0]);
//			System.out.println(ddd[1]);
			//System.out.println(ddd[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/***定时任务的执行方法. quartz定时器.这里的main不在是线上应用的入口 定时器的main才是 ***/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		if(1==1){
//			System.out.println("ddd");
//			return;
//		}
		long start,end;
		start = System.currentTimeMillis();
		 logger.error("开始爬虫~~~~~~~~~~~~~~~~~~爬虫开始时间:" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start)+"");

	   int isreturn=	0;
	   int i=1;
	   while(isreturn==0){
		   if(i>1){
			   logger.error("~~~~~~~~~~~~~~~~~~执行有异常重复执行.第"+i+"次");
		   } 
		   isreturn= ApiurlMain.zhixing();//如果状态是0 就一直执行 0表示没有完成.
		   if(isreturn==1){
			   break;
		   }
		   i++;
		   if(i%3==0){//每执行3次 就等待10分钟在执行三次.知道状态=1正常了.
			   try {
				   logger.error(i%3+"~~~~~~~~~~~~~~~~~~等待10分钟.第"+i+"次");
				TimeUnit.MINUTES.sleep(10);//10分钟
				 
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
		   }
		   if(i==100){ 
			   logger.error("~~~~~~~~~~~~~~~~~~重复次数过多,停止此次采集.第"+i+"次");
			   break;
		   }
		   break;
	   }
 		 end = System.currentTimeMillis();  
		 logger.error("结束爬虫~~~~~~~~~~~~~~~~~~爬虫开始时间:" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start) + "; ~爬虫结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end)+ "; Run Time:" + (end - start)/1000 + "(s)");
		
	}

	 public static void printResponse(HttpResponse httpResponse)
	            throws ParseException, IOException {
	        // 获取响应消息实体
	        HttpEntity entity = httpResponse.getEntity();
	      
	        // 响应状态
	       logger.error("status:" + httpResponse.getStatusLine());
	       logger.error("headers:");
	        HeaderIterator iterator = httpResponse.headerIterator();
	        while (iterator.hasNext()) {
	        	Header h=(Header) iterator.next();
	           logger.error("\t" + h.getName());
	           logger.error("\t" + h.getValue());
	           logger.error("\t" + h.getName());
	            
	        }
	        // 判断响应实体是否为空
	        if (entity != null) {
//	            String responseString = EntityUtils.toString(entity);
//	           logger.error("response length:" + responseString.length());
//	           logger.error("response content:"
//	                    + responseString.replace("\r\n", ""));
	        }
	    }
    public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	 //从响应信息中获取cookie
    public static String setCookie(HttpResponse httpResponse)
    {
       logger.error("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length==0)
        {
           logger.error("----there are no cookies");
            return null;
        }
        String cookie = "";
        for (int i = 0; i < headers.length; i++) {
            cookie += headers[i].getValue();
            if(i != headers.length-1)
            {
                cookie += ";";
            }
        }
 
        String cookies[] = cookie.split(";");
        for (String c : cookies)
        {
            c = c.trim();
            if(cookieMap.containsKey(c.split("=")[0]))
            {
                cookieMap.remove(c.split("=")[0]);
            }
            cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "":(c.split("=").length ==2?c.split("=")[1]:c.split("=",2)[1]));
        }
       logger.error("----setCookieStore success");
        String cookiesTmp = "";
        for (String key :cookieMap.keySet())
        {
            cookiesTmp +=key+"="+cookieMap.get(key)+";";
        }
 
        return cookiesTmp.substring(0,cookiesTmp.length()-2);
    }
    public static Map<String,String> analysis(String url) {
   	 Map<String, String> paramMap = new HashMap<String, String>();
       paramMap.clear();
       if (!"".equals(url)) {// 如果URL不是空字符串
           url = url.substring(url.indexOf('?') + 1);
           String paramaters[] = url.split("&");
           for (String param : paramaters) {
               String values[] = param.split("=");
               paramMap.put(values[0], values[1]);
           }
       }
       return paramMap;
   }
}
