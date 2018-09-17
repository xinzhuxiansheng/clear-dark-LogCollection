package com.cleardark.logcollection.agent.appender;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "UDPAppender", category = "Core", elementType = "appender", printObject = true)
public class UDPAppender extends AbstractAppender {
	//private static Logger logger = LoggerFactory.getLogger(UDPAppender.class);

	private String host;
	private Integer port;
	private String ip;
	private String projectName;

	protected UDPAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions,
			String host, Integer port,String ip,String projectName) {
		super(name, filter, layout, ignoreExceptions);
		this.host = host;
		this.port = port;
		this.ip = ip;
		this.projectName = projectName;
	}

	@Override
	public void append(LogEvent event) {
		final byte[] bytes = getLayout().toByteArray(event);
		String msg = new String(bytes);
		msg = String.format("%s-%s-%s",projectName,ip,msg);
		DatagramPacket dp;
		try {
			dp = parseMsg(msg, host, port);
			startSending(dp);
		} catch (Exception e) {
			LOGGER.error("日志发送过程中出现异常");
			e.printStackTrace();
		}
	}

	@PluginFactory
	public static UDPAppender createAppender(@PluginAttribute("name") String name, 
			@PluginAttribute("host") String host,
			@PluginAttribute("port") Integer port,
			@PluginAttribute("ip") String ip,
			@PluginAttribute("projectname") String projectName,
			@PluginElement("Filter") final Filter filter,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
		if (StringUtils.isBlank(name)) {
			LOGGER.error("no name defined in log4j2.xml");
			return null;
		}

		if (StringUtils.isBlank(host)) {
			LOGGER.error("no host defined in log4j2.xml");
		}

		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}

		return new UDPAppender(name, filter, layout, ignoreExceptions, host, port,ip,projectName);
	}
	
	private static void startSending(DatagramPacket msg) throws IOException, InterruptedException {

		// 无参构造的 DatagramSocket 会随机选择一个端口进行监听
		// 因为此时 DatagramSocket 的作用是发送，所以无需显式指定固定端口
		try (DatagramSocket socket = new DatagramSocket()) {
			System.out.println("随机给发送端分配的端口为：" + socket.getLocalPort() + "\n");

			socket.send(msg); // 发送数据包

			int recverPort = msg.getPort();
			InetAddress recverAddr = msg.getAddress();
			System.out.printf("消息已经发送 -> (%s:%d)\n", recverAddr.getHostAddress(), recverPort);
		}
	}

	private static DatagramPacket parseMsg(String msgBody, String addr, int port) throws UnknownHostException {

		byte[] msgData = msgBody.getBytes();
		DatagramPacket msg = new DatagramPacket(msgData, 0, msgData.length, // 数据从位置 0 开始，长度为 msgData.length
				InetAddress.getByName(addr), port); // 目的地 地址为 addr，监听端口为 port
		return msg;
	}

}
