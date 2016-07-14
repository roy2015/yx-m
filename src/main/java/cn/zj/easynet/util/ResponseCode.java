package cn.zj.easynet.util;


public enum ResponseCode {
		ResponseCode_common_E505("E505","timeout超时"),

		ResponseCode_sms_0("0","命令执行成功"),
		ResponseCode_sms_100("100","接口参数错误"),
		ResponseCode_sms_101("101","帐号不存在或密码错误"),
		ResponseCode_sms_102("102","帐号已被禁用"),
		ResponseCode_sms_103("103","账户余额不足"),
		ResponseCode_sms_104("104","IP鉴权失败"),
		ResponseCode_sms_105("105","达上限"),
		ResponseCode_sms_106("106","内容不合法"),
		ResponseCode_sms_107("107","电话号码不合法"),
		ResponseCode_sms_108("108","不符合业务规则"),
		ResponseCode_sms_110("110","服务器异常"),
		ResponseCode_sms_111("111","服务配置错误"),
	
		ResponseCode_authCode_200("200","短信验证码已发送"),
		ResponseCode_authCode_201("201","短信验证码发送失败"),
		ResponseCode_authCode_202("202","号码不能为空"),
		ResponseCode_authCode_203("203","非电信号码"),
	
		
		ResponseCode_account_E0001("E0001","手机号不能为空"),
		ResponseCode_account_E0002("E0002","验证码不能为空"),
		ResponseCode_account_E0003("E0003","验证码错误"),
		ResponseCode_account_E0004("E0004","验证码不存在或已超时失效"),
		ResponseCode_account_E0005("E0005","开户密码不能为空"),
		ResponseCode_account_E0006("E0005","企业Id不能为空"),
		ResponseCode_account_E0007("E0005","用户类型不能为空"),
		
		ResponseCode_account_0007("0007","参数错误"),
		ResponseCode_account_0008("6000","其它原因开户失败"),
		ResponseCode_account_6002("6002","业务服务器忙"),
		ResponseCode_account_6000("6000","开户成功");
		
		
		
        private String code;
        private String message;

        private ResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public static String getName(String code) {
            for (ResponseCode c: ResponseCode.values()) {
                if (c.getCode().equals(code)) {
                    return c.message;
                }
            }
            return "unknow";
        }
    }
