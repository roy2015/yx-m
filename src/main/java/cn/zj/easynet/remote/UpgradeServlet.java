package cn.zj.easynet.remote;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * Jul 19, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
/*-
 * 返回：
 * {"description":"描述~~","download":"http://addr/down/ecp0713.apk","force":2,"lastVer":"1.0.0","lowVer":"0.0.9","serverTime":1405738282158}
 * 
 * 注解：
 *   force: 0-根据账号不提示
 *          1-根据账号提示，客户端需要验证支持的最小版本，如果小于支持的lowVer,将提示强制升级
 *          2-无要求的全部强制升级
 */
@WebServlet(name = "upgradeServlet", loadOnStartup = 0, urlPatterns = { "/nnl/upgrade" }, asyncSupported = true)
public class UpgradeServlet extends HttpServlet {
    private static final long serialVersionUID = -4191357691315700386L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        super.init();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	response.setCharacterEncoding("utf8");
        UpgradeDto upgradeDto = new UpgradeDto();
        
        String version = !StringUtils.isBlank( request.getParameter("version") ) ? (String)( request.getParameter("version") ) : "";
        if(version.compareTo( upgradeDto.getLastVer() ) < 0){
        	upgradeDto.setForce("1");
        }else{
        	upgradeDto.setForce("0");
        }
        
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(upgradeDto));
        out.close();
    }

}
