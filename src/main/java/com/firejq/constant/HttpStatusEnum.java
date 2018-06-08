package com.firejq.constant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public enum HttpStatusEnum {

	OK(200, "OK"),

	BAD_REQUEST(400, "Bad Request"),

	FORBIDDEN(403, "Forbidden"),

	NOT_FOUND(404, "Not Found"),

	INTERNAL_SERVER_ERROR(500, "Internal Server Error");

	private int code;

	private String msg;

	HttpStatusEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
