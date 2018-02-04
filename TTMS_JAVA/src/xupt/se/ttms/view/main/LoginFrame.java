package xupt.se.ttms.view.main;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import com.sun.awt.AWTUtilities;

import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.clerk.ClerkUI;
import xupt.se.ttms.view.manager.ManagerUI;
import xupt.se.ttms.view.seat.SeatMgrUI;
import xupt.se.ttms.view.seat.SeatModifyUI;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.verifyEmployee;


public class LoginFrame extends JFrame implements ActionListener, MouseListener, FocusListener {

	private static final long serialVersionUID = 1L;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	// 全局的位置变量，用于表示鼠标在窗口上的位置
	//	static Point origin = new Point();

	// 定义全局组件
	JTextField username = new JTextField(16);
	JPasswordField password = new JPasswordField(16);
	JComboBox<String> select_position;
	
	ImagePanel backgroundPanel = null;
	JButton button_minimize, button_close, button_login, button_reset;
	JLabel label_position;

	public LoginFrame() {
		
		//窗口淡入淡出
		new WindowOpacity(this);
		
		Image backgrounImage = null;
		String positions[] = {"admin", "clerk", "manager"};
		
		try {
			backgrounImage = ImageIO.read(new File("image/main.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 窗口背景面板
		backgroundPanel = new ImagePanel(backgrounImage);
		backgroundPanel.setLayout(null);

//		Font font = new Font("楷体", Font.BOLD, 16);
//		user = new JLabel("username");	
//		user.setBounds(330, 400, 173, 30);
//		user.setFont(font);
		
//		System.out.println((int)(width*0.31));
//		System.out.println((int)(height*0.85));
		
		username.setBounds(90, 260, 173, 30);
		username.setFont(MyFont.Static);
		//username.setForeground(Color.gray);
		username.addFocusListener(this);
		username.setText("用户名");
//		passwd = new JLabel("password");
//		passwd.setFont(font);
//		passwd.setBounds(330, 440, 173, 30);
		password.setBounds(90, 300, 173, 30);
		password.setFont(MyFont.Static);
		//password.setForeground(Color.gray);
		password.addFocusListener(this);
		password.setText("密码");
		password.setEchoChar('\0');
		
		label_position = new JLabel("职位 ");
		label_position.setFont(MyFont.Static);
		label_position.setBounds(90, 346, 40, 36);
		
		select_position = new JComboBox<String>(positions);
		select_position.setBounds(140, 346, 124, 30);
		select_position.addActionListener(this);

		button_login = new JButton("登录");
		button_login.setBounds(90, 390, 65, 27);
		button_login.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_login.setForeground(Color.white);
		button_login.setFont(MyFont.Static);
		button_login.addMouseListener(this);

		button_reset = new JButton("重置");
		button_reset.setBounds(198, 390, 65, 27);
		button_reset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_reset.setForeground(Color.white);
		button_reset.setFont(MyFont.Static);
		button_reset.addMouseListener(this);

		backgroundPanel.add(username);
//		backgroundPanel.add(user);
		backgroundPanel.add(password);
//		backgroundPanel.add(passwd);
		backgroundPanel.add(label_position);
		backgroundPanel.add(select_position);
		backgroundPanel.add(button_login);
		backgroundPanel.add(button_reset);

		this.add(backgroundPanel);
		this.setTitle("风影剧院票务管理系统");
		this.setSize((int)(width * 0.31f), (int)(height * 0.85f));
		// 隐藏窗体
//		this.setUndecorated(true);
//		AWTUtilities.setWindowOpaque(this, false);
//		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	// 鼠标点击事件
	@SuppressWarnings("deprecation")
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_login) {
			String position = select_position.getSelectedItem().toString();
			System.out.println(position);
			if ("用户名".equals(username.getText())) {
				JOptionPane.showMessageDialog(null, "用户名不能为空!!!");
			} else if ("密码".equals(password.getText())) {
				JOptionPane.showMessageDialog(null, "密码不能为空!!!");
			} else {
				String params[] = { username.getText(), password.getText(), position };
				try {
					Employee emp = new verifyEmployee(params[0], params[1], params[2]).verifyPasswd();
					if (emp.getFlag() == 0) {
						JOptionPane.showMessageDialog(null, "用户不存在!!!");
					} else if (emp.getFlag() == 1) {
						JOptionPane.showMessageDialog(null, "密码有误 !!!");
					} else if (emp.getFlag() == 2) {
						JOptionPane.showMessageDialog(null, "职位信息有误 !!!");
					} else if (emp.getFlag() == 3) {

						this.dispose();
						if (params[2].equals("admin")) {
							this.dispose();
							new AdminUI();
						} else if (params[2].equals("clerk")) {
							this.dispose();
							new ClerkUI(emp);
						} else if (params[2].equals("manager")) {
							this.dispose();
							new ManagerUI(emp);
						}
					} else {
						JOptionPane.showMessageDialog(null, "用户名或密码有误 !!!");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == button_reset) {
			username.setText("用户名");
			password.setText("密码");
			password.setEchoChar('\0');
		}

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == username) {
			if (username.getText().equals("用户名")) {
				username.setText("");
			}
		} else if (e.getSource() == password) {
			if (password.getText().equals("密码")) {
				password.setText("");
				password.setEchoChar('*');
			}
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == username) {
			if (username.getText().equals("")) {
				username.setText("用户名");
			}
		} else if (e.getSource() == password) {
			if (password.getText().equals("")) {
				password.setText("密码");
				password.setEchoChar('\0');
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == select_position) {
		}
	}
}
