package cn.zj.easynet.util;

import java.lang.Character.UnicodeBlock;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import cn.zj.easynet.remote.dto.ZxRequestEntryDto;
import cn.zj.easynet.remote.request.ZxAccountReq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/***
 * 摘抄自platform-core-3.0.1.jar
 * @author Administrator
 *
 */
public class Utils
{
  private static final String ASSERT_NULL_FORMAT = "参数[%s]不能为空!";
  
  public static void append(StringBuffer buffer, CharSequence cs)
  {
    append(buffer, cs, false, false);
  }
  
  public static void append(StringBuffer buffer, CharSequence cs, boolean headWhiteSpace, boolean tailWhiteSpace)
  {
    if (null != buffer)
    {
      if (headWhiteSpace) {
        buffer.append(' ');
      }
      buffer.append(cs);
      if (tailWhiteSpace) {
        buffer.append(' ');
      }
    }
  }
  
  public static void append(StringBuffer buffer, char cs)
  {
    append(buffer, cs, false, false);
  }
  
  public static void append(StringBuffer buffer, char cs, boolean headWhiteSpace, boolean tailWhiteSpace)
  {
    if (null != buffer)
    {
      if (headWhiteSpace) {
        buffer.append(' ');
      }
      buffer.append(cs);
      if (tailWhiteSpace) {
        buffer.append(' ');
      }
    }
  }
  
  public static synchronized String printable(byte[] b)
  {
    char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
    
    char[] out = new char[b.length];
    for (int i = 0; i < b.length; i++)
    {
      int index = b[i] % alphabet.length;
      if (index < 0) {
        index += alphabet.length;
      }
      out[i] = alphabet[index];
    }
    return new String(out);
  }
  
  public static synchronized String printableNumber(byte[] b)
  {
    char[] alphabet = "1234567890".toCharArray();
    char[] out = new char[b.length];
    for (int i = 0; i < b.length; i++)
    {
      int index = b[i] % alphabet.length;
      if (index < 0) {
        index += alphabet.length;
      }
      out[i] = alphabet[index];
    }
    return new String(out);
  }
  
  /**
   * 中兴生成唯一编码
   * 
   * @param timeStr
   * @return
   */
  public static String genZxCmdId(String timeStr) {
      // 如果为空，则随机生成
      return timeStr + Utils.getNumericRandomID(8);
  }
  
  public static String getRandomID(int length)
  {
    byte[] b = new byte[length];
    SecureRandom sr = new SecureRandom();
    sr.nextBytes(b);
    return printable(b);
  }
  
  public static String getNumericRandomID(int length)
  {
    byte[] b = new byte[length];
    SecureRandom sr = new SecureRandom();
    sr.nextBytes(b);
    return printableNumber(b);
  }
  
  public static String parse2ipVal(String ipStr, int size)
  {
    StringBuffer sbf = new StringBuffer();
    String[] ipArr = ipStr.replace(".", String.valueOf(':')).split(String.valueOf(':'));
    for (String str : ipArr)
    {
      int length = size - str.length();
      for (int i = 0; i < length; i++) {
        sbf.append('0');
      }
      sbf.append(str);
    }
    return sbf.toString();
  }
  
  public static Object[] getArguments(Object[] varArgs)
  {
    if ((varArgs.length == 1) && ((varArgs[0] instanceof Object[]))) {
      return (Object[])varArgs[0];
    }
    return varArgs;
  }
  
  public static UUID getUUIDFromString(String uuid)
  {
    UUID uuidObj = null;
    if (!StringUtils.isBlank(uuid)) {
      try
      {
        uuidObj = UUID.fromString(uuid);
      }
      catch (Exception e) {}
    }
    return uuidObj;
  }
  
  public static String assembleJsonStr4MMl(String phoneNbr, String password) {
		ZxAccountReq req = new ZxAccountReq();
		String timeStr = DateUtil.getGeneralFormat().format(new Date());
		String serialNo = Utils.genZxCmdId(timeStr);
		
      req.setCmd(IConstants.ZX_CMD_ACCOUNT_CREATE);
      req.setId(serialNo);//生成唯一编码
      
      //entity
      ZxRequestEntryDto entry = new ZxRequestEntryDto();
      entry.setLogin(phoneNbr);
      entry.setDomain("ecplive.com");
      entry.setBusinessPhone(phoneNbr);
      entry.setCompanyId("32000105260003");//--------------
      entry.setRealname(phoneNbr);
      entry.setPassword(password);
      entry.setContactUrl("XXX");//------------
      entry.setChannelId("4");//-------------
      entry.setVersion("4");//--------------
      entry.setNumberType("1");//号码类型: 0=虚号码,1=实号码  ----------
      entry.setItFlag("1");//it标识位: 0=浙江,1=全国            -----------
      entry.setBillName("翼聊");//用户开票名称：空       --------------
      entry.setSerialNo(serialNo);//-----------------------
      entry.setBusinessType("2");
//      entry.setMessageType("0");//发送默认短信，可选参数

      req.setEntry(entry);
      String jsonStr = JSON.toJSONString(req);
      return jsonStr;
	}
  
  public static String assembleJsonStr4MMl(String phoneNbr, String password, String companyId, String version) {
		ZxAccountReq req = new ZxAccountReq();
		String timeStr = DateUtil.getGeneralFormat().format(new Date());
		String serialNo = Utils.genZxCmdId(timeStr);
		
    req.setCmd(IConstants.ZX_CMD_ACCOUNT_CREATE);
    req.setId(serialNo);//生成唯一编码
    
    //entity
    ZxRequestEntryDto entry = new ZxRequestEntryDto();
    entry.setLogin(phoneNbr);
    entry.setDomain("ecplive.com");
    entry.setBusinessPhone(phoneNbr);
    entry.setCompanyId(companyId);//--------------
    entry.setRealname(phoneNbr);
    entry.setPassword(password);
    entry.setContactUrl("XXX");//------------
    entry.setChannelId("4");//-------------
    entry.setVersion(version);//--------------
    entry.setNumberType("1");//号码类型: 0=虚号码,1=实号码  ----------
    entry.setItFlag("1");//it标识位: 0=浙江,1=全国            -----------
    entry.setBillName("翼聊");//用户开票名称：空       --------------
    entry.setSerialNo(serialNo);//-----------------------
    entry.setBusinessType("2");
//    entry.setMessageType("0");//发送默认短信，可选参数

    req.setEntry(entry);
    String jsonStr = JSON.toJSONString(req);
    return jsonStr;
	}
  
  	public static String QueryId(Long tid) {
		String date= Util.eatSign( Util.eatSpace(Util.getCurrentDate() ),":");
		return date+QuerySrtingTid(tid);
	}
		
	public static String QuerySrtingTid(Long tid){
		return "00000000".substring(0, 8-String.valueOf(tid).length())+String.valueOf(tid);
	}
	
	public static JSONObject getCmpIdAndVersion4AreaCode(String areaCode){
		JSONObject json = new JSONObject();
		
		if(StringUtils.isBlank(areaCode)){
			json.put("cmpyId", "00902000010003");
			json.put("version", "非浙江天翼手机个人");
		}else{
			json.put("version", "浙江天翼手机个人");
			if("0570".endsWith(areaCode)){
				json.put("cmpyId", "32000159280003");
			}else if("0571".endsWith(areaCode)){
				json.put("cmpyId", "32000105260003");
			}else if("0572".endsWith(areaCode)){
				json.put("cmpyId", "32000167650003");
			}else if("0573".endsWith(areaCode)){
				json.put("cmpyId", "32000176890003");
			}else if("0574".endsWith(areaCode)){
				json.put("cmpyId", "32000118480003");
			}else if("0575".endsWith(areaCode)){
				json.put("cmpyId", "32000149840003");
			}else if("0576".endsWith(areaCode)){
				json.put("cmpyId", "32000153150003");
			}else if("0577".endsWith(areaCode)){
				json.put("cmpyId", "32000197160003");
			}else if("0578".endsWith(areaCode)){
				json.put("cmpyId", "32000165100003");
			}else if("0579".endsWith(areaCode)){
				json.put("cmpyId", "32000165760003");
			}else if("0580".endsWith(areaCode)){
				json.put("cmpyId", "32000194330003");
			}else{
				if(areaCode.length() < 4){
					areaCode = "0" + areaCode;
				}
				json.put("cmpyId", "009020" + areaCode + "0003");
				json.put("version", "非浙江天翼手机个人");
			}
			
		}
		
		return json;
	}
	
	/**
	  * utf-8 转换成 unicode
	  * @author fanhui
	  * 2007-3-15
	  * @param inStr
	  * @return
	  */
	 public static String utf8ToUnicode(String inStr) {
	        char[] myBuffer = inStr.toCharArray();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < inStr.length(); i++) {
	         UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
	            if(ub == UnicodeBlock.BASIC_LATIN){
	             //英文及数字等
	             sb.append(myBuffer[i]);
	            }else if(ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
	             //全角半角字符
	             int j = (int) myBuffer[i] - 65248;
	             sb.append((char)j);
	            }else{
	             //汉字
	             short s = (short) myBuffer[i];
	                String hexS = Integer.toHexString(s);
	                String unicode = "\\u"+hexS;
	             sb.append(unicode.toLowerCase());
	            }
	        }
	        return sb.toString();
	    }

	 /**
	  * unicode 转换成 utf-8
	  * @author fanhui
	  * 2007-3-15
	  * @param theString
	  * @return
	  */
	 public static String unicodeToUtf8(String theString) {
	  char aChar;
	  int len = theString.length();
	  StringBuffer outBuffer = new StringBuffer(len);
	  for (int x = 0; x < len;) {
	   aChar = theString.charAt(x++);
	   if (aChar == '\\') {
	    aChar = theString.charAt(x++);
	    if (aChar == 'u') {
	     // Read the xxxx
	     int value = 0;
	     for (int i = 0; i < 4; i++) {
	      aChar = theString.charAt(x++);
	      switch (aChar) {
	      case '0':
	      case '1':
	      case '2':
	      case '3':
	      case '4':
	      case '5':
	      case '6':
	      case '7':
	      case '8':
	      case '9':
	       value = (value << 4) + aChar - '0';
	       break;
	      case 'a':
	      case 'b':
	      case 'c':
	      case 'd':
	      case 'e':
	      case 'f':
	       value = (value << 4) + 10 + aChar - 'a';
	       break;
	      case 'A':
	      case 'B':
	      case 'C':
	      case 'D':
	      case 'E':
	      case 'F':
	       value = (value << 4) + 10 + aChar - 'A';
	       break;
	      default:
	       throw new IllegalArgumentException(
	         "Malformed   \\uxxxx   encoding.");
	      }
	     }
	     outBuffer.append((char) value);
	    } else {
	     if (aChar == 't')
	      aChar = '\t';
	     else if (aChar == 'r')
	      aChar = '\r';
	     else if (aChar == 'n')
	      aChar = '\n';
	     else if (aChar == 'f')
	      aChar = '\f';
	     outBuffer.append(aChar);
	    }
	   } else
	    outBuffer.append(aChar);
	  }
	  return outBuffer.toString();
	 }
  
}

