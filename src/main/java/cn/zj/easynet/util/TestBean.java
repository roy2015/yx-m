package cn.zj.easynet.util;

import java.util.Map;

public class TestBean {
	private String arg0;
	private Integer arg1;
	private Double arg2;
	private int arg3;
	private long arg4;
	private double arg5;
	private Map<String, Object> arg6;
	

	public Map<String, Object> getArg6() {
		return arg6;
	}

	public void setArg6(Map<String, Object> arg6) {
		this.arg6 = arg6;
	}

	public double getArg5() {
		return arg5;
	}

	public void setArg5(double arg5) {
		this.arg5 = arg5;
	}

	public int getArg3() {
		return arg3;
	}

	public void setArg3(int arg3) {
		this.arg3 = arg3;
	}

	public long getArg4() {
		return arg4;
	}

	public void setArg4(long arg4) {
		this.arg4 = arg4;
	}

	public String getArg0() {
		return arg0;
	}

	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}

	public Integer getArg1() {
		return arg1;
	}

	public void setArg1(Integer arg1) {
		this.arg1 = arg1;
	}

	public Double getArg2() {
		return arg2;
	}

	public void setArg2(Double arg2) {
		this.arg2 = arg2;
	}

	public TestBean() {
		super();
	}

	@Override
	public String toString() {
		return "TestBean [arg0=" + arg0 + ", arg1=" + arg1 + ", arg2=" + arg2
				+ ", arg3=" + arg3 + ", arg4=" + arg4 + ", arg5=" + arg5
				+ ", arg6=" + arg6 + "]";
	}

	
}
