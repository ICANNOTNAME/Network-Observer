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
     * 主菜单
     */
    private JMenuBar menu;
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    /**
     * 接收数据包
     */
    public static DatagramSocket socket;
    /**
     * 储存连接
     */
    public static Map<InetSocketAddress, Connect> connection;
    public static JMenu re;
    public static JMenuItem catchNet;

    private MainFrame() {
        //初始化窗体
        super("网络观察者");
        connection = new HashMap<>();
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initJMenuBar();
        NetworkinterfacePanel lanIp = new NetworkinterfacePanel();
        //打开服务器
        try {
            socket = new DatagramSocket(25899);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(this, "25899 端口被占用，不能使用 UDP 功能。", "警告", JOptionPane.WARNING_MESSAGE);
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
     * 初始化主菜单
     */
    public void initJMenuBar() {
        menu = new JMenuBar();
        JMenu net = new JMenu("网络");
        JMenuItem newNet = new JMenuItem("新建连接(O)");
        newNet.setMnemonic(KeyEvent.VK_O);
        newNet.setAccelerator(KeyStroke.getKeyStroke("control O"));
        newNet.addActionListener(e -> new NewConn(MainFrame.this).setVisible(true));
        catchNet = new JMenuItem("抓取数据(C)");
        catchNet.setMnemonic(KeyEvent.VK_C);
        catchNet.addActionListener(e -> new SelectNetworkInterface(MainFrame.this).setVisible(true));
        JMenu help = new JMenu("帮助");
        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this, """
                本软件仅供编程爱好者研究和学习之用,
                禁止用于商业!
                如果你不同意，请立刻删除本软件！
                作者 QQ : 2484489782"""));
        help.add(about);
        catchNet.setAccelerator(KeyStroke.getKeyStroke("control alt C"));
        re = new JMenu("任务");

        net.add(newNet);
        net.add(catchNet);

        menu.add(net);
        menu.add(re);
        menu.add(help);
    }
}
