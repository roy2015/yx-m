package cn.zj.easynet.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import cn.zj.easynet.remote.GetEcpAccountInfoServlet;
import cn.zj.easynet.remote.dto.EcpInfoReq;
import cn.zj.easynet.remote.dto.EcpInfoResp;


/****
 * 
 * @author Administrator
 *
 */
public class EcpInfoUtil {
	public static Logger logger = Logger.getLogger(EcpInfoUtil.class);
	
	
	public static void getEcpAccountInfo(EcpInfoReq req, EcpInfoResp resp){
		try{
			DBEnviroment2006 dbEnv = new DBEnviroment2006();
			Connection conn = dbEnv.getConnection();
			if (conn == null) {
				logger.error("获取数据库连接失败！！");
			}else{
				PreparedStatement stmt = null;
                try {//
                    stmt =	conn.prepareStatement("select t1.uid,t1.realname,t1.businessphone,t2.pass,t2.validflag from uinfo as t1,uconf as t2 "
                    		+ "where t1.uid=t2.uid and t1.uid = ?");
                    stmt.setString(1, req.getEcpAccount() + "@ecplive.com");
                    ResultSet rs = stmt.executeQuery();
                    if (rs != null) {
                        if (rs.next()) {
                        	resp.setEcpAccount(rs.getString(1));
                        	resp.setBusinessPhone(rs.getString(3));
                        	resp.setCode("200");
                        	resp.setMsg("succ");
                        	resp.setPass(rs.getString(4));
                        	resp.setRealName(rs.getString(2));
                        	resp.setValidFlag(rs.getString(5));
                        	
                        	logger.debug(req.getEcpAccount() + "账户信息如下：");
                        	logger.debug("uid: " + rs.getString(1) );
                        	logger.debug("realname: " + rs.getString(2) );
                        	logger.debug("businessphone: " + rs.getString(3) );
                        	logger.debug("pass: " + rs.getString(4) );
                        	logger.debug("validflag: " + rs.getString(5) );
                        }else{
                        	resp.setEcpAccount(req.getEcpAccount());
                        	resp.setCode("404");
                        	resp.setMsg("无此账号信息:" + req.getEcpAccount());
                        	logger.debug("rs.next()为空！");
                        }
                    }else{
                    	resp.setEcpAccount(req.getEcpAccount());
                    	resp.setCode("505");
                    	resp.setMsg("error");
                    	logger.debug("rs为空！");
                    }
			
				}catch(Exception e){
					resp.setEcpAccount(req.getEcpAccount());
                	resp.setCode("505");
                	resp.setMsg(e.getMessage());
				}finally{
					dbEnv.close();
				}
			}
		}catch(Exception e){
			resp.setEcpAccount(req.getEcpAccount());
        	resp.setCode("505");
        	resp.setMsg(e.getMessage());
		}
	}
}
