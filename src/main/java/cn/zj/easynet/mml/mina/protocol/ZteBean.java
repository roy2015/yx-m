package cn.zj.easynet.mml.mina.protocol;

import cn.zj.easynet.mml.mina.protocol.Answer;

public class ZteBean {
	
	String seq;
	String jsonOpinfo;
	String zteOpinfo;
	Answer answer;
	
	public ZteBean(String seq,String zteOpinfo){
		setSeq(seq);
		setZteOpinfo(zteOpinfo);
	}
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getZteOpinfo() {
		return zteOpinfo;
	}
	public void setZteOpinfo(String zteOpinfo) {
		this.zteOpinfo = zteOpinfo;
	}
	public String getJsonOpinfo() {
		return jsonOpinfo;
	}

	public void setJsonOpinfo(String jsonOpinfo) {
		this.jsonOpinfo = jsonOpinfo;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}	

}