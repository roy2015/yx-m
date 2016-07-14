package cn.zj.easynet.util;

import java.util.Date;



import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import cn.zj.easynet.device.http.HttpClientPool;
import cn.zj.easynet.remote.AccountCallBackServlet;
import cn.zj.easynet.remote.dto.ZxRequestEntryDto;
import cn.zj.easynet.remote.request.ZxAccountReq;

public class ZXAgentUtil {
	private static Logger logger = Logger.getLogger(ZXAgentUtil.class);
	
	public static String createAccount(String zxAccountHttpUrl, String areaCodehttpUrl,String accountCallBackUrl, String phoneNbr) {
		/*ZxAccountReq req = new ZxAccountReq();
        req.setCmd(IConstants.ZX_CMD_ACCOUNT_CREATE);
        String timeStr = DateUtil.getGeneralFormat().format(new Date());
        req.setId(Utils.genZxCmdId(timeStr));//生成唯一编码
        ZxRequestEntryDto entry = new ZxRequestEntryDto();
        entry.setBusinessPhone(phoneNbr);
        entry.setCardNumber(phoneNbr);
//        entry.setUrl(accountCallBackUrl);

        entry.setUserType(IConstants.ZX_CMD_ENTRY_USETYPE);// 用户类型 1：学生；2：网吧；3：企业；4：家庭普通；5：WEB呼叫中心；0：其它---4
        entry.setEcpcompanyId(IConstants.ZX_CMD_ENTRY_ECPCOMPANYID);// ECP用户ID，由营业厅系统维护，ECP业务平台需要保存为企业的一个属性。
        entry.setUsername(phoneNbr);// 用户名，作昵称---业务号码
        entry.setLogin(phoneNbr);// 登录名-----业务号码
        entry.setPassword(Utils.getRandomID(8));// 登录密码----随机生成 8位字母与数字组合
        entry.setVersionType(IConstants.ZX_CMD_ENTRY_VERSIONTYPE);// 　版本类型0浙江版1网络版，2自助版、3定制版，4 EMA版 --1
        entry.setLanguage(IConstants.ZX_CMD_ENTRY_LANGUAGE);// 语言1: 中文 2: 英文----1
        entry.setState(IConstants.ZX_CMD_ENTRY_STATE);// SoftDA用户状态1：正常使用 2：禁止使用4：用户暂停 5：停机报号6：未使用7：待验证 ---1
        entry.setBindAuthorization(IConstants.ZX_CMD_ENTRY_BIND_AUTHORIZATION);// 绑定号码权限1: 网内, 2:市话权限3: 国内长途权限, 4:
                                                                               // 国际长途权限 , 默认权限: 3 ---3
        entry.setCallAuthorization(IConstants.ZX_CMD_ENTRY_CALL_AUTHORIZATION);// 呼叫限制权限 1: 网内, 2: 市话权限, 3: 国内长途权限, 4:
                                                                               // 国际长途权限 , 默认权限: 3---3
        entry.setSoftphoneType(IConstants.ZX_CMD_ENTRY_SOFTPHONETYPE);// 终端类型0: 无权使用软终端电话, 1: 有权使用软号码池
                                                                      // 2：有权使用固定软终端号码，默认：1---1
        entry.setChargecardType(IConstants.ZX_CMD_ENTRY_CHARGECARDTYPE);// 记帐卡类型0: 后付费 1: 预付费---0

        entry.setCardlocalNum(AreaCodeUtil.getAreaCode4PhoneNumber(areaCodehttpUrl, phoneNbr));
//        entry.setCardlocalNum("0571");
        logger.debug(entry.getCardlocalNum());
        
        entry.setFeeControl(IConstants.ZX_CMD_ENTRY_FEECONTROL);
        entry.setBusinessType(IConstants.ZX_CMD_ENTRY_BUSINESSTYPE);

        req.setEntry(entry);
        String jsonStr = JSON.toJSONString(req);
        
        String resp ="";
		try {
			resp = HttpClientPool.getInstance().postMethod(zxAccountHttpUrl, jsonStr);
		}catch (Exception e) {
			resp = e.getMessage();
		}
        return resp;*/
		return "";//注释掉上面所有
	}
}
