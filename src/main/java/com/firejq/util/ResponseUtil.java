package com.firejq.util;

import com.firejq.config.Config;
import com.firejq.constant.HttpStatus;
import com.firejq.entity.RequestHeader;
import com.firejq.entity.ResponseHeader;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class ResponseUtil {

	public static void responseHandler(SelectionKey sKey) {

		RequestHeader reqHeader = (RequestHeader) sKey.attachment();
		try (SocketChannel sChannel = (SocketChannel) sKey.channel()) {
			if (reqHeader == null) {
				respBadRequest(sChannel);
				return;
			}

			String uri = reqHeader.getReqUri();
			uri = uri.endsWith("/") ? uri + Config.DEFAULT_INDEX_PAGE : uri;

			File file = new File(uri);
			if (!file.exists()) {
				respNotFound(sChannel);
				return;
			}

			try (RandomAccessFile raFile = new RandomAccessFile(uri,
																"r");
				 FileChannel fChannel = raFile.getChannel()) {
				ByteBuffer fileBuffer
						= ByteBuffer.allocate((int) fChannel.size());
				fChannel.read(fileBuffer);
				fileBuffer.flip();

				respOK(sChannel, fileBuffer);

			} catch (Exception e) {
				respServerError(sChannel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 200
	private static void respOK(SocketChannel sChannel, ByteBuffer bodyBuffer)
			throws Exception {
		ResponseHeader respHeader = new ResponseHeader(HttpStatus.OK_STATUS,
													  HttpStatus.OK_MESSAGE);
		//respHeader.getHeaderField().put("Content-Type", );
		respHeader.getHeaderField().put("Content-Length",
										bodyBuffer.capacity() + "");
		respHeader.getHeaderField().put("Server",
										Config.DEFAULT_SERVER_NAME);
		ByteBuffer headerBuffer = ByteBuffer.wrap(respHeader.toString()
															.getBytes());
		sChannel.write(new ByteBuffer[]{headerBuffer, bodyBuffer});
	}

	// 400
	private static void respBadRequest(SocketChannel sChannel)
			throws Exception {
		// todo
	}

	// 403
	private static void respForbiddenRequest(SocketChannel sChannel)
			throws Exception {

	}

	// 404
	private static void respNotFound(SocketChannel sChannel)
			throws Exception {

	}

	// 500
	private static void respServerError(SocketChannel sChannel)
			throws Exception {

	}
}
