package com.pachong;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.HttpGet;

/**
 * 操作状态类
 * 主要用于service层返回给controller业务执行结果的通用对象 
 *  
   * result 操作结果0 "未知",1"成功",2"失败",3"异常"
   * reuslt_err 操作异常
   * reuslt_hint 返回提示语句
   * reuslt_value 返回的对象
   * **/   
 public class OperateState {
	/**
	 * 操作结果0 "未知",1"成功",2"失败",3"异常"1
	 * **/
  private  int result;
  @Override
	public String toString() {
		return "OperateState [result=" + result + ", reuslt_err=" + reuslt_err + ", reuslt_hint=" + reuslt_hint
				+ ", reuslt_value=" + reuslt_value + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
public static final String resultStr[]={"未知","成功","失败","异常"};
  /**
   * 异常内容
   * **/
  private String reuslt_err;//操作异常
  /**
   * 返回提示语句
   * */
  private String reuslt_hint;//返回提示语句.
  
  /**
   * 返回的对象
   * **/
  private Object reuslt_value;//执行DAO返回的对象。
  
  private long startTime = 0L;//开始时间
  private long endTime = 0L;//结束时间
  /**
   * result 操作结果0 "未知",1"成功",2"失败",3"异常"
   * reuslt_err 操作异常
   * reuslt_hint 返回提示语句
   * reuslt_value 返回的对象
   * **/
  public OperateState(int result, String reuslt_err,String reuslt_hint,Object reuslt_value){
	  this.result=result;
	  this.reuslt_err=reuslt_err;
	  this.reuslt_hint=reuslt_hint;
	  this.reuslt_value=reuslt_value;
   }
 
  public OperateState(int result, String reuslt_err,String reuslt_hint,Object reuslt_value,Long startTime,Long endTime){
	  this.result=result;
	  this.reuslt_err=reuslt_err;
	  this.reuslt_hint=reuslt_hint;
	  this.reuslt_value=reuslt_value;
	  this.startTime=startTime;
	  this.endTime=endTime;
    }
public long getStartTime() {
	return startTime;
}

public void setStartTime(long startTime) {
	this.startTime = startTime;
}

public long getEndTime() {
	return endTime;
}

public void setEndTime(long endTime) {
	this.endTime = endTime;
}

public Object getReuslt_value() {
	return reuslt_value;
}
public void setReuslt_value(Object reuslt_value) {
	this.reuslt_value = reuslt_value;
}
public int getResult() {
	return result;
}
public void setResult(int result) {
	this.result = result;
}
public String getReuslt_err() {
	return reuslt_err;
}
public void setReuslt_err(String reuslt_err) {
	this.reuslt_err = reuslt_err;
}
public String getReuslt_hint() {
	return reuslt_hint;
}
public void setReuslt_hint(String reuslt_hint) {
	this.reuslt_hint = reuslt_hint;
}
public static String[] getResultstr() {
	return resultStr;
}
 
public static Map analysis(String url) {
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

 
public static void main(String[] args) {
	String efdurl="https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx"
			+ "?DiamondID=%&Page=1&RowID=0&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0";
	System.out.println(efdurl.replaceAll("[%]", "444444") );
 
   Map map= analysis(efdurl);
    System.out.println("name = " + map.get("DiamondID"));
    System.out.println("id = " + map.get("Page"));
		if(1==1)return  ;		  
 try {
	URL u=new URL("%2fError%2f404.aspx%3faspxerrorpath%3d%2fRapNet%2fResults.aspx");
	System.out.println(u.toString());
} catch (MalformedURLException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
	   int isreturn=	0;
	   int i=1;
	   while(isreturn==0){
 		   i++;
		   if(i%10==0){
			   System.out.println(i);
			   try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			   1 Thread.currentThread().sleep(1000);
//
//			   1 TimeUnit.DAYS.sleep(1);//天
//			   2 TimeUnit.HOURS.sleep(1);//小时
//			   3 TimeUnit.MINUTES.sleep(1);//分
//			   4 TimeUnit.SECONDS.sleep(1);//秒
//			   5 TimeUnit.MILLISECONDS.sleep(1000);//毫秒
//			   6 TimeUnit.MICROSECONDS.sleep(1000);//微妙
//			   7 TimeUnit.NANOSECONDS.sleep(1000);//纳秒
		   }
	   }
	}
}
