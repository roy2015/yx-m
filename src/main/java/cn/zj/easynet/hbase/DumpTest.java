package cn.zj.easynet.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import cn.zj.easynet.device.http.HttpClientPool;
import cn.zj.easynet.util.EncryptUtil;

public class DumpTest {
	private static final Logger logger = Logger.getLogger(DumpTest.class);
	private static final HbaseTest hbaseUtil = new HbaseTest();
	
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	
	public static final String CHARSET_ISO88591 = "ISO-8859-1";
	
	
	public static BufferedReader getBufferReaderFromFile(File file,  
            String charset) throws FileNotFoundException {  
        InputStream ss = new FileInputStream(file);  
        InputStreamReader ireader;  
        BufferedReader reader = null;  
        try {  
            if (charset == null) {  
                ireader = new InputStreamReader(ss,  
                		CHARSET_ISO88591);  
            } else {  
                ireader = new InputStreamReader(ss, charset);  
            }  
            reader = new BufferedReader(ireader);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return reader;  
    }
	
	public static String getFullContent(BufferedReader reader) {  
        StringBuilder sb = new StringBuilder();  
        String readedLine = null;  
        String param = "";
        String ret = "";
        int i=0;
        
        try {  
            while ((readedLine = reader.readLine()) != null) {  
        		String[] strArry = readedLine.split("\\s+");
        		
        		String userId = strArry[0];
        		String phone = strArry[1];
        		String rowKey = userId + phone;
        		
        		hbaseUtil.insertData("yx_member", rowKey, "uinfo", "userId", userId);
        		hbaseUtil.insertData("yx_member", rowKey, "uinfo", "phone", phone);
        		
        		
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  finally {  
            try {  
                reader.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        String content = sb.toString();  
        int length_CRLF = LINE_SEPARATOR.length();  
        if (content.length() <= length_CRLF) {  
            return content;  
        }  
        return content.substring(0, content.length() - length_CRLF);//  
    } 
	
	public static String getFullContent(File file, String charset) {  
        BufferedReader reader = null;  
        if (!file.exists()) {  
            System.out.println("getFullContent: file(" + file.getAbsolutePath()  
                    + ") does not exist.");  
            return null;  
        }  
        if (charset == null) {  
             charset = CHARSET_ISO88591;  
         }  
         try {  
             reader = getBufferReaderFromFile(file, charset);  
             return getFullContent(reader);  
         } catch (FileNotFoundException e1) {  
             e1.printStackTrace();  
         } finally {  
             if (null != reader) {  
                 try {  
                     reader.close();  
                 } catch (IOException e) {  
                     e.printStackTrace();  
                 }  
             }  
         }  
         return null;  
     } 
	
	public static void test1() throws ClientProtocolException, IOException{
		hbaseUtil.createTable("yx_member");
	}

	public static void test2() throws ClientProtocolException, IOException{
		
		String filepath = "D:\\54.txt";
		String str = getFullContent(new File(filepath), "UTF-8");
	}
	
	public static void test3() throws ClientProtocolException, IOException{
		hbaseUtil.insertData("yx_member", "9998015305885835", "uinfo", "name", "郭俊2");
	}
	
	public static void test4() throws ClientProtocolException, IOException{
		hbaseUtil.QueryByRowKey("yx_member","9998015305885835");
	}
	
	public static void main(String[] args) throws Exception {
//		test1();
//		test2();
//		test3();
		test4();
	}

}
