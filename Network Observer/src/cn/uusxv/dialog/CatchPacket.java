package cn.uusxv.dialog;

import cn.uusxv.MainFrame;
import cn.uusxv.NetworkinterfacePanel;
import jpcap.JpcapCaptor;
import jpcap.packet.ARPPacket;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class CatchPacket extends JDialog {
    private final DefaultTableModel model;
    private JpcapCaptor captor;
    private Thread catching;

    public CatchPacket(int networkinterfaceindex, int bytecount, boolean catches, int timeout, int count) {
        JMenuItem item = new JMenuItem("抓取数据包");
        item.addActionListener(e -> setVisible(true));
        MainFrame.re.add(item);//添加到“任务”菜单
        //初始化

        setSize(535, 447);
        setLocationRelativeTo(null);
        setTitle("捕捉数据包");

        String[] column = {"类型", "发送者 IP", "接收者 IP", "信息"};
        model = new DefaultTableModel(null, column);
        JTable table = new JTable(model);
        JLabel info = new JLabel("以下是捕捉到的数据包");
        add(info, BorderLayout.NORTH);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        add(new JScrollPane(table));
        catching = new Thread(new Catch(networkinterfaceindex, bytecount, catches, timeout, count));
        catching.start();
        JPanel p1 = new JPanel();
        JButton close = new JButton("停止");
        JButton zanting = new JButton("暂停");
        JButton jixu = new JButton("继续");
        JButton clear = new JButton("清空");
        jixu.setEnabled(false);
        close.addActionListener(e -> {
            captor.close();
            MainFrame.re.remove(item);
            MainFrame.catchNet.setEnabled(true);
            catching.interrupt();
            dispose();
        });
        zanting.addActionListener(e -> {
            captor.close();
            catching.interrupt();
            jixu.setEnabled(true);
            zanting.setEnabled(false);
        });
        jixu.addActionListener(e -> {
            catching = new Thread(new Catch(networkinterfaceindex, bytecount, catches, timeout, count));
            catching.start();
            jixu.setEnabled(false);
            zanting.setEnabled(true);
        });
        clear.addActionListener(ActionListener -> {
            int qingkuang = JOptionPane.showConfirmDialog(null, "真的要清空吗？");
            if (qingkuang == JOptionPane.YES_OPTION) {
                model.setRowCount(0);
            }
        });
        p1.add(close);
        p1.add(zanting);
        p1.add(jixu);
        p1.add(clear);
        add(p1, BorderLayout.SOUTH);
    }

    class Catch implements Runnable {
        int networkinterfaceindex;
        int bytecount;
        boolean catches;
        int timeout;
        int count;

        public Catch(int networkinterfaceindex, int bytecount, boolean catches, int timeout, int count) {
            this.networkinterfaceindex = networkinterfaceindex;
            this.bytecount = bytecount;
            this.catches = catches;
            this.timeout = timeout;
            this.count = count;
        }

        @Override
        public void run() {
            try {
                captor = JpcapCaptor.openDevice(NetworkinterfacePanel.d[networkinterfaceindex], bytecount, catches,
                        timeout);//打开网卡
                //开始捕捉
                captor.loopPacket(count, arg0 -> {//抓到数据包的时候的操作
                    synchronized ("s") {
                        if (arg0 instanceof TCPPacket packet) {
                            String[] row = {"TCP", packet.src_ip.getHostAddress(), packet.dst_ip.getHostAddress(), new String(packet.data)};
                            model.addRow(row);
                        } else if (arg0 instanceof UDPPacket packet) {
                            String[] row = {"UDP", packet.src_ip.getHostAddress(), packet.dst_ip.getHostAddress(), new String(packet.data)};
                            model.addRow(row);
                        } else if (arg0 instanceof ARPPacket packet) {
                            if (!(packet.getSenderProtocolAddress() instanceof String)) {
                                String[] row = {"ARP", new String(packet.sender_protoaddr), new String(packet.target_protoaddr), new String(packet.data)};
                                model.addRow(row);
                            }
                        }
                    }
                });
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "抓包时出现错误。");
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }
}