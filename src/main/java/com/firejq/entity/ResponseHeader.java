package com.firejq.entity;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class ResponseHeader extends Header {

	private String reqMethod;

	private int status;

	private String msg;

	public ResponseHeader() {
		super();
	}

	public ResponseHeader(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public ResponseHeader(String httpVersion, String reqMethod, int status,
						  String msg) {
		super(httpVersion);
		this.reqMethod = reqMethod;
		this.status = status;
		this.msg = msg;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s %d %s\r\n",
								super.getHttpVersion(), status, msg));
		//sb.append(String.format("ContentType: %s\r\n", contentType));
		sb.append(String.format("ContentLength: %d\r\n",
								super.getHeaderField()
									 .getOrDefault("ContentLength", "0")));
		sb.append(String.format("Server: %s\r\n",
								super.getHeaderField()
									 .getOrDefault("Server", "")));
		sb.append("\r\n");
		return sb.toString();
	}
}
