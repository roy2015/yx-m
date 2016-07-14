package cn.zj.easynet.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
* @Description: 批量处理insert语句
* @author chentianqing
* @date 2015年12月25日 上午11:41:18
 */
public class SqlInsertHandle {
	
	public static int batCount = 1000;
	
	public static void main(String[] args) {
		try{
			String outPath = "C:\\Users\\Administrator\\Desktop\\guoj\\";
			String filePath = "C:\\Users\\Administrator\\Desktop\\dump\\yx_addr_log24.sql";
			String encodeing = "UTF-8";
			File file = new File(filePath);
			
//			int lastLine = getLineNum(file);
			
			String fileName = file.getName();
			int index = fileName.lastIndexOf(".");
			String prefixName = fileName.substring(0,index);
			String suffix = fileName.substring(index+1,fileName.length());
			String outFile = outPath + prefixName+"_result."+suffix;
			
			
			boolean hasError = false;
			//判断文件是否存在
			if(file.isFile() && file.exists()){ 
				//考虑到编码格式
	            InputStreamReader read = new InputStreamReader(new FileInputStream(file),encodeing);
	            BufferedReader bufferedReader = new BufferedReader(read);
	            
	            File of = new File(outFile);
	            if (!of.getParentFile().exists()) {
	            	of.getParentFile().mkdirs();
	            }
	            if(of.exists()){
	            	of.delete();
	            }
	            of.createNewFile();
	            
	            
	            FileWriter fw = new FileWriter(of);
	            BufferedWriter bufferedWriter = new BufferedWriter(fw); 
	            
	            
	            String lineTxt = null;
	            int i=0;
	            
	            StringBuffer sb = new StringBuffer();
	            
	            int firstLeftIndex = 0;
	            int firstRightIndex = 0;
	            int secondLeftIndex = 0;
	            //insert into (a,b,c) values
	            String insertHead = "";
	            
	            System.out.println("开始处理...");
	            
	            while((lineTxt = bufferedReader.readLine()) != null){
	            	i++;
	            	if((lineTxt.startsWith("insert into") ||lineTxt.startsWith("INSERT INTO")) && lineTxt.endsWith(");")){
	            		if(firstLeftIndex == 0){
	            			firstLeftIndex = lineTxt.indexOf("(")+1;
	            			firstRightIndex = lineTxt.indexOf(")")+1;
	            		}
	            		
	            		if(secondLeftIndex == 0){
	            			String str = lineTxt.substring(firstRightIndex, lineTxt.length());
	            			secondLeftIndex = firstRightIndex + str.indexOf("(")+1;
	            		}
	            		
	            		if(insertHead.equals("")){
	            			insertHead = lineTxt.substring(0, secondLeftIndex -1);
	            		}
	            		//(1,2,3)
	            		String insertBody = lineTxt.substring(secondLeftIndex -1,lineTxt.length()-1);
	            		
	            		if(i == 1){
	            			sb.append(insertHead);
	            		}
	            		
	            		if(i%batCount == 0){
	            			System.out.println("处理行数："+i);
	            			bufferedWriter.write(sb.toString());
	            			sb.setLength(0);
	            			sb.append(insertHead);
	            		}
	            		
	            		sb.append(insertBody);
	            		
	            		
	            		if((i + 1)%batCount==0){
	            			sb.append(";\r\n");
	            		}else{
	            			sb.append(",");
	            		}
	            	}else{
	            		hasError = true;
	            		System.out.println("第"+i+"行：格式不符");
	            	}
	            	
	            }
	            
	            read.close();
	            
	            if(sb.toString()!=null && !sb.toString().equals("")){
	            	String result = sb.substring(0, sb.length()-1)+";";
	            	bufferedWriter.write(result);
	            }
	            
//	            if((i+1)%batCount!=0){
//	            	result = sb.substring(0, sb.length()-1)+";";
//	            }else{
//	            	result = sb.toString();
//	            }
	            
//	            bufferedWriter.write(result);
	            
	            bufferedWriter.flush();
	            bufferedWriter.close();
	            fw.close();
	            
	            if(hasError){
	            	if(of.exists()){
		            	of.delete();
		            	System.out.println("存在错误，删除目标文件");
		            }
	            }
	            
	            System.out.println("处理结束.行数:"+i);
			}else{
				System.out.println("找不到指定的文件");
			}
		}catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }finally{
        	
        }
		
	}
	
	public static int getLineNum(File file){
		long fileLength = file.length(); 
		LineNumberReader rf = null; 
		int lines = 0;
		try { 
			rf = new LineNumberReader(new FileReader(file)); 
			if (rf != null) { 
				rf.skip(fileLength); 
				lines = rf.getLineNumber(); 
				rf.close(); 
			} 
		} catch (IOException e) { 
			if (rf != null) { 
				try { 
					rf.close(); 
				} catch (IOException ee) { 
				} 
			} 
		}
		return lines;
	}

}
