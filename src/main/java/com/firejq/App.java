package com.firejq;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author <a href="mailto:firejq@outlook.com">firejq</a>
 */
public class App {
	public static void main(String[] args) {
		if (args.length == 1) {
			new HttpServer(Integer.parseInt(args[0])).run();
			return;
		}
		if (args.length >= 2) {
			new HttpServer(args[0], Integer.parseInt(args[0])).run();
			return;
		}
		new HttpServer().run();
	}
}
