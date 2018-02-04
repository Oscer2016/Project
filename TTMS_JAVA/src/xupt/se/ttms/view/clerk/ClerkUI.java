package xupt.se.ttms.view.clerk;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.view.main.Entrance;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.User;

public class ClerkUI extends JFrame implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;

	// 定义员工对象
	private Employee emp;
	
	// 定义VIP用户对象
	private User user;

	// 定义辅助变量
	int sign_home = 0;
	int sign_modify = 0;
	int sign_usr = 0;
	int sign_back = 0;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JPanel backgroundPanel, topPanel, topMenu, topPrompt, subPanel, subMenu;
	ImagePanel centerPanel;
	JTabbedPane jTabbedPane;
	JLabel home, modify, usr, back;
	JButton button_sale, button_refund, button_browse;

	public ClerkUI(Employee emp) {
		
		this.emp = emp;
		
		//窗口淡入淡出
		new WindowOpacity(this);
		
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

		initBackgroundPanel();
		
		this.setTitle("售票员界面");
		this.setSize((int)(width * 0.7f), (int)(height * 0.77f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());

		initTop();
		initCenterPanel();

		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");

		this.add(backgroundPanel);
	}

	// 初始化顶部面板
	public void initTop() {
		Image imgPanel = null;
		try {
			imgPanel = ImageIO.read(new File("image/top.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initTopMenu();
		initTopPrompt();

//		topPanel = new JPanel(new BorderLayout());
		topPanel = new ImagePanel(imgPanel);
		topPanel.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(width, 40));
//		topMenu.setBounds(17, 9, 200, 30);
//		topPrompt.setBounds(820, 10, 200,30);
//		topPanel.add(topMenu);
//		topPanel.add(topPrompt);

		topPanel.add(topMenu, "West");
		topPanel.add(topPrompt, "East");
	}

	// 初始化顶部菜单
	public void initTopMenu() {

		topMenu = new JPanel();
		topMenu.setPreferredSize(new Dimension(250, 50));
		topMenu.setBounds(10, 10, 100, 20);
		topMenu.setOpaque(false);

		String[] nameStrings = { "首页", "修改密码", "VIP", "返回" };

		home = CreateMenuLabel(home, nameStrings[0], "home", topMenu);
		home.setName("home");
		modify = CreateMenuLabel(modify, nameStrings[1], "modify", topMenu);
		modify.setName("modify");
		usr = CreateMenuLabel(usr, nameStrings[2], "usr", topMenu);
		usr.setName("usr");
		back = CreateMenuLabel(back, nameStrings[3], "Back", topMenu);
		back.setName("Back");
	}

	// 创建顶部菜单Label
	public JLabel CreateMenuLabel(JLabel jlb, String text, String name, JPanel who) {
		JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
//		Icon icon = new ImageIcon("image/" + name + ".png");
		jlb = new JLabel();
		jlb.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
		jlb.addMouseListener(this);
		jlb.setFont(MyFont.Static);
		who.add(jlb);
		if (!"back".equals(name)) {
			who.add(line);
		}
		return jlb;
	}

	// 初始化顶部欢迎面板
	public void initTopPrompt() {

		JLabel label = new JLabel();
		if (emp != null) {
			label.setText("<html><font color='black'> 欢迎您, </font><font color='#336699'><b>" + this.emp.getEmployee_name()
					+ "</b></font></html>");
		} else {
			label.setText("<html><font color='black'> 欢迎您, </font><font color='#336699'><b></b></font></html>");
		}
		label.setFont(MyFont.Static);
		topPrompt = new JPanel();
		topPrompt.setPreferredSize(new Dimension(100, 30));
		topPrompt.setOpaque(false);
		topPrompt.add(label);
	}

	// 初始化中心面板
	public void initCenterPanel() {
		
		Image bgimg = null;
		try {
			bgimg = ImageIO.read(new File("image/main2.png"));
		} catch (IOException e) {
//			e.printStackTrace();
		}	
		centerPanel = new ImagePanel(bgimg);
		centerPanel.setLayout(null);
		home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		creatHome();
//		centerPanel.setOpaque(false);
	}

	// 初始化辅助变量
	public void initSign() {
		sign_home = 0;
		sign_modify = 0;
		sign_usr = 0;
		sign_back = 0;
	}

	// 创建首页面板
	public void creatHome() {

		centerPanel.removeAll();
		
		button_sale = new JButton();
		button_sale.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_sale.setHorizontalTextPosition(SwingConstants.CENTER);
		button_sale.setIcon(new ImageIcon("image/tickets.jpg"));
		button_sale.setBackground(Color.WHITE);
		button_sale.setFont(MyFont.Static);
		button_sale.setText(" 售票 ");
		button_sale.setBounds(85, 240, 160, 160);
		button_sale.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_sale.addMouseListener(this);
		
		button_refund = new JButton();
		button_refund.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_refund.setHorizontalTextPosition(SwingConstants.CENTER);
		button_refund.setIcon(new ImageIcon("image/tickets.jpg"));
		button_refund.setBackground(Color.WHITE);
		button_refund.setFont(MyFont.Static);
		button_refund.setText(" 退票 ");
		button_refund.setBounds(355, 240, 160, 160);
		button_refund.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_refund.addMouseListener(this);
		
		button_browse = new JButton();
		button_browse.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_browse.setHorizontalTextPosition(SwingConstants.CENTER);
		button_browse.setIcon(new ImageIcon("image/tickets.jpg"));
		button_browse.setBackground(Color.WHITE);
		button_browse.setFont(MyFont.Static);
		button_browse.setText(" 销售数据 ");
		button_browse.setBounds(625, 240, 160, 160);
		button_browse.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_browse.addMouseListener(this);
		
		centerPanel.add(button_sale);
		centerPanel.add(button_refund);
		centerPanel.add(button_browse);	
	}

	// 创建用户管理面板
	public void creatUserManagerTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(MyFont.Static);

		jTabbedPane.addTab("VIP管理", new UserAddJpanel(user, this).backgroundPanel);
		this.add(jTabbedPane, "Center");
	}
	
	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == home) {
			initSign();
			sign_home = 1;
			creatHome();
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
			modify.setText("<html><font color='black'>" + "修改密码" + "</font>&nbsp;</html>");
			usr.setText("<html><font color='black'>" + "VIP" + "</font>&nbsp;</html>");
			back.setText("<html><font color='black'>" + "返回" + "</font>&nbsp;</html>");
		} else if (e.getSource() == modify) {
			initSign();
			sign_modify = 1;
			// creatUserManagerTab();
			new ModifyPasswdUI(this, emp);
			modify.setText("<html><font color='#336699' style='font-weight:bold'>" + "修改密码" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			usr.setText("<html><font color='black'>" + "VIP" + "</font>&nbsp;</html>");
			back.setText("<html><font color='black'>" + "返回" + "</font>&nbsp;</html>");
		} else if (e.getSource() == usr) {
			initSign();
			sign_usr = 1;
			// creatUserManagerTab();
			new UserAddUI();
			usr.setText("<html><font color='#336699' style='font-weight:bold'>" + "VIP" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			modify.setText("<html><font color='black'>" + "修改密码" + "</font>&nbsp;</html>");
			back.setText("<html><font color='black'>" + "返回" + "</font>&nbsp;</html>");
		} else if (e.getSource() == back) {
			initSign();
			sign_back = 1;
			this.setVisible(false);
			Entrance.main(null);
		} else if (e.getSource() == button_sale) {
//			this.dispose();
			new QueryScheduleUI(emp,"");
		} else if (e.getSource() == button_browse) {
//			creatBrowseTicketTab();
//			this.dispose();
			new AnalyzeFrame(emp);

		} else if (e.getSource() == button_refund) {
//			this.dispose();
			new RefundFrame(emp);

		} 
	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == home) {
			home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		} else if (e.getSource() == modify) {
			modify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			modify.setText("<html><font color='#336699' style='font-weight:bold'>" + "修改密码" + "</font>&nbsp;</html>");
		} else if (e.getSource() == usr) {
			usr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			usr.setText("<html><font color='#336699' style='font-weight:bold'>" + "VIP" + "</font>&nbsp;</html>");
		} else if (e.getSource() == back) {
			back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			back.setText("<html><font color='#336699' style='font-weight:bold'>" + "返回" + "</font>&nbsp;</html>");
		} else if (e.getSource() == button_sale) {
			button_sale.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else if (e.getSource() == button_refund) {
			button_refund.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else if (e.getSource() == button_browse) {
			button_browse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	// 鼠标划出事件
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == home) {
			if (sign_home == 0) {
				home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == modify) {
			if (sign_modify == 0) {
				modify.setText("<html><font color='black'>" + "修改密码" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == usr) {
			if (sign_usr == 0) {
				usr.setText("<html><font color='black'>" + "VIP" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == back) {
			if (sign_back == 0) {
				back.setText("<html><font color='black'>" + "返回" + "</font>&nbsp;</html>");
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}