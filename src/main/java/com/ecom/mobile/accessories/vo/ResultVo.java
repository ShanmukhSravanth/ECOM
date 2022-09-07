package com.ecom.mobile.accessories.vo;

public class ResultVo {

	private boolean status = false;

	private String msg = "";

	private Object obj = null;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean success) {
		this.status = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
