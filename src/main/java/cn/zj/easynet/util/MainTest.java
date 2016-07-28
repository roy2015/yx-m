package cn.zj.easynet.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.json.JSONException;
import org.test.Animal;

import redis.clients.jedis.Jedis;
import cn.zj.easynet.device.http.HttpClientPool;
import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.CreateUserBusiness;
import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.remote.dto.ZxRequestEntryDto;
import cn.zj.easynet.remote.request.ZxAccountReq;
import cn.zj.easynet.util.security.RSAUtil;

import com.alibaba.fastjson.JSON;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class MainTest {

    private static final Logger logger           = Logger.getLogger(MainTest.class);
    public static final String  LINE_SEPARATOR   = System.getProperty("line.separator");

    public static final String  CHARSET_ISO88591 = "ISO-8859-1";

    static class MobileFragmentDto {

        private String  hcode;
        private String  areaCode;
        private String  areaName;
        private String  depCode;
        private String  depName;
        private String  govCode;
        private String  lanId;
        private Integer sp;

        public String getHcode() {
            return hcode;
        }

        public void setHcode(String hcode) {
            this.hcode = hcode;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getDepCode() {
            return depCode;
        }

        public void setDepCode(String depCode) {
            this.depCode = depCode;
        }

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getGovCode() {
            return govCode;
        }

        public void setGovCode(String govCode) {
            this.govCode = govCode;
        }

        public String getLanId() {
            return lanId;
        }

        public void setLanId(String lanId) {
            this.lanId = lanId;
        }

        public Integer getSp() {
            return sp;
        }

        public void setSp(Integer sp) {
            this.sp = sp;
        }
    }

    public static BufferedReader getBufferReaderFromFile(File file, String charset) throws FileNotFoundException {
        InputStream ss = new FileInputStream(file);
        InputStreamReader ireader;
        BufferedReader reader = null;
        try {
            if (charset == null) {
                ireader = new InputStreamReader(ss, CHARSET_ISO88591);
            } else {
                ireader = new InputStreamReader(ss, charset);
            }
            reader = new BufferedReader(ireader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public static void dealFileLineByLine(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String readedLine = null;
        String param = "";
        String ret = "";
        int i = 0;

        try {
            while ((readedLine = reader.readLine()) != null) {
                String[] strArry = readedLine.split("\\s+");
                i++;
                param = "{\"mobile\":\"" + strArry[1] + "\"," + "\"account\":\"\"," + "\"name\":\"\","
                        + "\"userId\":\"" + strArry[0] + "\"}";

                param = EncryptUtil.encryptFull(param);
                ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/q4", param);
                ret = EncryptUtil.decryptFull(ret);
                logger.error(ret);

                if (i % 100 == 0) {
                    TimeUnit.SECONDS.sleep(3);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void dealFileLineByLine1(BufferedReader reader, File outputFile) {
        StringBuilder sb = new StringBuilder();
        String readedLine = null;
        String ret = "";
        int i = 0;
        FileOutputStream outStream = null;
        String[] strArry;

        try {
            MobileFragmentDto fragmentDto = null;
            outStream = FileUtils.openOutputStream(outputFile);
            while ((readedLine = reader.readLine()) != null) {
                strArry = readedLine.split("\\s+");
                i++;

                ret = HttpClientPool.getInstance().getMethod("http://192.168.21.216/CI/hcode2/" + strArry[0], 10000);
                logger.error(ret);

                fragmentDto = JSON.parseObject(ret, MobileFragmentDto.class);

                if (fragmentDto != null) {
                    IOUtils.write(strArry[0], outStream, "utf-8");
                    IOUtils.write("\t", outStream, "utf-8");
                    IOUtils.write(fragmentDto.getAreaCode(), outStream, "utf-8");
                    IOUtils.write("\t", outStream, "utf-8");
                    IOUtils.write(fragmentDto.getAreaName(), outStream, "utf-8");
                    IOUtils.write("\t", outStream, "utf-8");
                    IOUtils.write(IOUtils.LINE_SEPARATOR, outStream, "utf-8");
                } else {
                    IOUtils.write(strArry[0], outStream, "utf-8");
                    IOUtils.write(IOUtils.LINE_SEPARATOR, outStream, "utf-8");
                }

                /*
                 * if(i % 100 == 0){ TimeUnit.SECONDS.sleep(1); }
                 */

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                IOUtils.closeQuietly(outStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void dealFile(File file, String charset) {
        BufferedReader reader = null;
        if (!file.exists()) {
            System.out.println("getFullContent: file(" + file.getAbsolutePath() + ") does not exist.");
            return;
        }
        if (charset == null) {
            charset = CHARSET_ISO88591;
        }
        try {
            reader = getBufferReaderFromFile(file, charset);
            dealFileLineByLine(reader);
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
    }

    public static String assembleJsonStr4MMl(String phoneNbr, String password) {
        ZxAccountReq req = new ZxAccountReq();
        String timeStr = DateUtil.getGeneralFormat().format(new Date());
        String serialNo = Utils.genZxCmdId(timeStr);

        req.setCmd(IConstants.ZX_CMD_ACCOUNT_CREATE);
        req.setId(serialNo);// 生成唯一编码

        // entity
        ZxRequestEntryDto entry = new ZxRequestEntryDto();
        entry.setLogin(phoneNbr);
        entry.setDomain("ecplive.com");
        entry.setBusinessPhone(phoneNbr);
        entry.setCompanyId("32000105260003");// --------------
        entry.setRealname(phoneNbr);
        entry.setPassword(password);
        entry.setContactUrl("XXX");// ------------
        entry.setChannelId("4");// -------------
        // entry.setVersion("4");//--------------
        entry.setNumberType("1");// 号码类型: 0=虚号码,1=实号码 ----------
        entry.setItFlag("1");// it标识位: 0=浙江,1=全国 -----------
        entry.setBillName("浙江省电信");// 用户开票名称：空 --------------
        entry.setSerialNo(serialNo);// -----------------------
        entry.setBusinessType("2");

        /*
         * entry.setCardNumber(phoneNbr); // entry.setUrl(""); entry.setRealname("郭俊");
         * entry.setUserType(IConstants.ZX_CMD_ENTRY_USETYPE);// 用户类型 1：学生；2：网吧；3：企业；4：家庭普通；5：WEB呼叫中心；0：其它---4
         * entry.setEcpcompanyId(IConstants.ZX_CMD_ENTRY_ECPCOMPANYID);// ECP用户ID，由营业厅系统维护，ECP业务平台需要保存为企业的一个属性。
         * entry.setUsername(phoneNbr);// 用户名，作昵称---业务号码 entry.setLogin(phoneNbr);// 登录名-----业务号码
         * entry.setPassword(password);// 登录密码----随机生成 8位字母与数字组合
         * entry.setVersionType(IConstants.ZX_CMD_ENTRY_VERSIONTYPE);// 　版本类型0浙江版1网络版，2自助版、3定制版，4 EMA版 --1
         * entry.setLanguage(IConstants.ZX_CMD_ENTRY_LANGUAGE);// 语言1: 中文 2: 英文----1
         * entry.setState(IConstants.ZX_CMD_ENTRY_STATE);// SoftDA用户状态1：正常使用 2：禁止使用4：用户暂停 5：停机报号6：未使用7：待验证 ---1
         * entry.setBindAuthorization(IConstants.ZX_CMD_ENTRY_BIND_AUTHORIZATION);// 绑定号码权限1: 网内, 2:市话权限3: 国内长途权限, 4: //
         * 国际长途权限 , 默认权限: 3 ---3 entry.setCallAuthorization(IConstants.ZX_CMD_ENTRY_CALL_AUTHORIZATION);// 呼叫限制权限 1: 网内,
         * 2: 市话权限, 3: 国内长途权限, 4: // 国际长途权限 , 默认权限: 3---3
         * entry.setSoftphoneType(IConstants.ZX_CMD_ENTRY_SOFTPHONETYPE);// 终端类型0: 无权使用软终端电话, 1: 有权使用软号码池 //
         * 2：有权使用固定软终端号码，默认：1---1 entry.setChargecardType(IConstants.ZX_CMD_ENTRY_CHARGECARDTYPE);// 记帐卡类型0: 后付费 1:
         * 预付费---0 entry.setCardlocalNum("0571"); // entry.setCardlocalNum("0571"); //
         * logger.debug(entry.getCardlocalNum()); entry.setFeeControl(IConstants.ZX_CMD_ENTRY_FEECONTROL);
         * entry.setBusinessType(IConstants.ZX_CMD_ENTRY_BUSINESSTYPE);
         */

        req.setEntry(entry);
        String jsonStr = JSON.toJSONString(req);
        return jsonStr;
    }

    public static void testDaemon() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("sub-thread-1 is sleeping");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("sub-thread-1 is awake");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("sub-thread-2 is sleeping");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("sub-thread-2 is awake");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        // t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        System.out.println("main thread is stoped");
    }

    public static void testNegative1() throws Exception {
        Map<String, String> projectMap = new HashMap<String, String>();
        List<String> todoList = new ArrayList<String>();// 需要切换的项目
        Map<String, String> project2DirMap = new HashMap<String, String>();// 项目到项目实际目录
        boolean toOnlineEnv = false;// 是否切到线上环境

        String basePath = "D://workspace_elf_yx_trunck//";// D://workspace_elf_yx_trunck// D://test//
        todoList.add("yx-share");
        todoList.add("yx-addr");
        todoList.add("yx-it");
        todoList.add("yx-phone");
        todoList.add("yx-plat");

        project2DirMap.put("yx-addr", "yx-addrlist");
        project2DirMap.put("yx-it", "yx-it");
        project2DirMap.put("yx-phone", "yx-phonepool");
        project2DirMap.put("yx-plat", "yx-platform");
        project2DirMap.put("yx-share", "yx-share");

        projectMap.put("yx-addr", basePath + "//" + project2DirMap.get("yx-addr") + "//src//main//");
        projectMap.put("yx-it", basePath + "//" + project2DirMap.get("yx-it") + "//src//main//");
        projectMap.put("yx-phone", basePath + "//" + project2DirMap.get("yx-phone") + "//src//main//");
        projectMap.put("yx-plat", basePath + "//" + project2DirMap.get("yx-plat") + "//src//main//");
        projectMap.put("yx-share", basePath + "//" + project2DirMap.get("yx-share") + "//");

        FileInputStream inputStream;
        FileOutputStream outputStream;
        String str;
        for (String proj : todoList) {
            if ("yx-share".equals(proj)) {
                // yx-share pom
                inputStream = new FileInputStream(new File(projectMap.get(proj) + "pom.xml"));
                str = new String(IOUtils.toByteArray(inputStream));
                IOUtils.closeQuietly(inputStream);

                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                StringBuffer prodsb = new StringBuffer();
                prodsb.append("<dependency>").append("\r\n");
                prodsb.append("			<groupId>im.yixin.platform</groupId>").append("\r\n");
                prodsb.append("			<artifactId>push-service</artifactId>").append("\r\n");
                prodsb.append("			<version>1.0.7-SNAPSHOT</version>").append("\r\n");
                prodsb.append("			<scope>${scope.compile}</scope>").append("\r\n");
                prodsb.append("		</dependency>");

                StringBuffer testsb = new StringBuffer();
                testsb.append("<dependency>").append("\r\n");
                testsb.append("			<groupId>im.yixin.platform</groupId>").append("\r\n");
                testsb.append("			<artifactId>push-service-test</artifactId>").append("\r\n");
                testsb.append("			<version>1.0.7</version>").append("\r\n");
                testsb.append("		</dependency>");

                if (toOnlineEnv) {
                    str = str.replace(sb1.append("<!-- ").append(prodsb.toString()).append(" -->").toString(),
                                      prodsb.toString());
                    str = str.replace(testsb.toString(),
                                      sb2.append("<!-- ").append(testsb.toString()).append(" -->").toString());
                } else {
                    str = str.replace(sb1.append("<!-- ").append(testsb.toString()).append(" -->").toString(),
                                      testsb.toString());
                    str = str.replace(prodsb.toString(),
                                      sb2.append("<!-- ").append(prodsb.toString()).append(" -->").toString());
                }

                outputStream = new FileOutputStream(new File(projectMap.get(proj) + "pom.xml"));
                IOUtils.write(str, outputStream);
                IOUtils.closeQuietly(outputStream);
            } else {
                // applicationContext.xml
                inputStream = new FileInputStream(new File(projectMap.get(proj)
                                                           + "resources//spring//applicationContext.xml"));
                str = new String(IOUtils.toByteArray(inputStream));
                IOUtils.closeQuietly(inputStream);
                if (toOnlineEnv) {
                    str = str.replaceAll("classpath:system-dev.properties", "classpath:system.properties");
                    if ("yx-it".equals(proj)) {
                        str = str.replaceAll("<!-- <import resource=\"remote-http.xml\" /> -->",
                                             "<import resource=\"remote-http.xml\" />");
                        str = str.replaceAll("<import resource=\"mock-remote-http.xml\" />",
                                             "<!-- <import resource=\"mock-remote-http.xml\" /> -->");
                    } else {
                    }
                } else {
                    str = str.replaceAll("classpath:system.properties", "classpath:system-dev.properties");
                    if ("yx-it".equals(proj)) {
                        str = str.replaceAll("<!-- <import resource=\"mock-remote-http.xml\" /> -->",
                                             "<import resource=\"mock-remote-http.xml\" />");
                        str = str.replaceAll("<import resource=\"remote-http.xml\" />",
                                             "<!-- <import resource=\"remote-http.xml\" /> -->");
                    } else {
                    }
                }
                outputStream = new FileOutputStream(new File(projectMap.get(proj)
                                                             + "resources//spring//applicationContext.xml"));
                IOUtils.write(str, outputStream);
                IOUtils.closeQuietly(outputStream);

                // system.properties
                inputStream = new FileInputStream(new File(projectMap.get(proj) + "config//system.properties"));
                str = new String(IOUtils.toByteArray(inputStream));
                IOUtils.closeQuietly(inputStream);
                if (toOnlineEnv) {
                    str = str.replaceAll("debug = true", "debug = false");
                } else {
                    str = str.replaceAll("debug = false", "debug = true");
                }
                outputStream = new FileOutputStream(new File(projectMap.get(proj) + "config//system.properties"));
                IOUtils.write(str, outputStream);
                IOUtils.closeQuietly(outputStream);

                // remote.config
                if (toOnlineEnv) {
                    inputStream = new FileInputStream(new File(projectMap.get(proj) + "config//prod_remote.config"));
                } else {
                    inputStream = new FileInputStream(new File(projectMap.get(proj) + "config//test_remote.config"));
                }
                str = new String(IOUtils.toByteArray(inputStream));
                IOUtils.closeQuietly(inputStream);
                outputStream = new FileOutputStream(new File(projectMap.get(proj) + "config//remote.config"));
                IOUtils.write(str, outputStream);
                IOUtils.closeQuietly(outputStream);

                // web.xml
                if (!"yx-it".equals(proj)) {
                    inputStream = new FileInputStream(new File(projectMap.get(proj) + "webapp//WEB-INF//web.xml"));
                    str = new String(IOUtils.toByteArray(inputStream));
                    IOUtils.closeQuietly(inputStream);

                    /*
                     * if(toOnlineEnv){ str = str.replace("<!-- <param-value>106.2.124.87</param-value> -->",
                     * "<param-value>106.2.124.87</param-value>"); str =
                     * str.replace("<param-value>106.2.44.240</param-value>",
                     * "<!-- <param-value>106.2.44.240</param-value> -->"); }else{ str =
                     * str.replace("<!-- <param-value>106.2.44.240</param-value> -->",
                     * "<param-value>106.2.44.240</param-value>"); str =
                     * str.replace("<param-value>106.2.124.87</param-value>",
                     * "<!-- <param-value>106.2.124.87</param-value> -->"); }
                     */

                    if ("yx-addr".equals(proj)) {
                        if (toOnlineEnv) {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://adr.b.yixin.im</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://adr.b.yixin.im</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-addr</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-addr</param-value> -->");
                            str = str.replace("<!-- <param-value>adr.b.yixin.im</param-value> -->",
                                              "<param-value>adr.b.yixin.im</param-value>");
                            str = str.replace("<param-value>106.2.44.240</param-value>",
                                              "<!-- <param-value>106.2.44.240</param-value> -->");
                        } else {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-addr</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-addr</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://adr.b.yixin.im</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://adr.b.yixin.im</param-value> -->");
                            str = str.replace("<!-- <param-value>106.2.44.240</param-value> -->",
                                              "<param-value>106.2.44.240</param-value>");
                            str = str.replace("<param-value>adr.b.yixin.im</param-value>",
                                              "<!-- <param-value>adr.b.yixin.im</param-value> -->");
                        }
                    } else if ("yx-phone".equals(proj)) {
                        if (toOnlineEnv) {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://pp.b.yixin.im</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://pp.b.yixin.im</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-phone</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-phone</param-value> -->");
                            str = str.replace("<!-- <param-value>pp.b.yixin.im</param-value> -->",
                                              "<param-value>pp.b.yixin.im</param-value>");
                            str = str.replace("<param-value>106.2.44.240</param-value>",
                                              "<!-- <param-value>106.2.44.240</param-value> -->");
                        } else {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-phone</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-phone</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://pp.b.yixin.im</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://pp.b.yixin.im</param-value> -->");
                            str = str.replace("<!-- <param-value>106.2.44.240</param-value> -->",
                                              "<param-value>106.2.44.240</param-value>");
                            str = str.replace("<param-value>pp.b.yixin.im</param-value>",
                                              "<!-- <param-value>pp.b.yixin.im</param-value> -->");
                        }
                    } else if ("yx-plat".equals(proj)) {
                        if (toOnlineEnv) {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://pl.b.yixin.im</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://pl.b.yixin.im</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-plat</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-plat</param-value> -->");
                            str = str.replace("<!-- <param-value>pl.b.yixin.im</param-value> -->",
                                              "<param-value>pl.b.yixin.im</param-value>");
                            str = str.replace("<param-value>106.2.44.240</param-value>",
                                              "<!-- <param-value>106.2.44.240</param-value> -->");
                        } else {
                            str = str.replace("<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-plat</param-value> -->",
                                              "<param-value>https://i.ecplive.cn/sso/logout?service=http://106.2.44.240/yx-plat</param-value>");
                            str = str.replace("<param-value>https://i.ecplive.cn/sso/logout?service=http://pl.b.yixin.im</param-value>",
                                              "<!-- <param-value>https://i.ecplive.cn/sso/logout?service=http://pl.b.yixin.im</param-value> -->");
                            str = str.replace("<!-- <param-value>106.2.44.240</param-value> -->",
                                              "<param-value>106.2.44.240</param-value>");
                            str = str.replace("<param-value>pl.b.yixin.im</param-value>",
                                              "<!-- <param-value>pl.b.yixin.im</param-value> -->");
                        }

                    } else {
                    }

                    outputStream = new FileOutputStream(new File(projectMap.get(proj) + "webapp//WEB-INF//web.xml"));
                    IOUtils.write(str, outputStream);
                    IOUtils.closeQuietly(outputStream);
                } else {
                }
            }
        }

        // resources\spring
        /*
         * FileInputStream inputStream = new FileInputStream(new File("D://test//applicationContext.xml")); String str =
         * new String(IOUtils.toByteArray(inputStream)); IOUtils.closeQuietly(inputStream); str =
         * str.replaceAll("classpath:system.properties", "classpath:system-dev.properties"); str =
         * str.replaceAll("<!-- <import resource=\"remote-http.xml\" /> -->",
         * "<import resource=\"remote-http.xml\" />"); str =
         * str.replaceAll("<import resource=\"mock-remote-http.xml\" />",
         * "<!-- <import resource=\"mock-remote-http.xml\" /> -->"); FileOutputStream outputStream = new
         * FileOutputStream(new File("D://test//applicationContext.xml")); IOUtils.write(str, outputStream);
         * IOUtils.closeQuietly(outputStream);
         */

        // config
        /*
         * FileInputStream inputStream = new FileInputStream(new File("D://test//system.properties")); String str = new
         * String(IOUtils.toByteArray(inputStream)); IOUtils.closeQuietly(inputStream); str =
         * str.replaceAll("debug = false", "debug = true"); FileOutputStream outputStream = new FileOutputStream(new
         * File("D://test//system.properties")); IOUtils.write(str, outputStream); IOUtils.closeQuietly(outputStream);
         */

        // config
        /*
         * FileInputStream inputStream = new FileInputStream(new File("D://test//test_remote.config")); String str = new
         * String(IOUtils.toByteArray(inputStream)); IOUtils.closeQuietly(inputStream); FileOutputStream outputStream =
         * new FileOutputStream(new File("D://test//remote.config")); IOUtils.write(str, outputStream);
         * IOUtils.closeQuietly(outputStream);
         */

        /*
         * FileInputStream inputStream = new FileInputStream(new File("D://test//pom.xml")); String str = new
         * String(IOUtils.toByteArray(inputStream)); IOUtils.closeQuietly(inputStream); StringBuffer sb1 = new
         * StringBuffer(); StringBuffer sb2 = new StringBuffer(); StringBuffer prodsb = new StringBuffer();
         * prodsb.append("<dependency>").append("\r\n");
         * prodsb.append("			<groupId>im.yixin.platform</groupId>").append("\r\n");
         * prodsb.append("			<artifactId>push-service</artifactId>").append("\r\n");
         * prodsb.append("			<version>1.0.7-SNAPSHOT</version>").append("\r\n");
         * prodsb.append("			<scope>${scope.compile}</scope>").append("\r\n"); prodsb.append("		</dependency>");
         * StringBuffer testsb = new StringBuffer(); testsb.append("<dependency>").append("\r\n");
         * testsb.append("			<groupId>im.yixin.platform</groupId>").append("\r\n");
         * testsb.append("			<artifactId>push-service-test</artifactId>").append("\r\n");
         * testsb.append("			<version>1.0.7</version>").append("\r\n"); testsb.append("		</dependency>"); str =
         * str.replace(sb1.append("<!-- ").append(prodsb.toString()).append(" -->").toString(), prodsb.toString()); str
         * = str.replace(testsb.toString(),sb2.append("<!-- ").append(testsb.toString()).append(" -->").toString());
         * FileOutputStream outputStream = new FileOutputStream(new File("D://test//pom.xml")); IOUtils.write(str,
         * outputStream); IOUtils.closeQuietly(outputStream);
         */

    }

    public static void test0() throws InterruptedException {
        System.out.println(EncryptUtil.decryptFull("3568511d544c56034f1b31510b0a05010a2c5b08216e68107554564e505d33050a095d3a060a114855448df0caa1e1cfaaf098d1b9fa68475b36021e370d0d3213486f1a0401060169494e0c0c1f0b27565521680e1bd6d498d283cc8ff3bb146448291b5353150c17050145044c56535b7b5076727b1b1a5f457d5445160c031d6a392727230e0924023a28223c1502140b07644a4a0918593c232c2d5178055e56175e44a2f1c1bcd2fc85f0f14d5c520c0717163b00447d744240515a595b451b52091f52775a2a2f1148010544151654215e3910190d08081656661f07710633293f11197018011e471115331d070a445e7a667b795f7d780b0d0a507b5d04"));
        System.out.println(EncryptUtil.encryptFull("{\"userId\": 3107001,\"code\": \"057181931111\",\"cityCode\":\"0571\"}"));
        System.out.println(EncryptUtil.decryptFull("35685e0157761b5d511b680c5a5647460b1c533f303e3b5c2254404e7c611a2e36337426352826484344172e203f2a191821555d1a537b5f4b635f5b7f7f536743596045"));

        System.out.println(EncryptUtil.decryptFull("3568511d544c56034f1b31510b0a05010a2c5b08216e68107554564e505d33050a095d3a060a1148"
                                                   + "55448df0caa1e1cfaaf098d1b9fa68475b36021e370d0d3213486f1a0401060169494e0c0c1f0b27"
                                                   + "565521680e1bd6d498d283cc8ff3bb146448291b5353150c17050145044c56535b795973767b1b1a"
                                                   + "5f457d5445160c031d6a392727230e0924023a28223c1502140b07644a4a0918593c232c2d517855"
                                                   + "5e56175e44a2f1c1bcd2fc85f0f14d0d5c5b190103310b0222644e46575f454b5544173718475d5b"
                                                   + "29680950747420786b6c017c2e464746150a461a213e065b281358560201605d505d07415151475c"
                                                   + "561b"));

    }

    public static void test1() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        map.put("account", "ecp");
        map.put("password", "2O^47pGE");
        map.put("receiver", "13989455787");
        map.put("message", "12444");
        map.put("businessID", "1000");
        map.put("time", Calendar.getInstance().getTimeInMillis() / 1000);
        map.put("remark", "124");

        HttpClientPool.getInstance().postMethod("http://192.168.100.250:8081/SmsPush/send", map);

    }

    public static void test2() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        map.put("mobile", "13989455787");
        map.put("authCode", "13456");
        map.put("password", "22233");

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount", map);

        String ret = HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount?mobile=13989455787&authCode=13456&password=123235",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    public static void test3() throws JSONException, ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/CI/hcode2/13617137075", 10000);
        System.out.println(ret);
    }

    public static void test4() throws JSONException, ClientProtocolException, IOException {
        String ret = ZXAgentUtil.createAccount("http://192.168.21.201:8073/yxzq/RecvRequest?account=yypt&password=yypt",
                                               "http://192.168.24.181:8080/yx-m/nnl/accountCallBack",
                                               "http://192.168.21.216/CI/hcode2/", "13989455787");
        System.out.println(ret);
    }

    /**
     * 测试时间格式化
     */
    public static void test5() {
        // yyyy-MM-dd hh:mm:ss
        System.out.println(DateUtil.getGeneralFormat().format(new Date()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()));

    }

    public static void test6() {
        try {
            String req = "";
            req = assembleJsonStr4MMl("18072852267", "5a7fdba6");
            com.alibaba.fastjson.JSONObject jsonReq = com.alibaba.fastjson.JSONObject.parseObject(req);
            ZteBean ZB = CreateUserBusiness.doZteCreate(jsonReq);
            ZB.setJsonOpinfo(req);

            MinaClient.getInstance().initMinaClient();
            Answer ack = MinaClient.getInstance().send("2014090514344900000052", ZB, 1);
            logger.debug(com.alibaba.fastjson.JSON.toJSONString(ack));
        } catch (Exception e) {
            // e.printStackTrace();
            // logger.error("服务错误：" + e.toString());
        }
    }

    public static void test7() {
        /*Monitor m = Monitor.getInstance(ConfigUtil.MONITOR_PRODUCT_NAME);

        m.setXmppAddress(ConfigUtil.MONITOR_XMPP_ADDRESS);
        m.init();
        Monitor.increase("CREATE_USER_ACCOUNT1"); // 记数+1
        Monitor.increase("CREATE_USER_ACCOUNT1");
        Monitor.setAttributeCount("CREATE_USER_ACCOUNT1", 99);
        // System.out.println( Monitor.getAttributeCount(ConfigUtil.MONITOR_PRODUCT_NAME) );*/

    }

    public static void test8() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        map.put("mobile", "13989455787");

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/getAuthCode?mobile=18072852267",
        // map);

        // String ret =
        // HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/getAuthCode?mobile=13989455787", 1000
        // * 60 * 3);
        String ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/getAuthCode?mobile=18072939353",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    public static void test9() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount", map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 100; i++) {
            watch.split();
            ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/checkAuthCodeAndAccount?mobile=18072852267&authCode=004186&password=5a7f11",
                                                         1000 * 60);

            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime());
        }
    }

    public static void test10() {
        String str1 = "3.0.0.0";
        String str2 = "2.7.0.0.0";
        logger.error(str1.compareTo(str2) > 0);
    }

    public static void test11() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount", map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 10; i++) {
            watch.split();
            ret = HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount?mobile=18072852267&authCode=053411&password=5a7fff",
                                                         1000 * 60);

            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test12() throws JSONException, ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/upgrade?version=3.1.0.2", 10000);
        System.out.println(ret);
    }

    public static void test13() {
        System.out.println(ConfigUtil.ANDROID__NEW_DESCRIPTION);
    }

    public static void test14() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount", map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 1000; i++) {
            watch.split();
            // ret =
            // HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/services/http/remote/queryUserEp?userId=1921332",
            // 1000 * 6);
            ret = HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-it/services/http/remote/queryUserEp?userId=1921332",
                                                         1000 * 6);
            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    /**
     * 协同通信接口upgrade
     * 
     * @throws JSONException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test15() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount", map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 1; i++) {
            watch.split();
            // ret =
            // HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/services/http/remote/queryUserEp?userId=1921332",
            // 1000 * 6);
            ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/upgrade?version=3.1.0.2", 1000 * 6);
            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test16() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"userId\":81192787,\"ecpAccount\":\"057181933844\",\"ecpPassword\":\""
                       + AlgorithmUtil.md5("07136231227") + "\"}";
        // String param = "{\"userId\":3732001,\"ecpAccount\":\"\",\"ecpPassword\":\"\"}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/bindEcp", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8082/yx-it/services/http/remote/bindEcp", param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.ephonelive.com.cn/services/http/remote/bindEcp",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test17() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"userId\":127007}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/unbindEcp",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test18(String ecpAccount) {
        try {
            DBEnviroment2006 dbEnv = new DBEnviroment2006();
            Connection conn = dbEnv.getConnection();
            if (conn == null) {
                System.out.println("连接失败：Null connection");
            } else {
                PreparedStatement stmt = null;
                try {//
                    stmt = conn.prepareStatement("select t1.uid,t1.realname,t1.businessphone,t2.pass,t2.validflag from uinfo as t1,uconf as t2 "
                                                 + "where t1.uid=t2.uid and t1.uid = ?");
                    if (stmt == null) {
                        System.out.println("stmt为空！");
                        return;
                    }
                    stmt.setString(1, ecpAccount + "@ecplive.com");
                    ResultSet rs = stmt.executeQuery();
                    if (rs != null) {
                        if (rs.next()) {
                            System.out.println("账户信息如下：");
                            System.out.println("uid: " + rs.getString(1));
                            System.out.println("realname: " + rs.getString(2));
                            System.out.println("businessphone: " + rs.getString(3));
                            System.out.println("pass: " + rs.getString(4));
                            System.out.println("validflag: " + rs.getString(5));
                        } else {
                            System.out.println("rs.next()为空");
                        }
                    } else {
                        System.out.println("无该用户账户！！");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    dbEnv.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void test19() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"userId\":127007}";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", "127007");
        map.put("userName", "roy");
        // param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-it/services/http/remote/unbindEcp",
                                                             map);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test20(String mobile) throws ClientProtocolException, IOException {
        System.out.println("调用CRM销户接口：");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", mobile);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.100.146/ecp-ii/rop?channel=8000&type=300",
                                                             map);
        System.out.println("接口数据返回：\t" + ret);
    }

    public static void test21() throws ClientProtocolException, IOException {
        Jedis jedis = new Jedis("192.168.21.2", 6379);
        Set<String> set = jedis.keys("queryEcpInfo_uid_*");
        String value = "";
        File f = new File("/data/guoj/temp/redisInfo.txt");// 新建一个文件对象
        FileWriter fw = new FileWriter(f);// 新建一个FileWriter

        for (String key : set) {
            value = jedis.get(key);
            key = StringUtils.rightPad(key, 27, " ");
            try {
                fw.append(key + " :\t" + value + "\r\n");// 将字符串写入到指定的路径下的文件中
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        fw.close();
        jedis.close();
    }

    public static void test22(String[] args) throws ClientProtocolException, IOException {
        if (args.length < 3) {
            System.out.println("usage: arg1  arg2   arg3");
            return;
        }
        System.out.println("调用绑定一号通接口：");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", args[0]);
        map.put("b", args[1]);
        map.put("c", args[3]);
        String param = EncryptUtil.encryptFull(JSON.toJSON(map).toString());
        String ret = HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/setPhoneNumber",
                                                             param);
        System.out.println("接口数据返回：\t" + ret);
    }

    public static void test23() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"mobile\":13989455787}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/sendAuthCode",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test24() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"mobile\":13989455787," + "\"code\":998000}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/checkAuthCode",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * 企业激活
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test25() throws ClientProtocolException, IOException {
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        /*
         * String param = "{"+ "\"mobile\":\"13588361351\","+ "\"domain_id\":\"657853\","+
         * "\"domain_name\":\"易信测试企业\","+ "\"domain\":\"xy.entser.com\","+ "\"account_id\":\"111111\","+
         * "\"account_name\":\"zs\","+ "\"nickname\":\"周松\","+ "\"node\":\"hz\""+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"18268159516\","+ "\"domain_id\":\"871107\","+
         * "\"domain_name\":\"挖掘机哪家强邮件系统\","+ "\"domain\":\"yixin04.entser.com\","+ "\"account_id\":\"332752055\","+
         * "\"account_name\":\"admin\","+ "\"nickname\":\"admin\","+ "\"node\":\"hz\""+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"13257143729\","+ "\"domain_id\":\"871107\","+ "\"domain_name\":\"wjj\","+
         * "\"domain\":\"wjj.entser.com\","+ "\"account_id\":\"332752055\","+ "\"account_name\":\"admin\","+
         * "\"nickname\":\"admin\","+ "\"node\":\"hz\""+ "}";
         */

        String param = "{" + "\"mobile\":\"15377115061\"," + "\"domain_id\":\"944445\","
                       + "\"domain_name\":\"zs的企业(zs.entser.com)\"," + "\"domain\":\"zs.entser.com\","
                       + "\"account_id\":\"332752055\"," + "\"account_name\":\"admin\"," + "\"nickname\":\"admin\","
                       + "\"node\":\"hz\"" + "}";

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/activateEp4Qiye",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/activateEp4Qiye",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        watch.split();
        logger.error(watch.getTime() / 1000d);
        logger.error(ret);
    }

    /**
     * 解散企业
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test25_1() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"domain_id\":\"999\"," + "\"domain\":\"\"}";

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/deleteCompany4Qiye",
        // param);
        // ret = EncryptUtil.decryptFull(ret);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/deleteCompany4Qiye",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/deleteCompany4Qiye",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://pl.b.yixin.im/services/http/remote/deleteCompany4Qiye",
                                                             param);
        logger.error(ret);
    }

    /**
     * queryMobile
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test26() throws ClientProtocolException, IOException {
        String param = "{" + "\"mobile\":\"13989455787\"," + "\"epMail\":\"guojun@yixin.im\"," + "}";

        /*
         * String param = "{"+ "\"mobile\":\"13588361351\","+ "\"epMail\":\"yixin001@zs.entser.com\","+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"15397100246\","+ "\"epMail\":\"chenyuan01@yixin02.entser.com\","+ "}";
         */
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/queryMobile",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/queryMobile",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/queryMobile",
                                                             param);
        // String ret = HttpClientPool.getInstance().postMethod("http://pl.b.yixin.im/services/http/remote/queryMobile",
        // param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * unbindMail
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test27() throws ClientProtocolException, IOException {
        /*
         * String param = "{"+ "\"mobile\":\"13989455787\","+ "\"epMail\":\"gj@zs.entser.com\""+ "}";
         */

        String param = "{" + "\"mobile\":\"13989455787\"," + "\"epMail\":\"admin@zs.entser.com\"" + "}";
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/unbindMail",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/unbindMail",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/unbindMail",
                                                             param);
        logger.error(ret);
    }

    /**
     * bindMail
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test28() throws ClientProtocolException, IOException {
        /*
         * String param = "{"+ "\"mobile\":\"15868163293\","+ "\"uid\": 2626002,"+
         * "\"epMail\":\"test@yixin01.entser.com\","+ "}";
         */
        String param = "{" + "\"mobile\":\"13989455787\"," + "\"uid\": 3732001,"
                       + "\"epMail\":\"admin@zs.entser.com\"," + "}";
        /*
         * String param = "{"+ "\"mobile\":\"13989455787\","+ "\"uid\": 3732001,"+
         * "\"epMail\":\"admin@xy.entser.com\","+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"13588361351\","+ "\"uid\": 4719001,"+
         * "\"epMail\":\"admin@yixin02.entser.com\","+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"13588361351\","+ "\"uid\": 4719001,"+
         * "\"epMail\":\"admin@zs.entser.com\","+ "}";
         */

        /*
         * String param = "{"+ "\"mobile\":\"18072852267\","+ "\"uid\": 4122001,"+
         * "\"epMail\":\"admin@yixin04.entser.com\","+ "}";
         */

        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/bindMail", param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/bindMail",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/bindMail",
        // param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * getUnreadMailCount
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test29() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":3732001," + "\"epMail\":\"gj@zs.entser.com\"" + "}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getUnreadMailCount",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * getUnreadurl
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test30() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":3732001," + "\"epMail\":\"admin@zs.entser.com\"}";
        param = EncryptUtil.encryptFull(param);
        /*
         * String ret = HttpClientPool .getInstance() .postMethod(
         * "http://localhost:8080/yx-plat/services/http/remote/getUnreadurl", param);
         */
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/getUnreadurl",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test31() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":3732001," + "\"epMail\":\"gj@zs.entser.com\","
                       + "\"maillist\":\"roy2010a@163.com,guojun@yixin.im\"}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getSendurl",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * getBindMail
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test32() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":3732001}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getBindMail",param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getBindMail",param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getBindMail",param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/getBindMail",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test33() throws ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://192.168.21.2/yx-addr/services/http/remote/allSyncCompanyMail?domain_id=657853",
                                                            500);
        logger.error(ret);
    }

    public static void test34() throws ClientProtocolException, IOException {
        String param = "{" + "\"epId\":3111," + "\"userId\":3732001" + "}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/quitEp",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    /**
     * 绑定商务号码
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test35() throws ClientProtocolException, IOException {
        String param = "{" + "\"yht\":\"057133344445\"," + "\"userId\":3732001," + "\"unbind\":\"1\"" + "}";
        // param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://127.0.0.1:8080/yx-it/services/http/remote/bind",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    /**
     * 测 q4
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test36() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"mobile\":\"13376338878\"," + "\"account\":\"\"," + "\"name\":\"\","
                       + "\"userId\":\"96238508\"}";

        /*
         * String param = "{\"cmd\":301," + "\"entry\":{ \"account\":\"\"," + "\"name\":\"改个名字\"," +
         * "\"mobile\":\"13646600470\"" + "}," + "\"userId\":\"6842624\"," +
         * "\"id\":\"FF8696A54B074655A118F9FF02B55642\"}";
         */

        param = EncryptUtil.encryptFull(param);
        // String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/q4",
        // param);
        // String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/q4",
        // param);
        // String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-it/services/http/remote/q4",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("https://192.168.21.2:8888/yx-it/services/http/remote/q4", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/q4", param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/q4", param);
        // String ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/itForAp",
        // param);
        ret = EncryptUtil.decryptFull(ret);
        logger.debug(ret);
    }

    /**
     * 测 q4
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test36_1() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{" + "\"userId\":\"3107001\"}";

        // param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/commuPerm",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * 验证易信用户名/密码
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test38() throws ClientProtocolException, IOException {
        String param = "";
        String ret = HttpClientPool.getInstance().postMethod("http://223.252.215.121/yx-ecp/auth?account=13989455787&pass=07136231227&accountType=3",
                                                             param);
        logger.error("ret :" + ret);
    }

    /***
     * 查计费资源
     * 
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void test39() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        String param = "{\"cmd\":\"10102\"," + "\"entry\":{ \"serviceId\":5001," + "\"userId\":\"3732001\"" + "},"
                       + "\"id\":\"888888880001\"}";

        // param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/queryAccountResources",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void testWatch() throws InterruptedException {
        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        TimeUnit.SECONDS.sleep(3);
        watch.split();
        System.out.println(watch.getTime());
        watch.stop();
        watch.reset();
        watch.start();

        TimeUnit.SECONDS.sleep(2);
        watch.split();
        System.out.println(watch.getTime());
    }

    public static void testDate() {
        Calendar ca = java.util.Calendar.getInstance();
        ca.setTimeInMillis(1445563353000L);
        System.out.println(ca.getTime());
    }

    public static void test40() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"3107001\"}";

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/hasactivity",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test41() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"3107001\"}";

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/queryactivitylist",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test42() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"4469028\"," + "\"activitytype\":\"2\"," + "\"activityid\":\"8\"}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/fetchmailresource",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test43() throws ClientProtocolException, IOException {
        String param = "13989455787";
        String ret = HttpClientPool.getInstance().postMethod("http://223.252.215.121/yx-ecp/queryUinfos", param);
        // String ret = HttpClientPool.getInstance().postMethod("http://service.yixin.im/ecp/queryUinfos", param);
        logger.error(ret);
    }

    public static void test43_1() throws ClientProtocolException, IOException {
        String uid = "34758";
        // String ret = HttpClientPool.getInstance().postMethod("http://223.252.215.121/yx-ecp/queryUinfos", param);
        String ret = HttpClientPool.getInstance().postMethod("http://223.252.215.121/yx-ecp/queryUinfoByUid?uid=" + uid,
                                                             "");
        logger.error(ConfigUtil.REDIS_LIST);
        logger.error(ret);
    }

    public static void test44() throws ClientProtocolException, IOException {
        String param = "";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.24.181:8080/yx-plat/services/http/remote/getservertime",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        /*
         * com.alibaba.fastjson.JSONObject obj = (com.alibaba.fastjson.JSONObject)(JSON.parseObject(ret).get("body"));
         * Date d = new Date(); d.setTime( Long.valueOf(obj.getString("currenttime")) );
         */
        logger.error(ret);
    }

    public static void test45() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"2626002\"}";
        param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getsmslist",
        // param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test45_1() throws ClientProtocolException, IOException {
        String param = "{\"userId\":\"3034010\"," + "\"cmd\":\"501\"," + "\"id\":\"854ca291343d4798b66d8310199eb4e9\"}";
        // param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getsmslist",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/platForAp",
                                                             param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test46() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"48778345\"," + "\"page\":\"1\"," + "\"pagesize\":\"50\"}";
        String param1;

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        param1 = EncryptUtil.encryptFull(param);
        watch.start();
        for (int i = 1; i <= 1; i++) {
            ret = HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/yx-proxy/services/http/remote/getsmsmembers",
                                                          param1);
            // ret =
            // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmsmembers",
            // param1);
            // ret =
            // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/getsmsmembers",
            // param1);
            // ret =
            // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-proxy/services/http/remote/getsmsmembers",
            // param1);
            // ret =
            // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/getsmsmembers",
            // param1);

            watch.split();
            logger.error(i + ":\t" + watch.getTime());
            ret = EncryptUtil.decryptFull(ret);
            logger.error(i + ":\t" + ret);
        }

    }

    public static void test47() throws ClientProtocolException, IOException {
        /*
         * String param = "{\"uid\":\"78376256\"," + "\"groupid\":\"84ee338350abca47b3d1f5565a7b529a\"," +
         * "\"page\":\"1\"," + "\"pagesize\":\"100\"}";
         */
        String param = "{\"uid\":\"3971418\"," + "\"groupid\":\"3187e2c0e095ff4ca890bc6fbeb7c6bd\","
                       + "\"page\":\"1\"," + "\"pagesize\":\"100\"}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/querygroupsms",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/querygroupsms",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/querygroupsms",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test48() throws ClientProtocolException, IOException {
        /*
         * String param = "{\"uid\":\"78376256\"," + "\"groupid\":\"567e6fa1562e894e9785e27e838932a0\"," +
         * "\"page\":\"1\"," + "\"pagesize\":\"10\"}";
         */

        String param = "{\"uid\":\"3107001\"," + "\"groupid\":\"a61fc1d51e9efa4f8ea2468d5d05b54f\","
                       + "\"page\":\"1\"," + "\"pagesize\":\"10\"}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/queryvariablesmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/queryvariablesmslist",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/queryvariablesmslist",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test49() throws ClientProtocolException, IOException {
        /*
         * String param = "{\"contactuid\":\"\"," + "\"contactmobile\":\"13331326601\"," + "\"page\":\"1\"," +
         * "\"uid\":\"89824809\"," + "\"pagesize\":\"100\"}";
         */

        String param = "{\"contactuid\":\"\"," + "\"contactmobile\":\"057181933583\"," + "\"page\":\"1\","
                       + "\"uid\":\"2076001\"," + "\"pagesize\":\"100\"}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/queryp2psmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/queryp2psmslist",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/queryp2psmslist",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test50() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"2258001\"," + "\"groupid\":\"90f5edd26e28e247a6c2e77279d38588\","
                       + "\"page\":\"1\"," + "\"pagesize\":\"10\"}";
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/queryfailedsms",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/queryfailedsms",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/queryfailedsms",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test51() throws ClientProtocolException, IOException {
        String param = "{\"uid\":\"\"," + "\"smsId\":\"2201ba3e41157742bf53224f05740aa6\"}";

        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/querySmsDetail4Prepay",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/querySmsDetail4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://pl.b.yixin.im/services/http/remote/querySmsDetail4Prepay",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test52() {
        try {
            throw new java.lang.NoSuchMethodError();
        } catch (Error e) {
            logger.error("123");
        }
    }

    public static void test53() throws ClientProtocolException, IOException {
        String param = "{" + "\"mobile\":\"13989455787\"," + "\"epMail\":\"admin@zs.entser.com\"" + "}";

        /*
         * String param = "{"+ "\"mobile\":\"13989455787\","+ "\"epMail\":\"gs@xy.entser.com\""+ "}";
         */
        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/unbindMailForClient",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/unbindMailForClient",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-plat/services/http/remote/unbindMailForClient",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test54() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":\"0\"," + "\"mobile\":\"13989455787\"," + "\"epMail\":\"gj@zs.entser.com\","
                       + "\"verifyCode\":\"111111\"" + "}";

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/bindMail",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * 测试动态调度
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test55() throws ClientProtocolException, IOException {
        String param = "{" + "\"cronExpress\":\"10 06 12 29 12 ? 2015\"," +
        // "\"cronExpress\":\"55 31 9 * * ?\"," +
        // "\"cronExpress\":\"0/5 * * * * ?\"," +
                       "\"batch\":\"3\"" + "}";

        // param = EncryptUtil.encryptFull(param);it.ephonelive.com.cn
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-it/services/http/remote/sendSysMsg",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.ephonelive.com.cn/services/http/remote/sendSysMsg",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test56() throws ClientProtocolException, IOException {

        String filepath = "D:\\54.txt";
        dealFile(new File(filepath), "UTF-8");
    }

    public static void test57() throws ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://localhost:6017/notify?uid=3732001&phone=13989455787&clientType=52&online=1&token=590bbf6f-eb7c-41f9-9b42-e8baf62a317a",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    public static void test58() throws ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://localhost:6017/user/3732001?token=d2adff65-ed6e-4bb6-8670-0d820f378f34",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    /**
     * 解绑一号通
     * 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test59() throws ClientProtocolException, IOException {
        String param = "{" + "\"userId\":\"89047965\"" + "}";

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://pp.ephonelive.com.cn/phonepool/cancelPhoneNumber",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    // 写文件
    public static void test60() throws ClientProtocolException, IOException {

        String filepath = "D:\\541.txt";
        // FileUtils.writeStringToFile(new File(filepath),"123");
        OutputStream out = null;
        try {
            out = FileUtils.openOutputStream(new File(filepath));
            IOUtils.write("123", out, "utf-8");
            IOUtils.write(IOUtils.LINE_SEPARATOR, out, "utf-8");
            IOUtils.write("123", out, "utf-8");
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public static void test61() throws ClientProtocolException, IOException {

        File inputFile = new File("D:\\test\\54_in.txt");
        File outputFile = new File("D:\\test\\54_out.txt");

        BufferedReader reader = null;
        if (!inputFile.exists()) {
            System.out.println(" file(" + inputFile.getAbsolutePath() + ") does not exist.");
            return;
        }
        try {
            reader = getBufferReaderFromFile(inputFile, "utf-8");
            // dealFileLineByLine1(reader, outputFile);
            dealFileLineByLine(reader);
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

    }

    public static void test62() throws ClientProtocolException, IOException {
        String param = "{" + "\"cmd\":\"10102\"," + "\"entry\":{" + "\"serviceId\":\"5002\","
                       + "\"userId\":\"3732001\"" + "}," + "\"id\":\"888888880001\"" + "}";

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://it.ephonelive.com.cn/services/http/remote/queryAccountResources",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/queryAccountResources",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    public static void test63() throws ClientProtocolException, IOException {
        String param = "";
        String ret = HttpClientPool.getInstance().postMethod("http://223.252.197.132:8284/check-phone-time?uid=8054098",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://yxapi.nis.netease.com/check-phone-time?uid=112027321",param);
        logger.error(ret);
    }

    public static void test64() throws ClientProtocolException, IOException {
        String param = "{" + "\"uid\":2076001," + "\"timeStamp\":\"0\"" + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/findMsgAndPush",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/findMsgAndPush",
        // param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    public static void test65() throws ClientProtocolException, IOException {
        String param = "{" + "\"entry\":{\"timeStamp\":\"0\"" + "}," + "\"id\":\"3D7550D154C246589DFD9FB6A2590F90\","
                       + "\"cmd\":\"408\"," + "\"userId\" : 5264005" + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        // param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/platForAp",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/findMsgAndPush",
        // param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    public static void test66() throws ClientProtocolException, IOException {
        String param = "{\"userId\":\"3034010\"," + "\"cmd\":\"504\"," + "\"id\":\"854ca291343d4798b66d8310199eb4e9\"}";
        // param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/platForAp",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/platForAp",
                                                             param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test67() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":\"2626002\"}";
        String param = "{\"userId\":\"4122001\"}";
        // param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/platForAp",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-addr/services/http/remote/userAddr",
                                                             param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test68() throws ClientProtocolException, IOException {
        // String param = "{\"userId\":\"2626002\"}";
        String param = "{\"userId\":\"4122001\"}";
        // param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/platForAp",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-plat/services/http/remote/authorize",
                                                             param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test69() throws ClientProtocolException, IOException {
        String param = "{" + "\"id\":\"2a3404da05c541f6b3a9bb562a9c08d9\"," + "\"userId\":7972027," + "\"cmd\":102,"
                       + "\"entry\":{\"addrId\":2196968,\"depId\":0,\"ts\":0}" + "}";

        // param = EncryptUtil.encryptFull(param);
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/getsmslist",
        // param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/services/http/remote/getsmslist",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-addr/services/http/remote/queryByAP",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-plat/services/http/remote/platForAp", param);

        watch.split();
        logger.error(":\t" + watch.getTime());
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test70() throws ClientProtocolException, IOException {
        String param = "{" + "\"mobile\" : 13989455787" + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/getCphoneInfo",
        // param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);

    }

    public static void test71() throws ClientProtocolException, IOException {

        String ret;
        // String param = "{\"ecpAccount\":\"huangsuqin.xc\"}";
        String param = "{\"ecpAccount\":\"057181933844\"}";
        // String param = "{\"ecpAccount\":\"57187396623\"}";
        // String param = "{\"ecpAccount\":\"57187397010\"}"; //wu.hh
        // String param = "{\"ecpAccount\":\"057181932772\"}";//hsh
        // String param = "{\"ecpAccount\":\"rj7888@toocle.com\"}";
        param = EncryptUtil.encryptFull(param);

        ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/getEcpAccountInfo",
                                                      param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test72() throws ClientProtocolException, IOException {
        String param = "{" + "\"key\" : \"queryEcpid_uid_3732001\"" + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/fetchRedisValueByKey",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test73() throws ClientProtocolException, IOException {
        String param = "{" + "\"userId\":77346148," + "\"command\":1001," + "\"data\":60" + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-phone/phonepool/postPortalMessage", param);
        String ret = HttpClientPool.getInstance().postMethod("http://pp.b.yixin.im/phonepool/postPortalMessage", param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test74() throws ClientProtocolException, IOException {
        String param = "{" + "\"userId\":48778345," + "}";
        /*
         * String param = "{"+ "\"uid\":3107001,"+ "\"timeStamp\":\"0\"" + "}";
         */

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-phone/phonepool/qryEcpInfo4Prepay", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/qryEcpInfo4Prepay",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test75() throws ClientProtocolException, IOException {
        String param = "{" + "\"userId\":111742902," + "}";

        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-phone/phonepool/qryEcpInfo4Prepay", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/cc", param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test76() throws ClientProtocolException, IOException {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Request><TransactionID>"
                       + "<F>1000000063201512300014827396</F></TransactionID><RspCode>0000</RspCode><RspDesc>成功</RspDesc></Request>";

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-phone/phonepool/qryEcpInfo4Prepay", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://it.b.yixin.im/services/http/remote/crmccb", param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test77() throws ClientProtocolException, IOException {
        String param = "{" + "\"key\":\"queryEcpInfo_uid_3732001\"," + "\"value\":\"1_1\"" + "}";

        param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-phone/phonepool/qryEcpInfo4Prepay", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/resolveEcpinfo4Prepaysys",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test78() throws ClientProtocolException, IOException {
        String param = "{" + "\"id\":\"a3f438bc-7f00-4f94-b67c-e9bcc06360ed\"," + "\"cmd\":\"506\"" + "}";

        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-proxy/services/http/remote/hcode2", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-phone/phonepool/qryEcpInfo4Prepay", param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://106.2.44.240/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://pl.ephonelive.com.cn/icg-proxy/communicate?fromUid=108041633&fromClientType=1002",
                                                             param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    /**
     * freemaker template
     */
    public static void test79() {
        Configuration cfg = new Configuration();
        TemplateLoader templateLoader = new ClassTemplateLoader(MainTest.class, "/");
        cfg.setTemplateLoader(templateLoader);
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Map<String, String> param = new HashMap<String, String>();
        param.put("account", "guoj");

        StringWriter out = new StringWriter();
        try {
            Template temp = cfg.getTemplate("bindEmail.ftl");
            temp.process(param, out);
        } catch (Exception e) {
            logger.error(e.getMessage());

        }
        logger.error(out.toString());

    }

    public static void test80() throws Exception {
/*        RSAUtil rsaUtil = RSAUtil.getInstance();
        rsaUtil.generateKeyPair("D://test//", "test");
        String strPriKey = rsaUtil.getKeyContent("D://test//test_priKey.txt");
        rsaUtil.verify(rsaUtil.getKeyContent("D://test//test_pubKey.txt"), "guoj",
                       new String(rsaUtil.signature(strPriKey, "guoj"), "utf-8"));*/
    }

    public static void test81() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        Dto1 dto1 = new Dto1("guo", 33, true);
        System.out.println(dto1);
        System.out.println(mapper.map(dto1, Dto2.class));

        Pattern pattern = Pattern.compile("\\s*[,]+\\s*");
        for (String str : pattern.split(" guoj,jun")) {
            System.out.println(str);
        }
    }

    public static void test82() throws Exception {
        ThreadLocal<String> threadLocal = new ThreadLocal<String>();
        threadLocal.set("guo");

        final InheritableThreadLocal<String> inheritThreadLocal = new InheritableThreadLocal<String>();
        inheritThreadLocal.set("guoj1");

        Thread thread1 = new Thread(new Runnable() {

            private ThreadLocal<String> threadLocal = inheritThreadLocal;

            public ThreadLocal<String> getThreadLocal() {
                return threadLocal;
            }

            public void setThreadLocal(ThreadLocal<String> threadLocal) {
                this.threadLocal = threadLocal;
            }

            @Override
            public void run() {
                Thread currThread = Thread.currentThread();
                System.out.println("thread1:" + Thread.currentThread());

                System.out.println("thread1:" + threadLocal.get());
            }
        });
        thread1.start();
        thread1.join();
        System.out.println("test82: " + threadLocal.get());
        ThreadLocal<String> threadLocal1 = new ThreadLocal<String>();
        threadLocal1.set("jun");
        System.out.println("test82: " + Thread.currentThread());
        System.out.println("test82: " + threadLocal1.get());
    }

    public static void test83() {
        // System.out.println(NetUtils.getLocalHost());
        // Locale currentLocale = Locale.getDefault();
        Locale currentLocale = new Locale("zh", "CN");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources", currentLocale);
        System.out.println(resourceBundle.getString("name"));
        ;

    }

    /**
     * BigDecimal全测试
     */
    public static void test84() {
        BigDecimal bigDecimal = BigDecimal.valueOf(2045L);
        System.out.println(bigDecimal.divide(BigDecimal.valueOf(1000L), 2, BigDecimal.ROUND_HALF_UP));
        System.out.println(new BigDecimal("123.789").toPlainString());

        BigDecimal bd1 = new java.math.BigDecimal(1911.11);
        BigDecimal bd2 = new java.math.BigDecimal(10.12);

        BigDecimal bd3 = bd1.multiply(bd2);
        System.out.println("bd3: ======" + bd3);
        System.out.println("bd3.doubleValue():＝＝＝＝＝＝＝"+ bd3.doubleValue());

        NumberFormat nf = new DecimalFormat("#,###.####");
        System.out.println(nf.format(bd3.doubleValue()));

        System.out.println(bd3.setScale(4, RoundingMode.HALF_UP));

        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        System.out.println(bd3.round(mc));

        BigDecimal bd4 = BigDecimal.valueOf(1223, 2);
        System.out.println(bd4);

    }

    public static void test85() {
        ConcurrentHashMap<Dto1, String> map = new ConcurrentHashMap<Dto1, String>();
        map.put(new Dto1("guo", 1, true), "1");
        map.put(new Dto1("guo", 2, true), "2");
        map.put(new Dto1("guo", 3, true), "3");
        map.put(new Dto1("guo", 4, true), "4");
        map.put(new Dto1("guo", 5, true), "5");
        map.put(new Dto1("guo", 6, true), "6");
        System.out.println(map);
        map.remove(new Dto1("guo", 2, true));
        System.out.println(map);

        HashMap<Dto1, String> map1 = new HashMap<Dto1, String>();
        map1.put(new Dto1("guo1", 1, true), "1");
        map1.put(new Dto1("guo1", 2, true), "2");
        map1.put(new Dto1("guo1", 3, true), "3");
        map1.put(new Dto1("guo1", 4, true), "4");
        map1.put(new Dto1("guo1", 5, true), "5");
        map1.put(new Dto1("guo1", 6, true), "6");
        System.out.println(map1);
        map1.remove(new Dto1("guo1", 2, true));
        System.out.println(map1);
    }

    public static void test86() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(new DemoRunner(lock));
        Thread t2 = new Thread(new DemoRunner(lock));
        t1.start();
        // t2.start();
        t1.join();
        // t2.join();
    }

    public static void test87() throws Throwable {
/*        SFtpClient sftpClient = new SFtpClient("192.168.20.170", 22, "dubbo", "dubbo");
        Vector<LsEntry> vector = (Vector<LsEntry>) sftpClient.listFiles("/home/dubbo");

        for (Iterator<LsEntry> iterator = vector.iterator(); iterator.hasNext();) {
            System.out.println(iterator.next().getFilename());
        }*/
    }

    public static void test88() throws JSONException, ClientProtocolException, IOException {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("source", "31641035"));
        list.add(new BasicNameValuePair("url_long", "https://fx.lianlianpay.com/"));
        System.out.println(URLEncodedUtils.format(list, "utf-8"));

        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        String ret = HttpClientPool.getInstance().getMethod("http://api.t.sina.com.cn/short_url/shorten.json?"
                                                                    + URLEncodedUtils.format(list, "utf-8"), 10000);
        watch.split();
        System.out.println(watch.getTime());
        System.out.println(ret);
    }

    public static void test89() throws JSONException, ClientProtocolException, IOException {

        HashMap map = new HashMap<String, String>();
        map.put("url", "https://fx.lianlianpay.com/");

        String ret = HttpClientPool.getInstance().postMethod("http://dwz.cn/create.php", map);

        System.out.println(ret);
    }
    
    public static void test90() throws JSONException, ClientProtocolException, IOException {
        String str= "123 kmv".replaceAll("[^a-zA-Z0-9]", "").trim();
        System.out.println(str);
    }
    
    /**
     * <ul>下面这三者class/classLoader去resource等价
     * <li>cls.getResource("123.properties")
     * <li>cls.getResource("/cn/zj/easynet/util/123.properties")
     * <li>cls.getClassLoader().getResource("cn/zj/easynet/util/123.properties") :cn前不能加/，
     * 见源码{@link Class#resolveName }
     * </ul>
     * @throws JSONException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void test91() throws JSONException, ClientProtocolException, IOException {
        Class<?> cls = MainTest.class;
        try {
            File file = new File(cls.getResource("123.properties").toURI());
//            File file = new File(cls.getResource("/cn/zj/easynet/util/123.properties").toURI());
//            File file = new File(cls.getClassLoader().getResource("cn/zj/easynet/util/123.properties").toURI());
            List<String> list = IOUtils.readLines(new FileInputStream(file), "utf-8");
//            List<String> list = IOUtils.readLines(MainTest.class.getClassLoader().getResourceAsStream("/cn/zj/easynet/util/123.properties"), "utf-8");
            for (String str : list) {
                System.out.println(str);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 验证java 正则
     * @throws InterruptedException
     */
    public static void test92() throws InterruptedException {
        Pattern package_pattern = Pattern.compile("package\\s+([$_a-zA-Z][.$_a-zA-Z0-9]*);");
        String code = "package com,netease.yixin;";
        
        Matcher matcher = package_pattern.matcher(code);
        
        if(matcher.matches()){
            System.out.println("matched!");
        } else{
            System.out.println(" not matched!");
        }
        
        /*String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        System.out.println(pkg);*/
    }
    
    
    public static void test93() {
        String savePath = "D:\\test1";
        File csvDir = new File(savePath);
        deleteDir(csvDir);
    }
    
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    /**
     * 测试classLoader
     * @throws Exception
     */
    public static void test94() throws Exception {
        URL urls[] = new URL[1];
        urls[0] = new URL("file:/Users/apple/Desktop/selenium2Demo.jar");
        URLClassLoader loader = new URLClassLoader(urls);
        Class<?> cls =  (Class<?>)loader.loadClass("org.test.impl.Cat");
        Thread.currentThread().setContextClassLoader(loader);
        
        System.out.println( Class.forName("org.test.impl.Cat",true,Thread.currentThread().getContextClassLoader()) );
//        System.out.println( Class.forName("cn.zj.easynet.util.Money").getClassLoader() );
        System.out.println( MainTest.class.getClassLoader() );

        URLClassLoader loader1 = new URLClassLoader(new URL[]{new URL("file:e:/test/selenium.jar")}, loader);
        Class<?> cls1 =  (Class<?>)loader1.loadClass("org.test.impl.Cat");
        
        System.out.println("jar:\t" + cls.getClassLoader() +"\t" +cls.getClassLoader().getParent());
        System.out.println("jar:\t" + cls1.getClassLoader() +"\t" +cls1.getClassLoader().getParent());
        System.out.println(Animal.class.getClassLoader() + ""  + Animal.class.getClassLoader().getParent()); 
        System.out.println( "当前线程:\t" +Thread.currentThread().getContextClassLoader() );//当前线程的classLoader
        
        Animal obj = (Animal) cls.newInstance();
        
        obj.running();
        
        /*Method method = cls.getDeclaredMethod("running");
        method.invoke(obj);*/
    }
    
    public static void test95(){
    	ConcurrentHashMap< String, String> map = new ConcurrentHashMap<String, String>();
//    	System.out.println( map.put("1", "guoj") );
    	System.out.println( map.putIfAbsent("1", "guoj1") );
        System.out.println(map.get("1"));
    }

    public static void main(String[] args) throws Throwable {
        // test1();
        // test2();
        // test3();
        // test4();
        // test5();
        // test6();
        // test7();
        // test8();
        // test9();
        // test10();
        // test11();
        // test12();
        // test13();
        // test14();
        // test15();
        // test17();
        // test18(args[0]);
        // test20(args[0]);
        // test21();
        // System.out.println( AlgorithmUtil.md5("07136231227") );
        // test22(args);
        // test23();
        // test24();
        // test37();
        // test25();
        // test25_1();
        // test26();
        // test27();
        // test28();
        // test53();
        // test29();
        // test30();
        // test31();
        // test32();
        // test37();
        // test33();
        // test34();
        // test35();
        // test36();
        // test36_1();
        // test38();
        // testWatch();
        // test39();
        // test40();
        // test41();
        // test42();
        // test43();
        // test43_1();
        // test44();
        // test45();
        // test45_1();
        // testDate();
        // test46();
        // test47();
        // test48();
        // test49();
        // test50();
        // test51();
        // test52();
        // test53();
        // test54();
        // test55();
        // test56();
        // test57();
        // test58();
        // test59();
        // test60();
        // test61();
        // test62();
        // test3();
        // test36();
        // test3();
        // test63();
        // test64();
        // test65();
        // test67();
        // test68();
        // test69();
        // test70();
        // test71();
        // test72();
        // test73();
        // test74();
        // test75();
        // test76();
        // test77();
        // test78();
        // testNegative1();
        // test79();
        // test80();
        // test81();
        // test82();
        // test83();
         test84();
        // test85();
        // test86();
//        test87();
//        test88();
//        test90();
//        test91();
//        test92();
        /*String uuid = UUID.randomUUID().toString();
        System.out.println(uuid.replaceAll("-", ""));
        System.out.println(uuid.replaceAll("[^0-9a-zA-Z]", ""));*/
        
//        test94();
    	/*Dto1 dto1 =  new Dto1();
    	Dto2 dto2 =  new Dto2();
    	System.out.println(dto1.temp(dto2.getClass()).getClass());*/
    }
}
