package cn.zj.easynet.device.http;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zj.easynet.common.marshal.Now;
import cn.zj.easynet.common.util.StringUtil;
import cn.zj.easynet.device.DeviceEnv;
import cn.zj.easynet.device.http.request.ResponseResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClientPool {
    private static Logger logger = LoggerFactory
        .getLogger(HttpClientPool.class);

    private static class HttpClientPoolHolder {
        static final HttpClientPool INSTANCE = new HttpClientPool();
    }

    private CloseableHttpClient client;

    MessageConstraints messageConstraints = MessageConstraints.custom()
        .setMaxHeaderCount(200).setMaxLineLength(2000).build();

    // connection config
    ConnectionConfig connectionConfig = ConnectionConfig.custom()
        .setMalformedInputAction(CodingErrorAction.IGNORE)
        .setUnmappableInputAction(CodingErrorAction.IGNORE)
        .setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints)
        .build();

    // request config
    RequestConfig defaultRequestConfig = RequestConfig.custom()
        .setSocketTimeout(DeviceEnv.httpTimeoutThreshold)
        .setConnectTimeout(DeviceEnv.httpTimeoutThreshold)
        .setConnectionRequestTimeout(DeviceEnv.httpTimeoutThreshold).build();

    private HttpClientPool() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(50);
        cm.setMaxTotal(200);

        cm.setDefaultConnectionConfig(connectionConfig);

        client = HttpClients.custom().setConnectionManager(cm)
            .setDefaultRequestConfig(defaultRequestConfig).build();

    }

    static public HttpClientPool getInstance() {
        return HttpClientPoolHolder.INSTANCE;
    }

    public StatusLine getMethod(String url) throws ClientProtocolException,
        IOException {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            StatusLine sl = response.getStatusLine();
            EntityUtils.consume(response.getEntity());
            return sl;
        } finally {
            get.releaseConnection();
        }

    }

    public String getMethod(String url, int timeout)
        throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(url);
        try {
            RequestConfig requestConfig = RequestConfig
                .copy(defaultRequestConfig).setSocketTimeout(timeout).build();
            get.setConfig(requestConfig);
            HttpResponse response = client.execute(get);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("req url failed, url: {} ,retcode: {}", url,
                    sl.getStatusCode());
                EntityUtils.consume(response.getEntity());
            }

        } finally {
            get.releaseConnection();
        }
        return null;
    }

    public String getMethod(String url, int timeout, int retryTimes) {
        String ret = null;
        for (int i = 0; i < retryTimes; i++) {
            try {
                ret = getMethod(url, timeout);
                if (ret == null)
                    continue;
            } catch (Exception e) {
                logger.warn("getMethod fail, left retry times: "
                    + (retryTimes - i - 1), e);
            }

            if (ret != null)
                break;
        }
        return ret;
    }

    /**
     * 邮箱插件相关post请求
     * 
     * @param url
     *            要请求的URL
     * @param params
     *            对应的请求参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String postMail(String url, Map<String, String> params)
        throws ClientProtocolException, IOException {
        return postMail(url, params.entrySet(), null);
    }

    /**
     * 邮箱插件相关post请求
     * 
     * @param url
     *            要请求的URL
     * @param params
     *            对应的请求参数
     * @param timeout
     *            请求超时时间
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String postMail(String url, Map<String, String> params, int timeout)
        throws ClientProtocolException, IOException {
        return postMail(url, params.entrySet(), timeout);
    }

    /**
     * 邮箱插件相关post请求
     * 
     * @param url
     *            要请求的URL
     * @param params
     *            对应的请求参数
     * @param timeout
     *            请求超时时间
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String postMail(String url, Set<Map.Entry<String, String>> params,
        Integer timeout) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry: params) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
            }
            // 超时判断
            if (timeout == null) {
                timeout = DeviceEnv.httpTimeoutThreshold;
            }

            RequestConfig requestConfig = RequestConfig
                .copy(defaultRequestConfig).setSocketTimeout(timeout).build();
            post.setConfig(requestConfig);
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = client.execute(post);

            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("req url failed, url: {} ,retcode: {}", url,
                    sl.getStatusCode());
                EntityUtils.consume(response.getEntity());
            }

            return null;
        } finally {
            post.releaseConnection();
        }
    }


    /**
     * 反回参数解析
     * 
     * @param response
     * @return
     */
    public ResponseResult parseResponseStr(String response) {
        if (StringUtil.isEmpty(response)) {
            return new ResponseResult(500, "resonse wrong!");
        }

        String[] strs = response.split("\n");
        if (strs.length >= 2) {
            return new ResponseResult(Integer.parseInt(strs[0]), strs[1]);
        } else {
            return new ResponseResult(500, response);
        }
    }

    /**
     * cookies中解析出的
     * 
     * @param headers
     * @return
     */
    private String getMainAccount(Header[] headers) {
        for (Header h: headers) {
            String value = h.getValue();
            if (value.contains("P_INFO")) {

                int index = value.indexOf("=");
                int len = value.indexOf("|");
                if (len > index) {
                    return value.substring(index + 1, len);
                }
            }
        }
        return null;
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String postMethod(String url, Map<String, Object> params)
        throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue().toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("req url failed, url: {} ,retcode: {}", url,
                    sl.getStatusCode());
                EntityUtils.consume(response.getEntity());
            }

            return null;
        } finally {
            post.releaseConnection();
        }
    }
    
    /**
     * @param url
     * @param params
     * @param timeout
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String postMethod(String url, Map<String, Object> params, int timeout)
        throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue().toString()));
            }
            RequestConfig requestConfig = RequestConfig
                .copy(defaultRequestConfig).setSocketTimeout(timeout).build();
            post.setConfig(requestConfig);

            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("req url failed, url: {} ,retcode: {}", url,
                    sl.getStatusCode());

                EntityUtils.consume(response.getEntity());

            }
            return null;
        } finally {
            post.releaseConnection();
        }
    }


    public String postMethod(String url, String entity)
        throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        try {
            HttpEntity str = new StringEntity(entity, "UTF-8");
            post.setEntity(str);

            HttpResponse response = client.execute(post);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity respEntity = response.getEntity();
                return EntityUtils.toString(respEntity, "utf-8");
            } else {
                logger.error("req url failed, url: {} ,retcode: {}", url,
                    sl.getStatusCode());

                EntityUtils.consume(response.getEntity());

            }

            return null;
        } finally {
            post.releaseConnection();
        }
    }

    public String execute(HttpRequestBase post) throws ClientProtocolException,
        IOException {
        try {

            HttpResponse response = client.execute(post);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                logger.info("Http execute success");
                HttpEntity respEntity = response.getEntity();
                return EntityUtils.toString(respEntity, "utf-8");
            } else {
                logger.error("req url failed, url: " + post.getURI().toString()
                    + ",retcode: " + sl.getStatusCode());
                EntityUtils.consume(response.getEntity());
            }

            return null;
        } finally {
            post.releaseConnection();
        }
    }

    public String execute(HttpRequestBase post, int timeout)
        throws ClientProtocolException, IOException {
        try {
            RequestConfig requestConfig = RequestConfig
                .copy(defaultRequestConfig).setSocketTimeout(timeout).build();
            post.setConfig(requestConfig);
            HttpResponse response = client.execute(post);
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HttpStatus.SC_OK) {
                logger.info("Http execute success");
                HttpEntity respEntity = response.getEntity();
                return EntityUtils.toString(respEntity, "utf-8");
            } else {
                logger.error("req url failed, url: " + post.getURI().toString()
                    + ",retcode: " + sl.getStatusCode());
                EntityUtils.consume(response.getEntity());
            }
            return null;
        } finally {
            post.releaseConnection();
        }
    }


    public static void main(final String[] args)
        throws ClientProtocolException, IOException {
        final String[] uris = { "http://hc.apache.org/",
            "http://hc.apache.org/httpcomponents-core-ga/",
            "http://hc.apache.org/httpcomponents-client-ga/",
            "http://svn.apache.org/viewvc/httpcomponents/",
            "http://www.baidu.com", "http://www.google.com",
            "http://www.youdao.com", "http://www.soso.com",
            "http://www.sogou.com", "http://www.badurltest.com",
            "http://192.168.164.95", };
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("1", "2");
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            es.execute(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        String url = uris[(int) (Thread.currentThread().getId() % uris.length)];
                        logger.debug("begin post {}", url);
                        String ret = HttpClientPool.getInstance().postMethod(
                            url, params, 1000);
                        System.out.println(ret);
                    } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                        logger.debug(e.getMessage(), e);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        logger.debug(e.getMessage(), e);
                    }
                }
            });
        }

    }
}
