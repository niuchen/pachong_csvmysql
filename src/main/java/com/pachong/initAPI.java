package com.pachong;

import java.io.FileInputStream;
import java.util.Properties;

public    class initAPI {
   // public static String filepath="";//存放下文件路径
    public static String csvpath="";//存储csv文件路径
    public static String jdbcurl="";
    public static String user="";
    public static String        password="";
    public static String  DataSourcename="";
    public static String Driver="";
    static {
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/initAPI.properties");
            pro.load(in);
          //  filepath=  pro.getProperty("filepath");
            csvpath= pro.getProperty("csvpath");
            jdbcurl= pro.getProperty("jdbcurl");
            user= pro.getProperty("user");
            password= pro.getProperty("password");
            DataSourcename= pro.getProperty("DataSourcename");
            Driver= pro.getProperty("Driver");
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
