package com.firejq.util;

import com.firejq.config.Config;
import com.firejq.constant.HttpStatusEnum;
import com.firejq.entity.RequestHeader;
import com.firejq.entity.ResponseHeader;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class ResponseUtil {

	public static void responseHandler(SelectionKey sKey) {

		RequestHeader reqHeader = (RequestHeader) sKey.attachment();
		SocketChannel sChannel = (SocketChannel) sKey.channel();
		try {
			// 请求出错，返回 400
			if (reqHeader == null) {
				respBadRequest(sChannel);
				return;
			}

			String uri = reqHeader.getReqUri();
			uri = uri.endsWith("/") ? Config.DEFAULT_INDEX_PAGE :
				  uri.substring(1);

			// 文件不存在，返回 404
			if (!(new File(uri).exists())) {
				respNotFound(sChannel);
				return;
			}
			try {
				respOK(sChannel, uri);
			} catch (Exception e) {
				e.printStackTrace();
				respServerError(sChannel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 200
	private static void respOK(SocketChannel sChannel, String uri)
			throws Exception {
		try (RandomAccessFile raFile = new RandomAccessFile(uri, "r");
			 FileChannel fChannel = raFile.getChannel()) {
			ByteBuffer bodyBuffer = ByteBuffer
					.allocate((int) fChannel.size());
			fChannel.read(bodyBuffer);
			bodyBuffer.flip();

			ResponseHeader respHeader = new ResponseHeader(
					HttpStatusEnum.OK.getCode(),
					HttpStatusEnum.OK.getMsg());
			//respHeader.getHeaderField().put("Content-Type", );
			respHeader.getHeaderField()
					  .put("Content-Length", bodyBuffer.capacity() + "");
			ByteBuffer headerBuffer = ByteBuffer.wrap(respHeader.toString()
																.getBytes());
			sChannel.write(new ByteBuffer[]{headerBuffer, bodyBuffer});

			log(sChannel.getRemoteAddress().toString(), respHeader);

		}
	}

	// 400
	private static void respBadRequest(SocketChannel sChannel)
			throws Exception {
		respErrorHanlder(sChannel, HttpStatusEnum.BAD_REQUEST);
	}

	// 403
	private static void respForbiddenRequest(SocketChannel sChannel)
			throws Exception {
		respErrorHanlder(sChannel, HttpStatusEnum.FORBIDDEN);

	}

	// 404
	private static void respNotFound(SocketChannel sChannel)
			throws Exception {
		respErrorHanlder(sChannel, HttpStatusEnum.NOT_FOUND);
	}

	// 500
	private static void respServerError(SocketChannel sChannel)
			throws Exception {
		respErrorHanlder(sChannel, HttpStatusEnum.INTERNAL_SERVER_ERROR);
	}

	private static void respErrorHanlder(SocketChannel sChannel,
										 HttpStatusEnum staus)
			throws Exception {

		ResponseHeader respHeader = new ResponseHeader(staus.getCode(),
													   staus.getMsg());
		ByteBuffer headerBuffer = ByteBuffer.wrap(respHeader.toString()
															.getBytes()),
				bodyBuffer = ByteBuffer.wrap(staus.getMsg().getBytes());

		//		sChannel.write(new ByteBuffer[]{headerBuffer, bodyBuffer});
		//		todo 以 json 格式返回错误信息
		sChannel.write(headerBuffer);

		log(sChannel.getRemoteAddress().toString(), respHeader);
	}

	private static void log(String ip,
							ResponseHeader header) {
		System.out.println(
				"----------response---------\n" +
						String.format("%s [%s] %s %d",
									  ip,
									  Date.from(Instant.now()).toString(),
									  header.getHttpVersion(),
									  header.getStatus()) +
						"\n---------------------------\n");
	}
}
