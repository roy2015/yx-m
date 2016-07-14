package cn.zj.easynet.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class AccountCallBackServlet
 */
@WebServlet(name = "accountCallBackServlet", loadOnStartup = 5,  urlPatterns = { "/nnl/accountCallBack" })
public class AccountCallBackServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(AccountCallBackServlet.class);
	private static final long serialVersionUID = 1L;
	public static ConcurrentHashMap<String, HttpServletResponse> sessionMap = new ConcurrentHashMap<String, HttpServletResponse>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountCallBackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("进入AccountCallBackServlet=============================================");
		byte[] b = new byte[1024];
		request.getInputStream().read(b);
		String s = new String(b);
		logger.debug(s);
		
		try {
			logger.debug(new org.json.JSONObject(s).getString("id"));
			ServletResponse callbackResp = sessionMap.get( new org.json.JSONObject(s).getString("id") );
			logger.debug(s);
			if(callbackResp !=null){
				logger.debug("------------------------callbackResp is not null");
				callbackResp.setCharacterEncoding("utf8");
			}else{
				logger.debug("------------------------callbackResp is null");
			}
			logger.debug("out begin");
			PrintWriter out = callbackResp.getWriter();
			logger.debug("out end");
			out.write(s);
			logger.debug("out write");
			out.flush();
			logger.debug("out flush");
			out.close();
			logger.debug("out close");
			
			synchronized (callbackResp) {
				callbackResp.notifyAll();
			}
			
			logger.debug("notifyAll");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
