package com.cleardark.logcollection.collector;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPReceiver {
	private static Logger logger = LoggerFactory.getLogger(UDPReceiver.class);
	
	private static final int LISTENING_PORT = 9999;
	private static final int BUFFER_SIZE = 512;

	public static void main(String[] args) throws Exception {
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket msg = new DatagramPacket(buffer, buffer.length);

		try (DatagramSocket socket = new DatagramSocket(LISTENING_PORT)) {
			System.out.println("接收端已经启动...\n");
			while (true) {
				socket.receive(msg); // 接收数据包
				String msgBody = new String(msg.getData(), msg.getOffset(), msg.getLength());
				if (msgBody.isEmpty()) { // 发现接收的消息是空字符串("")便跳出循环
					break;
				}
				int senderPort = msg.getPort();
				InetAddress senderAddr = msg.getAddress();

				//System.out.printf("发送端 地址和端口 -> (%s:%d)\n", senderAddr.getHostAddress(), senderPort);

				//System.out.println("发送端 发送的消息 -> " + msgBody + "\n");
			}
		}
		System.out.println("接收端已经关闭。");
	}
}
