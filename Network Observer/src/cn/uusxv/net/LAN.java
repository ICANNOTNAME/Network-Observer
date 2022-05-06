package cn.uusxv.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class LAN {
	public static ArrayList<InetAddress> iplist = new ArrayList<InetAddress>();

	public static ArrayList<String> getIPs() {
		ArrayList<String> list = new ArrayList<String>();
		boolean flag = false;
		int count = 0;
		Runtime r = Runtime.getRuntime();
		Process p;
		try {
			p = r.exec("arp -a");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String inline;
			while ((inline = br.readLine()) != null) {
				if (inline.indexOf("接口") > -1) {
					flag = !flag;
					if (!flag) {
						// 碰到下一个"接口"退出循环
						break;
					}
				}
				if (flag) {
					count++;
					if (count > 2) {
						// 有效IP
						String[] str = inline.split(" {4}");
						list.add(str[0]);
					}
				}
				System.out.println(inline);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(list);
		return list;
	}

	public static ArrayList<InetAddress> getAllIP() {
		iplist.clear();
		try {
			Enumeration<?> nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<?> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					InetAddress ia = (InetAddress) ias.nextElement();
					iplist.add(ia);
				}
			}
		} catch (SocketException ex) {
		}
		return iplist;
	}
}