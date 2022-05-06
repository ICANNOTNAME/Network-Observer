package cn.uusxv;

import cn.uusxv.dialog.Connect;
import cn.uusxv.dialog.NewConn;
import cn.uusxv.dialog.SelectNetworkInterface;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    /**
     * ���˵�
     */
    private JMenuBar menu;
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    /**
     * �������ݰ�
     */
    public static DatagramSocket socket;
    /**
     * ��������
     */
    public static Map<InetSocketAddress, Connect> connection;
    public static JMenu re;
    public static JMenuItem catchNet;

    private MainFrame() {
        //��ʼ������
        super("����۲���");
        connection = new HashMap<>();
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initJMenuBar();
        NetworkinterfacePanel lanIp = new NetworkinterfacePanel();
        //�򿪷�����
        try {
            socket = new DatagramSocket(25899);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(this, "25899 �˿ڱ�ռ�ã�����ʹ�� UDP ���ܡ�", "����", JOptionPane.WARNING_MESSAGE);
        }
        add(lanIp);
        setJMenuBar(menu);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MainFrame().setVisible(true);
    }

    /**
     * ��ʼ�����˵�
     */
    public void initJMenuBar() {
        menu = new JMenuBar();
        JMenu net = new JMenu("����");
        JMenuItem newNet = new JMenuItem("�½�����(O)");
        newNet.setMnemonic(KeyEvent.VK_O);
        newNet.setAccelerator(KeyStroke.getKeyStroke("control O"));
        newNet.addActionListener(e -> new NewConn(MainFrame.this).setVisible(true));
        catchNet = new JMenuItem("ץȡ����(C)");
        catchNet.setMnemonic(KeyEvent.VK_C);
        catchNet.addActionListener(e -> new SelectNetworkInterface(MainFrame.this).setVisible(true));
        JMenu help = new JMenu("����");
        JMenuItem about = new JMenuItem("����");
        about.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this, """
                �����������̰������о���ѧϰ֮��,
                ��ֹ������ҵ!
                ����㲻ͬ�⣬������ɾ���������
                ���� QQ : 2484489782"""));
        help.add(about);
        catchNet.setAccelerator(KeyStroke.getKeyStroke("control alt C"));
        re = new JMenu("����");

        net.add(newNet);
        net.add(catchNet);

        menu.add(net);
        menu.add(re);
        menu.add(help);
    }
}
