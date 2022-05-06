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
        JMenuItem item = new JMenuItem("ץȡ���ݰ�");
        item.addActionListener(e -> setVisible(true));
        MainFrame.re.add(item);//��ӵ������񡱲˵�
        //��ʼ��

        setSize(535, 447);
        setLocationRelativeTo(null);
        setTitle("��׽���ݰ�");

        String[] column = {"����", "������ IP", "������ IP", "��Ϣ"};
        model = new DefaultTableModel(null, column);
        JTable table = new JTable(model);
        JLabel info = new JLabel("�����ǲ�׽�������ݰ�");
        add(info, BorderLayout.NORTH);
        table.setFont(new Font("΢���ź�", Font.PLAIN, 16));
        add(new JScrollPane(table));
        catching = new Thread(new Catch(networkinterfaceindex, bytecount, catches, timeout, count));
        catching.start();
        JPanel p1 = new JPanel();
        JButton close = new JButton("ֹͣ");
        JButton zanting = new JButton("��ͣ");
        JButton jixu = new JButton("����");
        JButton clear = new JButton("���");
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
            int qingkuang = JOptionPane.showConfirmDialog(null, "���Ҫ�����");
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
                        timeout);//������
                //��ʼ��׽
                captor.loopPacket(count, arg0 -> {//ץ�����ݰ���ʱ��Ĳ���
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
                JOptionPane.showMessageDialog(null, "ץ��ʱ���ִ���");
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }
}