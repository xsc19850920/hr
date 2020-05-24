package com.rs.hr.common;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by woodle on 15/1/19.
 */
public class IPUtils {
	private static Logger logger = LoggerFactory.getLogger(IPUtils.class);
	private static Pattern ipPattern = Pattern.compile("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b");

	/**
	 * 获取客户端ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddressByRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		} else {
			String[] ips = ip.split(",");
			for (int i = 0; i < ips.length; i++) {
				ip = ips[i];
				if (!(ip.startsWith("10.") || ip.startsWith("192.168") || ip.startsWith("172.16.") || ip.startsWith("19.2.168") || ip.equalsIgnoreCase("unknown"))) {
					return ip.trim();
				} else {
					ip = null;
				}
			}
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		} else {
			return ip.trim();
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "";
	}
	
	
	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	logger.error("IPUtils ERROR ", e);
        }

        //使用代理，则获取第一个IP地址
        if (StrUtil.isEmpty(ip) && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        
        return ip;
    }

	/**
	 * 将ip转化为int
	 * 
	 * @param ip
	 * @return
	 */
	public static long convertIpToInt(String ip) {
		List<String> ipList = StrUtil.split(ip, '.');
		String[] ipArray = ipList.toArray(new String[ipList.size()]);
		long ipInt = 0;

		try {
			for (int i = 0; i < ipArray.length; i++) {
				if (ipArray[i] == null || ipArray[i].trim().equals("")) {
					ipArray[i] = "0";
				}
				if (new Integer(ipArray[i].toString()).intValue() < 0) {
					Double j = new Double(Math.abs(new Integer(ipArray[i].toString()).intValue()));
					ipArray[i] = j.toString();
				}
				if (new Integer(ipArray[i].toString()).intValue() > 255) {
					ipArray[i] = "255";
				}
			}
			ipInt = new Double(ipArray[0]).longValue() * 256 * 256 * 256 + new Double(ipArray[1]).longValue() * 256 * 256 + new Double(ipArray[2]).longValue() * 256 + new Double(ipArray[3]).longValue();
		} catch (Exception e) {
			// do nothing
		}
		return ipInt;
	}

	/**
	 * 将字符串型ip转成int型ip
	 * 
	 * @param strIp
	 * @return
	 */
	public static int Ip2Int(String strIp) {
//		String[] ss = strIp.split("\\.");
//		if (ss.length != 4) {
//			return 0;
//		}
//		byte[] bytes = new byte[ss.length];
//		for (int i = 0; i < bytes.length; i++) {
//			bytes[i] = (byte) Integer.parseInt(ss[i]);
//		}
//		return byte2Int(bytes);
		return 0;
	}

	/**
	 * 将int型ip转成String型ip
	 * 
	 * @param intIp
	 * @return
	 */
	public static String int2Ip(int intIp) {
		byte[] bytes = int2byte(intIp);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(bytes[i] & 0xFF);
			if (i < 3) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	private static byte[] int2byte(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		bytes[3] = (byte) ((0xff000000 & i) >> 24);
		return bytes;
	}

	private static int byte2Int(byte[] bytes) {
		int n = bytes[0] & 0xFF;
		n |= ((bytes[1] << 8) & 0xFF00);
		n |= ((bytes[2] << 16) & 0xFF0000);
		n |= ((bytes[3] << 24) & 0xFF000000);
		return n;
	}

	/**
	 * 获取本地ip
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// do not nothing
		}
		return null;
	}

	/**
	 * 是否是一个ip
	 * 
	 * @param address
	 * @return
	 */
	public static boolean isIP(String address) {
		return ipPattern.matcher(address).matches();
	}

	public static void main(String[] args) {
//		String ip1 = "192.168.1.1";
//		int intIp = Ip2Int(ip1);
//		String ip2 = int2Ip(intIp);
//		System.out.println(ip2.equals(ip1));
	}
}
