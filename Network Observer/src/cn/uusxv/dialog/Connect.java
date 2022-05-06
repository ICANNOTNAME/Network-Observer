package cn.uusxv.dialog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import cn.uusxv.MainFrame;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Connect extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket tcp;
	private DatagramSocket udp;
	private final String ip;
	public String TYPE;
	private JTextField textField;
	private final JMenuItem item;
	private JTextPane textPane;
	private DataInputStream dis;
	private DataOutputStream dos;

	public String getIP() {
		return ip;
	}

	public Connect(Socket s, DatagramSocket ss, String ip, JMenuItem item) {
		if (s == null) {
			udp = ss;
			TYPE = "UDP";
		} else {
			tcp = s;
			TYPE = "TCP";
			try {
				dis = new DataInputStream(tcp.getInputStream());
				dos = new DataOutputStream(tcp.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.ip = ip;
		this.item = item;
		init();
		new Thread(new GetMessage()).start();
	}

	private void init() {
		setSize(500, 400);
		setTitle("����ʹ�� " + TYPE + " �� " + ip + " ����");
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));

		textPane = new JTextPane();
		getContentPane().add(textPane, BorderLayout.CENTER);
		textPane.setEditable(false);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(25);

		JButton btnNewButton = new JButton("\u53D1\u9001");
		btnNewButton.addActionListener(new sendMessage());
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\u65AD\u5F00");// �Ͽ���ť
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (TYPE.equals("TCP")) {
					try {
						tcp.close();
						MainFrame.connection.remove(new InetSocketAddress(ip, tcp.getPort()));
					} catch (IOException e1) {
					}
				} else {
					MainFrame.connection.remove(new InetSocketAddress(ip, 0));
				}
				MainFrame.re.remove(item);
				dispose();
			}
		});
		panel.add(btnNewButton_1);

		setVisible(true);
		toFront();
	}

	private class sendMessage implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (TYPE == "TCP") {
				if (!textField.getText().isEmpty()) {
					try {
						dos.write(textField.getText().getBytes());
						Thread th = new Thread(new GetMessage());
						th.start();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(rootPane, "�޷����ͱ�����Ϣ��", "����", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				byte[] b = new byte[1024 * 1024 * 4];
				int port;
				try {
					port = Integer.parseInt(JOptionPane.showInputDialog("�����뷢�͵Ķ˿ں�"));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(rootPane, "�˿ںŲ�������", "����", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					udp.send(new DatagramPacket(textField.getText().getBytes(), textField.getText().getBytes().length,
							InetAddress.getByName(ip), port));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				appendText("���͵� " +ip + " " + new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()), Color.GREEN);
				appendText(textField.getText(), Color.BLACK);
			}
		}
	}

	private synchronized void appendText(String info, Color color) {
		Style style = textPane.addStyle("title", null);// ʹ��title����
		if (color != null) {
			StyleConstants.setForeground(style, color);
		} else {
			StyleConstants.setForeground(style, Color.BLACK);
		}
		textPane.setEditable(true);// �����¼��Ϊ�ɱ༭״̬
		textPane.setCaretPosition(textPane.getDocument().getLength());// �趨�ı�����λ��Ϊ�������ݵĵײ�
		textPane.setCharacterAttributes(style, false);// �����ݵ���ʽ��Ϊstyle��ʽ�����ı�ȫ��
		textPane.replaceSelection(info + "\n");// �ڲ����λ���滻�ı�����
		textPane.setEditable(false);// �����¼��Ϊ���ɱ༭״̬
	}

	private class GetMessage implements Runnable {
		public void run() {
			if (TYPE.equals("TCP")) {
				try {
					String info = dis.readUTF();
					String message = ip + " " + new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()) + "\n";
					appendText(message, Color.BLUE);
					appendText(info, Color.BLACK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				byte[] by = new byte[1024 * 1024 * 4];
				DatagramPacket dp = new DatagramPacket(by, by.length);
				try {
					udp.receive(dp);
					String info = new String(dp.getData());
					String message ="�յ�"+ ip + " ����Ϣ" + new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()) + "\n";
					appendText(message, Color.BLUE);
					appendText(info, Color.BLACK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
