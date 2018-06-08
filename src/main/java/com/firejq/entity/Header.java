package com.firejq.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public abstract class Header {

	public Header() {
	}

	public Header(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	// HTTP 版本
	private String httpVersion = "HTTP/1.1";

	// 首部字段
	private Map<String, String> headerField = new HashMap<>();

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public Map<String, String> getHeaderField() {
		return headerField;
	}

	public void setHeaderField(Map<String, String> headerField) {
		this.headerField = headerField;
	}
}
