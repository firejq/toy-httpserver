package com.firejq.util;

import com.firejq.config.Config;
import com.firejq.constant.HttpConstant;
import com.firejq.entity.RequestHeader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class RequestUtil {

	public static void requestHandler(SelectionKey sKey) {
		try (SocketChannel sChannel = (SocketChannel) sKey.channel()) {

			ByteBuffer reqBuffer
					= ByteBuffer.allocate(Config.DEFAULT_BUFFER_SIZE);
			sChannel.read(reqBuffer);
			reqBuffer.flip();
			byte [] reqBytes = new byte[reqBuffer.limit()];
			reqBuffer.get(reqBytes);

			// 将解析后的 requestHeader 放入 selectionKey 中
			sKey.attach(parseReqHeader(new String(reqBytes)));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static RequestHeader parseReqHeader(String reqHeaderStr) {
		if (reqHeaderStr.isEmpty()) {
			// throw new InvalidHeaderException();
		}

		// 解析 HTTP 报文首部
		RequestHeader reqHeader = new RequestHeader();
		// 解析 HTTP 报文首部的请求行
		int index = reqHeaderStr.indexOf(HttpConstant.LINE_SEPARATOR);
		String [] fields = reqHeaderStr.substring(0, index)
									   .split(" ");
		if (fields.length != 3) {
			// throw new InvalidHeaderException();
		}
		reqHeader.setReqMethod(fields[0]);
		reqHeader.setReqUri(fields[1]);
		reqHeader.setHttpVersion(fields[2]);

		// 解析 HTTP 报文首部的首部字段
		fields = reqHeaderStr.split(HttpConstant.LINE_SEPARATOR);
		for (String part : fields) {
			index = part.indexOf(HttpConstant.KEY_VALUE_SEPARATOR);
			if (index == -1) {
				continue;
			}
			String key = part.substring(0, index);
			if (index == -1 || index + 1 >= part.length()) {
				reqHeader.getHeaderField().put(key, "");
				continue;
			}
			String value = part.substring(index + 1);
			reqHeader.getHeaderField().put(key, value);
		}

		return reqHeader;
	}

}
