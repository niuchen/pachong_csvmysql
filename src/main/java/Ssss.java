import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import au.com.bytecode.opencsv.CSVWriter;

public class Ssss {
    public static void main(String[] args) throws Exception {  
        new Ssss().test1();  
          
        //System.getProperties().list(System.out);  
    }  
      
    public void test1() throws Exception {  
          
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("d:\\sa2.csv"), Charset.forName("GBK"));  
          
        // CSVWriter writer = new CSVWriter(new FileWriter("c:\\yourfile.csv"), ',');  
        CSVWriter writer = new CSVWriter(out, ',');  
         // feed in your array (or convert your data to an array)  
        String c = "fir\"中国#人民,ond#政府\"ird";  
  
          
         String[] entries = c.split("#");  
         writer.writeNext(entries);  
        writer.close();  
          
        System.out.println("over");  
    }  
}
