package com.pachong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import au.com.bytecode.opencsv.CSVWriter;

public class Test {

	public static void main(String[] args) {
  
 		File f =new File("D:\\spaga\\ctl00_cphMainContent_repGrids_ctl00_gvGrid\\ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value\\70657026_1.html");
		System.out.println(f.toString());
		FileReader reader;String str="";
		try {
			reader = new FileReader(f);
			BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
	        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
	        String s = "";
	        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
	            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
	         //   System.out.println(s);
	        }
	        bReader.close();
	          str = sb.toString();
	      //  System.out.println(str );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//定义一个fileReader对象，用来初始化BufferedReader
		String id="02";
		 Document newpagetablehrmldoc = Jsoup.parse(str); 
		// String pid=newpagetablehrmldoc.getElementById("ctl00_cphMainContent_gvResults_ctl"+id+"_hidDiamondID").val();//产品id
		// System.out.println(pid);
//    		String file= "ctl00_cphMainContent_gvResults_ctl"+id+"_lblCertFile";//获取页面里的证书访问的路径
//    		String imager=	"ctl00_cphMainContent_gvResults_ctl"+id+"_lblImageFile";//获取页面图片访问的路径
//    		Elements filee=newpagetablehrmldoc.getElementById(file).select("a");
//    		Elements imagere=newpagetablehrmldoc.getElementById(imager).select("a");
//    		 System.out.println(newpagetablehrmldoc.getElementById(file).select("a").get(0).attr("heft"));
//    		 System.out.println(newpagetablehrmldoc.getElementById(imager));
		Elements es= newpagetablehrmldoc.select("td[class*=CellValue]");
			String toptitle[]=new String []{
					"形状","报告日期","尺寸","测量","颜色","底面","净度","腰围","切口","顶点",
					"抛光","展位","对称","处理","荧光","标题","深度 %","比率","表 %","星型刻面长",
					"Report Shape","认证评论","主要特征","Lot #","可用性","区域位置","Escrow","一个图片","评级通过特定","图片"
					,"联系方式信息","品牌","Shade","Lab location","已更新时间","Inclusions","会员评论"};
			
			
		 
System.out.println(es.size());
		int i=0;
		for(Element e:es){
			 System.out.println(i+":"+toptitle[i]+":"+e.text());
			 i++;
		}
		
		if(1==1)return;
		// TODO Auto-generated method stub
		String url="https://member.rapnet.com/RapNet/Search/GetImageFile.aspx?LotID=83097553&FileType=IMAGE";
		   HttpGet getdata=null;
			getdata = new HttpGet(url);
		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		//   getdata.addHeader("Referer", Referer);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   CloseableHttpClient client=null;
		 
				 try {
					       client=HttpClientUtil.getHttpClient();
					       HttpResponse impes=client.execute(getdata);//执行图片真是路径
						// 读取新的 URL 地址 
						   Header header=impes.getFirstHeader("Location");
						 if(header!=null){
							 String newuri=header.getValue();
						     System.out.println("图片地址:"+impes.getFirstHeader("Location").getValue());
						 }else{
							 System.out.println("图片地址空");
						 }
						 String newloginPosthtml= EntityUtils.toString(impes.getEntity());
						 System.out.println(newloginPosthtml);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   
		 
	}

}
