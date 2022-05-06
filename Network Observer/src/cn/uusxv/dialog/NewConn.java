package cn.uusxv.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.uusxv.MainFrame;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;

public class NewConn extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final JTextField textField;
	private final JTextField textField_1;
	private final JComboBox<String> comboBox;
	private final JProgressBar progressBar;
	private ConnectToHost conn = new ConnectToHost();

	/**
	 * Create the dialog.
	 */
	private class ConnectToHost extends Thread {// 连接到指定服务器端
		public void run() {
			try {// 此IP是否重复添加
				if (!MainFrame.connection.containsKey(new InetSocketAddress(textField.getText(), 0))
						|| !MainFrame.connection.containsKey(
								new InetSocketAddress(textField.getText(), Integer.parseInt(textField_1.getText())))) {
					if (comboBox.getSelectedItem().equals(MainFrame.TCP)) {
						tcp();
					} else if (comboBox.getSelectedItem().equals(MainFrame.UDP)) {
						udp();
					}
				} else {
					progressBar.setIndeterminate(false);
					JOptionPane.showMessageDialog(rootPane, "这个 IP 的连接没有断开，或者是端口输入错误。", "重复打开同一个连接",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				udp();
			}
		}
	}

	private void tcp() {
		try {// 连接到此服务器
			Socket s = new Socket(textField.getText(), Integer.parseInt(textField_1.getText()));
			JMenuItem item = new JMenuItem(
					MainFrame.TCP + "-" + InetAddress.getByName(textField.getText()).getHostAddress());
			MainFrame.connection.put(
					new InetSocketAddress(textField.getText(), Integer.parseInt(textField_1.getText())),
					new Connect(s, null, textField.getText(),item));
			dispose();
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainFrame.connection
							.get(new InetSocketAddress(textField.getText(), Integer.parseInt(textField_1.getText())))
							.setVisible(true);
					MainFrame.connection
							.get(new InetSocketAddress(textField.getText(), Integer.parseInt(textField_1.getText())))
							.toFront();
				}
			});
			MainFrame.re.add(item);
		} catch (NumberFormatException e1) {
			progressBar.setIndeterminate(false);
			progressBar.setString("端口号错误");
		} catch (UnknownHostException e1) {
			progressBar.setIndeterminate(false);
			progressBar.setString("IP地址错误");
		} catch (IOException e1) {
			progressBar.setIndeterminate(false);
			progressBar.setString("无法连接到“" + textField.getText() + "”");
		}
	}

	private void udp() {
		if (!MainFrame.connection.containsKey(new InetSocketAddress(textField.getText(), 0))) {
			try {
				InetAddress addr = InetAddress.getByName(textField.getText());
				if (!addr.isReachable(15000)) {// 如果不能在15秒内连接到此IP
					progressBar.setIndeterminate(false);
					progressBar.setString("无法在10秒内连接");
					return;
				} // 放置此IP
				JMenuItem item = new JMenuItem(MainFrame.UDP + "-" + addr.getHostAddress());
				dispose();
				MainFrame.connection.put(new InetSocketAddress(textField.getText(), 0),
						new Connect(null, MainFrame.socket, textField.getText(),item));
				
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						MainFrame.connection.get(new InetSocketAddress(textField.getText(), 0)).setVisible(true);
						MainFrame.connection.get(new InetSocketAddress(textField.getText(), 0)).toFront();
					}
				});
				MainFrame.re.add(item);
			} catch (UnknownHostException e) {
				progressBar.setIndeterminate(false);
				progressBar.setString("IP地址错误");
			} catch (IOException e) {
				progressBar.setIndeterminate(false);
				progressBar.setString("无法连接到目标主机");
			}
		} else {
			progressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(rootPane, "这个 IP 的连接没有断开，或者是端口输入错误。", "重复打开同一个连接", JOptionPane.ERROR_MESSAGE);
		}
	}

	public NewConn(JFrame frame) {
		// 初始化窗体
		super(frame, true);
		setTitle("\u65B0\u5EFA\u8FDE\u63A5");
		setSize(310, 245);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setBounds(10, 10, 19, 29);
		contentPanel.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(104, 14, 158, 21);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("端口(TCP)");
		lblNewLabel_1.setBounds(10, 49, 87, 29);
		contentPanel.add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(104, 53, 158, 21);
		contentPanel.add(textField_1);

		comboBox = new JComboBox<String>();
		comboBox.addItem(MainFrame.TCP);
		comboBox.addItem(MainFrame.UDP);
		comboBox.setBounds(104, 92, 158, 21);
		contentPanel.add(comboBox);

		JLabel lblNewLabel_1_1 = new JLabel("\u8FDE\u63A5\u65B9\u5F0F");
		lblNewLabel_1_1.setBounds(10, 88, 64, 29);
		contentPanel.add(lblNewLabel_1_1);

		progressBar = new JProgressBar();
		progressBar.setBounds(47, 127, 215, 20);
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		progressBar.setString("等待连接");
		contentPanel.add(progressBar);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("确定");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						progressBar.setIndeterminate(true);
						progressBar.setString("正在连接到“" + textField.getText() + "”");
						// 每点击一次确定，重启一次连接线程
						conn.stop();
						conn = null;
						conn = new ConnectToHost();
						conn.start();
					}
				});
				okButton.setActionCommand("确定");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.setActionCommand("取消");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}

				});
				buttonPane.add(cancelButton);
			}
		}
	}
}
