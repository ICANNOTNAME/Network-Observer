package cn.uusxv;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cn.uusxv.net.LAN;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class NetworkinterfacePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DefaultTableModel model;
	private final JTable table;
	public static NetworkInterface[] d = JpcapCaptor.getDeviceList();

	public NetworkinterfacePanel() {
		String[] header = { "Íø¿¨Ãû", "ÃèÊö" };
		model = new DefaultTableModel(null, header);
		table = new JTable(model);
		setLayout(new BorderLayout());
		add(new JScrollPane(table));
		new Thread() {
			@Override
			public void run() {
				load();
			}
		}.start();

	}

	public synchronized void load() {
		model.setRowCount(0);
//		ArrayList<String> l = LAN.getIPs();
		for (int i = 0; i < d.length; i++) {
			String row[] = { d[i].name, d[i].description };
			model.addRow(row);
		}
	}
}