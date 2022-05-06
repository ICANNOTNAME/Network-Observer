package cn.uusxv.dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;

import cn.uusxv.MainFrame;
import cn.uusxv.NetworkinterfacePanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;

public class SelectNetworkInterface extends JDialog {
	/**
	 */
	private static final long serialVersionUID = 1L;
	private final JButton btnNewButton = new JButton("\u786E\u5B9A");
	private final JFormattedTextField formattedTextField;
	private final JFormattedTextField formattedTextField_1;
	private final JComboBox<String> comboBox;
	private final JSpinner spinner;

	public SelectNetworkInterface(MainFrame frame) {
		super(frame, "新建抓取数据包向导", true);
		setSize(452, 356);
		setLocationRelativeTo(frame);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(panel, BorderLayout.SOUTH);

		JPanel panel_1 = new JPanel();
		formattedTextField = new JFormattedTextField();
		formattedTextField.setBounds(110, 117, 319, 24);
		formattedTextField.setValue(65535);
		panel_1.add(formattedTextField);

		formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setBounds(127, 156, 302, 24);
		formattedTextField_1.setValue(100);
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u76D1 \u542C \u5730 \u5740");
		lblNewLabel.setBounds(10, 29, 81, 33);
		panel_1.add(lblNewLabel);

		comboBox = new JComboBox<String>();
		for (int i = 0; i < NetworkinterfacePanel.d.length; i++) {
			comboBox.addItem(
					NetworkinterfacePanel.d[i].addresses[NetworkinterfacePanel.d[i].addresses.length - 1].address
							.getHostAddress());
		}
		comboBox.setBounds(110, 32, 319, 27);
		panel_1.add(comboBox);

		spinner = new JSpinner();
		spinner.setBounds(293, 77, 123, 30);
		spinner.setEnabled(false);
		panel_1.add(spinner);

		JLabel lblNewLabel_1 = new JLabel("\u6293\u53D6\u6B21\u6570\u9650\u5236");
		lblNewLabel_1.setBounds(10, 77, 81, 32);
		panel_1.add(lblNewLabel_1);

		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.addItem("无限抓取");
		comboBox_1.addItem("有限抓取");
		comboBox_1.setBounds(110, 80, 139, 27);
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_1.getSelectedIndex() == 1) {
					spinner.setEnabled(true);
				} else {
					spinner.setEnabled(false);
				}
			}
		});

		JLabel lblNewLabel_3 = new JLabel("\u6700\u591A\u6355\u83B7\u5B57\u8282\u6570");
		lblNewLabel_3.setBounds(10, 119, 94, 27);
		panel_1.add(lblNewLabel_3);
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 185, 446, 7);
		panel_1.add(separator);

		JCheckBox chckbxNewCheckBox = new JCheckBox("\u6DF7\u6293\u6A21\u5F0F");
		chckbxNewCheckBox.setBounds(10, 198, 73, 27);
		panel_1.add(chckbxNewCheckBox);

		JLabel lblNewLabel_5 = new JLabel(
				"\u6DF7\u6293\u6A21\u5F0F\u4E2D\uFF0C\u53EF\u4EE5\u6355\u83B7\u6240\u6709\u6570\u636E\u5305\uFF0C\u5373\u4FBF\u6E90 MAC \u6216\u76EE\u7684MAC\u5730");
		lblNewLabel_5.setBounds(84, 201, 345, 21);
		panel_1.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel(
				"\u5740\u4E0E\u6253\u5F00\u7684\u7F51\u7EDC\u63A5\u53E3\u7684 MAC\u5730\u5740\u4E0D\u76F8\u540C\u3002\u800C\u975E\u6DF7\u4E71\u6A21\u5F0F\u4E2D\u53EA\u80FD\u6355\u83B7\u7531\u5BBF\u4E3B");
		lblNewLabel_6.setBounds(25, 222, 404, 21);
		panel_1.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("\u673A\u53D1\u9001\u548C\u63A5\u6536\u7684\u6570\u636E\u5305\u3002");
		lblNewLabel_7.setBounds(25, 242, 404, 21);
		panel_1.add(lblNewLabel_7);

		JLabel lblNewLabel_4 = new JLabel("\u7B49\u5F85\u8D85\u65F6(\u5355\u4F4D:\u6BEB\u79D2)");
		lblNewLabel_4.setBounds(10, 156, 123, 22);
		panel_1.add(lblNewLabel_4);

		panel_1.add(formattedTextField_1);
		panel_1.add(comboBox_1);

		JLabel lblNewLabel_2 = new JLabel("\u6B21\u6570");
		lblNewLabel_2.setBounds(259, 81, 33, 24);
		panel_1.add(lblNewLabel_2);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int timeout = (int) formattedTextField_1.getValue();
				int bytecount = (int) formattedTextField.getValue();
				int index = comboBox.getSelectedIndex();
				int count = -1;
				if (comboBox_1.getSelectedIndex() == 1) {
					count = (int) spinner.getValue();
					if (count <= 0) {
						JOptionPane.showMessageDialog(null, "抓取次数无效", "错误", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				boolean hunzhua = chckbxNewCheckBox.isSelected();
				dispose();
				new CatchPacket(index, bytecount, hunzhua, timeout, count).setVisible(true);
				MainFrame.catchNet.setEnabled(false);
			}
		});
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\u53D6\u6D88");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(btnNewButton_1);
	}
}
