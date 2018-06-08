package com.firejq;

import com.firejq.config.Config;
import com.firejq.util.RequestUtil;
import com.firejq.util.ResponseUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class HttpServer {

	private String ipAddress;

	private int port;

	public HttpServer() {
		this(Config.DEFAULT_IPADDRESS, Config.DEFAULT_PORT);
	}

	public HttpServer(int port) {
		this(Config.DEFAULT_IPADDRESS, port);
	}

	public HttpServer(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public void run() {
		// 创建 ServerSocketChannel 和 Selector
		try (ServerSocketChannel ssChannel = ServerSocketChannel.open();
			 Selector selector = Selector.open()) {
			ssChannel.bind(new InetSocketAddress(this.ipAddress, this.port));
			// 设置为非阻塞
			ssChannel.configureBlocking(false);
			// 注册 ACCEPT 事件
			ssChannel.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println(
					"httpserver start, serving ./\n" +
							"Available on:\n" +
							this.ipAddress + ":" + this.port + "\n" +
							"Hit CTRL-C to stop the server\n");

			while (true) {
				if (selector.select() <= 0)
					continue;
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {

					SelectionKey sKey = it.next();
					it.remove();

					if (sKey.isValid() && sKey.isAcceptable()) {
						SocketChannel sChannel = ssChannel.accept();
						sChannel.configureBlocking(false);
						sChannel.register(selector, SelectionKey.OP_READ);

					} else if (sKey.isValid() && sKey.isReadable()) {
						RequestUtil.requestHandler(sKey);
						sKey.interestOps(SelectionKey.OP_WRITE);

					} else if (sKey.isValid() && sKey.isWritable()) {
						ResponseUtil.responseHandler(sKey);
						sKey.interestOps(SelectionKey.OP_READ);

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
