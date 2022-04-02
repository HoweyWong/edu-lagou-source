package com.lagou.edu.message.server;

import com.lagou.edu.message.server.check.HttpCheckServer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月1日 下午8:28:27
 */
@Slf4j
public class Bootstrap {
	static {
		PushServer.pushServer.start(); // 启动失败, 系统会自动关闭
		final HttpCheckServer checkServer = new HttpCheckServer(11221);
		checkServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					PushServer.pushServer.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					checkServer.stop();
				} catch (Exception e) {
					log.error("checkServer.stop() - e",e);
				}
			}
		}));
	}
}