package com.pachong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.pachong.uilt.SQLHelper;

import au.com.bytecode.opencsv.CSVReader;

/**文件入库**/
public class CsvMysqlMain {
	
	
	
    
    
	public static ArrayList<File> filelist=new ArrayList<File>();
    public static void getFileList(String strPath) {
    	
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                	 System.out.println("目录:"+files[i].getName());
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                   
                } else if (fileName.endsWith("csv")) { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println("文件:" + strFileName);
                    filelist.add(files[i]); 
                } else {
                    continue;
                }
            }
         }else{
        	 System.out.println("错误");
         }
       
        
    }
	public static void main(String[] d){
		
        String path = "D:\\spaga"; // 路径
 		CsvMysqlMain.getFileList(path);
 		List<File> filepathlsit=CsvMysqlMain.filelist;
 		System.out.println("文件数"+filepathlsit.size());
		 SQLHelper sqlexe=new SQLHelper();
		 try {
			 for (int i = 0; i < filepathlsit.size(); i++) {
				// File file = new File("D:\\spaga\\ctl00_cphMainContent_repGrids_ctl00_gvGrid\\ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value@9,650 -52%\\1.csv");  
				  //  FileReader fReader = new FileReader(file);  
				//    reader = new CSVReader(new FileReader(file));  
				    InputStreamReader   filereader2 = new InputStreamReader(new FileInputStream(filepathlsit.get(i)), Charset.forName("GBK"));
			        CSVReader csvReader = new CSVReader(filereader2);
			//+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				  //  String[] strs = csvReader.readNext();  
				 
			 //读取csv文件的全部数据 并插入数据库
				    List<String[]> list = csvReader.readAll();  
				   int shu=1;
				    for(String[] ss : list){  
				    	if(shu==1){//第一行是标题不用插入
				    		shu++;continue;
				    	}
				    	String sql="INSERT INTO products2 (`p1`, `p2`, `p3`, `p4`, `p5`, `p6`, `p7`, `p8`, `p9`, `p10`, `p11`, `p12`, `p13`, `p14`, `p15`, `p16`, `p17`, `p18`, `p19`, `p20`, `p21`, `p22`, `p23`, `p24`, `p25`, `p26`, `p27`, `p28`, `p29`, `p30`, `p31`, `p32`, `p33`, `p34`, `p35`, `p36`, `p37`, `p38`, `p39`, `p40`, `p41`, `p42`, `p43`, `p44`, `p45`, `p46`) ";
				    	   sql+="values (";
				        for(String s : ss)  {
				        	sql+="?,";    
				        }
				        sql=sql.substring(0, sql.length()-1);
				        sql+=")";
				      
				        boolean b=sqlexe.update(sql,ss);
				        System.out.println(shu+"值:"+sql);  
				        if(!b){
				        	  System.out.println("异常!");  
				        	break;
				        }else{
				        	  System.out.println("执行成功!");  
				        }
				        shu++;
				    }  
				    csvReader.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		 //	sql.update("");
	}
}
