package cn.zj.easynet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONException;

import cn.zj.easynet.device.http.HttpClientPool;
import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.CreateUserBusiness;
import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.remote.dto.ZxRequestEntryDto;
import cn.zj.easynet.remote.request.ZxAccountReq;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.DBEnviroment2006;
import cn.zj.easynet.util.DateUtil;
import cn.zj.easynet.util.EncryptUtil;
import cn.zj.easynet.util.IConstants;
import cn.zj.easynet.util.Utils;
import cn.zj.easynet.util.ZXAgentUtil;

import com.alibaba.fastjson.JSON;
import com.netease.om.Monitor;

public class MainTestAbandon {

    private static final Logger logger = Logger.getLogger(MainTestAbandon.class);

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
         * entry.setBindAuthorization (IConstants.ZX_CMD_ENTRY_BIND_AUTHORIZATION);// 绑定号码权限1: 网内, 2:市话权限3: 国内长途权限, 4:
         * // 国际长途权限 , 默认权限: 3 ---3 entry.setCallAuthorization(IConstants .ZX_CMD_ENTRY_CALL_AUTHORIZATION);// 呼叫限制权限 1:
         * 网内, 2: 市话权限, 3: 国内长途权限, 4: // 国际长途权限 , 默认权限: 3---3
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
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount",
        // map);

        String ret = HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount?mobile=13989455787&authCode=13456&password=123235",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    public static void test3(String mobile) throws JSONException, ClientProtocolException, IOException {
        String ret = HttpClientPool.getInstance().getMethod("http://192.168.21.216/CI/hcode2/" + mobile, 10000);
        System.out.println(ret);
    }

    public static void test12() throws JSONException, ClientProtocolException, IOException {
        // String ret =
        // HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/upgrade?version=3.1.0.2",
        // 10000);
        String ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/upgrade4ios?version=3.0.1.1",
                                                            10000);
        // String ret =
        // HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/upgrade4ios?version=3.1.0",
        // 10000);
        System.out.println(ret);
    }

    public static void test4() throws JSONException, ClientProtocolException, IOException {
        String ret = ZXAgentUtil.createAccount("http://192.168.21.201:8073/yxzq/RecvRequest?account=yypt&password=yypt",
                                               "http://192.168.24.181:8080/yx-m/nnl/accountCallBack",
                                               "http://192.168.21.216/CI/hcode2/", "13989455787");
        System.out.println(ret);
    }

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
        Monitor.setAttributeCount("CREATE_USER_ACCOUNT1", 99);*/
        // System.out.println(
        // Monitor.getAttributeCount(ConfigUtil.MONITOR_PRODUCT_NAME) );

    }

    public static void test8() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        map.put("mobile", "18072852267");

        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/getAuthCode?mobile=18072852267",
        // map);

        String ret = HttpClientPool.getInstance().getMethod("http://localhost:8080/yx-m/nnl/getAuthCode?mobile=13989455787",
                                                            1000 * 60 * 3);
        logger.debug(ret);
    }

    public static void test9() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount",
        // map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 100; i++) {
            watch.split();
            ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/checkAuthCodeAndAccount?mobile=18072852267&authCode=004186&password=5a7f11",
                                                         1000 * 60);

            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test11() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount",
        // map);

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

    public static void test14() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount",
        // map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 1; i++) {
            watch.split();
            // ret =
            // HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/services/http/remote/queryUserEp?userId=1921332",
            // 1000 * 6);
            ret = HttpClientPool.getInstance().getMethod("http://192.168.21.2/yx-it/services/http/remote/queryUserEp?userId=1921332",
                                                         1000 * 6);
            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test15() throws JSONException, ClientProtocolException, IOException {
        HashMap map = new HashMap<String, String>();
        /*
         * map.put("mobile", "13989455787"); map.put("authCode", "13456"); map.put("password", "22233");
         */
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-m/nnl/checkAuthCodeAndAccount",
        // map);

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        for (int i = 1; i <= 300; i++) {
            watch.split();
            // ret =
            // HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/services/http/remote/queryUserEp?userId=1921332",
            // 1000 * 6);
            ret = HttpClientPool.getInstance().getMethod("http://up.ecpchina.com/nnl/upgrade?version=3.1.0.2", 1000 * 6);
            logger.error(i + ":\t" + ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test10() {
        String str1 = "3.0.0.0";
        String str2 = "2.7.0.0.0";
        logger.error(str1.compareTo(str2) > 0);
    }

    public static void test13() {
        System.out.println(ConfigUtil.ANDROID__NEW_DESCRIPTION);
    }

    public static void test16() throws ClientProtocolException, IOException {
        // String param =
        // "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        // String param =
        // "{\"userId\":127007,\"ecpAccount\":\"huangsuqin.xc\",\"ecpPassword\":\"7ebafa886b3dd44d210d1b2a00e867ee\"}";
        String param = "{\"userId\":2258001,\"ecpAccount\":\"057181932772\",\"ecpPassword\":\"eeacab1254870c76654c8e8d23996253\"}";
        // String param =
        // "{\"userId\":3107001,\"ecpAccount\":\"57187396623\",\"ecpPassword\":\"5cdbbb241ea63e9f1d6854df8dee14d6\"}";
        // String param =
        // "{\"userId\":2711001,\"ecpAccount\":\"57187397010\",\"ecpPassword\":\"6536c59faa48f5bf11b4479ce6978532\"}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-it/services/http/remote/bindEcp",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test17() throws ClientProtocolException, IOException {
        // String param =
        // "{\"userId\":127007,\"ecpAccount\":\"057181932640\",\"ecpPassword\":\"123456\"}";
        // String param = "{\"userId\":127007}";
        String param = "{\"userId\":2711001}";
        param = EncryptUtil.encryptFull(param);
        String ret = HttpClientPool.getInstance().postMethod("http://localhost:8080/yx-it/services/http/remote/unbindEcp",
                                                             param);
        ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test19() throws ClientProtocolException, IOException {

        String ret;
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        // String param = "{\"ecpaccount\":\"huangsuqin.xc\"}";
        // String param = "{\"ecpaccount\":\"057181933844\"}";
        // String param = "{\"ecpaccount\":\"57187396623\"}";
        // String param = "{\"ecpaccount\":\"57187397010\"}"; //wu.hh
        // String param = "{\"ecpAccount\":\"057181932772\"}";//hsh
        String param = "{\"ecpaccount\":\"rj7888@toocle.com\"}";
        param = EncryptUtil.encryptFull(param);

        for (int i = 1; i <= 1; i++) {
            watch.split();
            // ret =
            // HttpClientPool.getInstance().getMethod("http://pl.ephonelive.com.cn/services/http/remote/queryUserEp?userId=1921332",
            // 1000 * 6);
            ret = HttpClientPool.getInstance().postMethod("http://up.ecpchina.com/nnl/getEcpAccountInfo", param);
            // ret = EncryptUtil.decryptFull(ret);
            logger.error(ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
    }

    public static void test20() throws ClientProtocolException, IOException {
        String param = "{\"userId\":31070012}";
        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.24.181:8080/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        /*
         * String ret = HttpClientPool .getInstance() .postMethod(
         * "http://192.168.21.2/yx-it/services/http/remote/qryEcpInfo4Prepay", param);
         */
        org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
        watch.start();
        String ret = "";

        for (int i = 1; i <= 10; i++) {
            watch.split();
            ret = HttpClientPool.getInstance().postMethod("http://192.168.21.2/yx-it/services/http/remote/qryEcpInfo4Prepay",
                                                          param);
            logger.error(ret);
            logger.error(i + ":\t" + watch.getTime() / 1000d);
        }
        // ret = EncryptUtil.decryptFull(ret);
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

    public static void test22() throws ClientProtocolException, IOException {
        String param = "{\"cmd\":\"4\",\"entry\":{\"BUSINESSPHONE\":\"13257143729\",\"URL\":\"http://192.168.21.2:8082/yx-it/services/http/remote/zxQuerycb\"},\"id\":\"20141206201237000000101\"}";
        // param = EncryptUtil.encryptFull(param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://192.168.24.181:8080/yx-it/services/http/remote/qryEcpInfo4Prepay",
        // param);
        String ret = HttpClientPool.getInstance().postMethod("http://192.168.21.201:8073/yxzq/RecvRequest?account=yypt&password=yypt",
                                                             param);
        // String ret =
        // HttpClientPool.getInstance().postMethod("http://it.ephonelive.com.cn/services/http/remote/qryEcpInfo4Prepay",
        // param);
        // ret = EncryptUtil.decryptFull(ret);
        logger.error(ret);
    }

    public static void test23() throws ClientProtocolException, IOException {
        String param = "{\"mobile\":\"13989455787\"}";
        String ret = HttpClientPool.getInstance().postMethod("http://223.252.215.121/yx-ecp/queryUinfos", param);
        logger.error(ret);
    }

    public static void main(String[] args) throws Exception {
        // test1();
        // test2();
        // test3("13257143729");
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
        // test16();
        // test17();
        // test18(args[0]);
        test20();
        // test20();
        // test22();
        // test23();

    }
}
