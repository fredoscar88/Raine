package com.visellico.raine.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.visellico.rainecloud.serialization.RCDatabase;

public class Client {
	
	public enum Error {
		NONE, INVALID_HOST, NO_RECEIVER, SOCKET_EXCEPTION
	}
	
	private String ipAddress;
	private int port;
	private Error errorCode = Error.NONE;
	
	private InetAddress serverAddress;
	
	private DatagramSocket socket;
	
	/**
	 * 
	 * @param host
	 * 			eg. 192.168.1.1:5000
	 */
	public Client(String host) {
		String[] parts = host.split(":");
		//verify
		if (parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		ipAddress = parts[0];
		try {
			port = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
		
	}
	
	/**
	 * @param host
	 * 		eg 192.168.1.1
	 * @param port
	 * 		eg 5000
	 */
	public Client (String host, int port) {
		
		this.ipAddress = host;
		this.port = port;
	}
	
	public boolean connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}
		
		try {
			socket = new DatagramSocket();	//random port, we don't care
		} catch (SocketException e) {
			e.printStackTrace();
			errorCode = Error.SOCKET_EXCEPTION;
			return false;
		}
		
		sendConnectionPacket();
		// ~ Wait for server to repl ~
		return true;
	}
	
	private void sendConnectionPacket() {
//		byte[] data = "CnxnPacket".getBytes();
		byte[] data = "ConnectionPacket".getBytes();
		send(data);
	}
	
	public void send(byte[] data) {
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);	//put our mail in the mail box to send
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void send(RCDatabase database) {
		byte data[] = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);		
	}
	
	public Error getErrorCode() {
		return errorCode;
	}
	
}
