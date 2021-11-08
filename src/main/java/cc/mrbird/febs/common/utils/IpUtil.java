package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class IpUtil {

	private static final String UNKNOWN = "unknown";

	/**
	 * 获取 IP地址
	 * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()获取 IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
	 * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * 判断ip是否在范围内
	 * @param ip
	 * @param cidr
	 * @return
	 */
	public static boolean isInRange(String ip, String cidr) {
		String[] ips = ip.split("\\.");
		int ipAddr = (Integer.parseInt(ips[0]) << 24)
				| (Integer.parseInt(ips[1]) << 16)
				| (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
		int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
		int mask = 0xFFFFFFFF << (32 - type);
		String cidrIp = cidr.replaceAll("/.*", "");
		String[] cidrIps = cidrIp.split("\\.");
		int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
				| (Integer.parseInt(cidrIps[1]) << 16)
				| (Integer.parseInt(cidrIps[2]) << 8)
				| Integer.parseInt(cidrIps[3]);
		return (ipAddr & mask) == (cidrIpAddr & mask);
	}

	/**
	 * 判断ip是否是俄罗斯ip
	 * @param ip
	 * @param russiaIpList
	 * @return
	 */
	public static boolean checkIsRussiaIp(String ip, List<String> russiaIpList){
		for (int i = 0; i < russiaIpList.size(); i++) {
			if (isInRange(ip, russiaIpList.get(i)+"/32")){
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		/**
		 * 10.235.1.160/255.255.255.252
		 * 10.193.86.137-10.193.86.138
		 * 10.235.1.161/32
		 * 91.215.36.24
		 */
		List<String> russiaIpList = Arrays.asList("91.215.36.24","10.235.1.160","10.235.1.161","10.193.86.137","10.193.86.138");
		List<String> checkIpList = Arrays.asList("91.215.36.24","10.235.1.160","10.235.1.161","10.193.86.137","10.193.86.138","10.235.1.165");
		checkIpList.stream().forEach(checkedIp -> {
			log.info(checkedIp + "是否是俄罗斯ip：" + checkIsRussiaIp(checkedIp, russiaIpList));
		});
	}

}
