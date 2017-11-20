//package com.pachong.uilt;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.util.UUID;
//
//import com.wamei.util.ImageUtil;
//import com.wamei.util.JsonResponseHelper;
//import com.wamei.util.SystemConstants;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.log4j.Logger;
//
//import javax.imageio.ImageIO;
//import javax.servlet.http.HttpServletRequest;
//
///**
// * Created by qixuan.chen on 2016/8/18.
// */
//public class ImageDownloadUtil {
//
//    private static final Logger logger = Logger.getLogger(ImageDownloadUtil.class);
//
//    public static String download(HttpServletRequest request,String url, String savePath, Integer width, Integer height) {
//        HttpClient httpclient = new DefaultHttpClient();
//        String picSrc = "";
//        String picType = url.substring(url.lastIndexOf(".")+1,url.length());
//        String fileName = UUID.randomUUID().toString().replace("-", "")+"."+picType;
//        String path = request.getSession().getServletContext().getRealPath(savePath+fileName);
//        File storeFile = null;
//        try {
//            HttpGet httpget = new HttpGet(url);
//
//            //伪装成google的爬虫
//            httpget.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
//            // Execute HTTP request
//            logger.info("executing request: " + httpget.getURI());
//            HttpResponse response = httpclient.execute(httpget);
//            storeFile = new File(path);
//            FileOutputStream output = new FileOutputStream(storeFile);
//
//            // 得到网络资源的字节数组,并写入文件
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream instream = entity.getContent();
//                try {
//                    byte b[] = new byte[1024];
//                    int j = 0;
//                    while( (j = instream.read(b))!=-1){
//                        output.write(b,0,j);
//                    }
//                    output.flush();
//                    output.close();
//                } catch (IOException ex) {
//                    // In case of an IOException the connection will be released
//                    // back to the connection manager automatically
//                    throw ex;
//                } catch (RuntimeException ex) {
//                    // In case of an unexpected exception you may want to abort
//                    // the HTTP request in order to shut down the underlying
//                    // connection immediately.
//                    httpget.abort();
//                    throw ex;
//                } finally {
//                    // Closing the input stream will trigger connection release
//                    try { instream.close(); } catch (Exception ignore) {}
//                }
//                if (storeFile.exists()) {
//                    BufferedImage newImage = ImageUtil.getFileImage(storeFile, width, height);
//                    ImageIO.write(newImage, picType, storeFile);
//                    picSrc = "http://"+ JsonResponseHelper.serverAddress+"/wamei/"+savePath+fileName;
//                }
//
//            }
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            httpclient.getConnectionManager().shutdown();
//        }
//
//
//        return picSrc;
//
//    }
//
//    public static void main(String[] args) throws MalformedURLException {
//        //抓取下面图片的测试
//        //ImageDownloadUtil.download("http://blog.goyiyo.com/wp-content/uploads/2012/12/6E0E8516-E1DC-4D1D-8B38-56BDE1C6F944.jpg", "d:/aaa.jpg");
//    }
//}