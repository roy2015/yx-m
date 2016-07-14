package cn.zj.easynet.mml.mina.protocol;

import java.io.Serializable;

public class ZteRequest implements Serializable {
    private String id;  // 8�ֽ� Hex
    private String req;

    public ZteRequest(String id, String req) {
    	if( id.length()!=8 )
            throw new RuntimeException("ID ��ʽ����");
        this.id = id;
        this.req = req;
    }

    public String getId() {
        return id;
    }

    public String getReq() {
        return req;
    }
}