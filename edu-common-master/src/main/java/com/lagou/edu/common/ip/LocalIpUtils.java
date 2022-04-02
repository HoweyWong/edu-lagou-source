/**  
 *@Description:     
 */
package com.lagou.edu.common.ip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class LocalIpUtils {
	
	private static Logger logger = Logger.getLogger(LocalIpUtils.class);
	
	/** 获取本机内网地址 **/
	public static String getLocalIpAddr() {
		InetAddress ip = null;
		if (isWindowsOS()) {
			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			if (ip != null) {
				logger.debug("windows本机的IP = " + ip.getHostAddress());
				return ip.getHostAddress();
			}
		}
		String ipAddress = getLocalIp(true);
		if(isBlank(ipAddress)){//如果eth0网卡为空
			ipAddress = getLocalIp(false);
		}
		return ipAddress;
	}

	/**
	 * 获取本地地址
	 * @param justEth0 true - 只看eth0  false  所有
	 * @return
	 * @Date:2014-4-24  
	 * @Author:Guibin Zhang  
	 * @Description:
	 */
	private static String getLocalIp(boolean justEth0) {
		InetAddress ip = null;
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			if(!justEth0 || "eth0".equalsIgnoreCase(netInterface.getName())){
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip.getHostAddress() != null && ip instanceof Inet4Address && ip.getHostAddress().indexOf(".") != -1
							&& !ip.getHostAddress().startsWith("192.168.") && !"127.0.0.1".equals(ip.getHostAddress()) && !"localhost".equals(ip.getHostAddress())) {
						logger.debug("linux或mac本机的IP = " + ip.getHostAddress());
						return ip.getHostAddress();
					}
				}
			}
		}
		return null;
	}

//	public static void main(String[] args) {
//		// getIps();
//		//getLocalIpAddr();
//		System.out.println("isWindows? " + isWindowsOS());
//	}

	/** 判断系统是不是windows **/
	public static boolean isWindowsOS() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			return true;
		}
		return false;
	}
	
	/** 判断系统是不是mac **/
	public static boolean isMacOSX() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("mac") > -1) {
			return true;
		}
		return false;
	}
	
	/** 判断系统是不是linux **/
	public static boolean isLinux() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("linux") > -1) {
			return true;
		}
		return false;
	}
	
	private static boolean isBlank(String str){
		return (str == null) || (str.trim().length() == 0);
	}
}
