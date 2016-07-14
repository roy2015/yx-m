/*
 * cn.zj.easynet.yx.it.remote.request.PrepayRequestBase.java
 * Jul 8, 2014 
 */
package cn.zj.easynet.remote.request;


/**
 * Jul 8, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class CmdRequestBase{
    private String id;// 请求唯一标示,String, 必填
    private String cmd;// 请求信令 Int, 必填

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * @param cmd
     *            the cmd to set
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
