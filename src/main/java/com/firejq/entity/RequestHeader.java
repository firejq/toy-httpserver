package com.firejq.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class RequestHeader extends Header {

	private String reqMethod;

	private String reqUri;

	public RequestHeader() {
		super();
	}

	public RequestHeader(String httpVersion,
						 String reqMethod,
						 String reqUri) {
		super(httpVersion);
		this.reqMethod = reqMethod;
		this.reqUri = reqUri;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getReqUri() {
		return reqUri;
	}

	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}
}
