package xupt.se.ttms.view.manager;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;
import xupt.se.ttms.view.main.Entrance;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.EmployeeSrv;

public class ModifyPasswdUI extends JFrame implements MouseListener, FocusListener {

	// 定义全局组件
	ImagePanel backgroundPanel;
	JPasswordField password_old = new JPasswordField(16);
	JPasswordField password_new = new JPasswordField(16);
	JPasswordField password_verify = new JPasswordField(16);
	JButton button_ok, button_cancel;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义用户对象
	Employee emp = new Employee();
	JFrame jframe = null;

	public ModifyPasswdUI(ManagerUI jframe, Employee emp) {

		this.emp = emp;
		this.jframe = jframe;

		//窗口淡入淡出
		new WindowOpacity(this);

		Image backgrounImage = null;
		try {
			backgrounImage = ImageIO.read(new File("image/passwd.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 窗口背景面板
		backgroundPanel = new ImagePanel(backgrounImage);
		backgroundPanel.setLayout(null);

		password_old.setBounds(25, 70, 190, 30);
		password_old.setFont(MyFont.Static);
		//password_old.setForeground(Color.gray);
		password_old.addFocusListener(this);
		password_old.setText("原始密码");
		password_old.setEchoChar('\0');

		password_new.setBounds(25, 120, 190, 30);
		password_new.setFont(MyFont.Static);
		//password.setForeground(Color.gray);
		password_new.addFocusListener(this);
		password_new.setText("新密码");
		password_new.setEchoChar('\0');

		password_verify.setBounds(25, 170, 190, 30);
		password_verify.setFont(MyFont.Static);
		password_verify.addFocusListener(this);
		password_verify.setText("验证密码");
		password_verify.setEchoChar('\0');

		button_ok = new JButton("确认");
		button_ok.setBounds(40, 240, 65, 27);
		button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_ok.setForeground(Color.white);
		button_ok.setFont(MyFont.Static);
		button_ok.addMouseListener(this);

		button_cancel = new JButton("取消");
		button_cancel.setBounds(130, 240, 65, 27);
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_cancel.setForeground(Color.white);
		button_cancel.setFont(MyFont.Static);
		button_cancel.addMouseListener(this);

		backgroundPanel.add(password_old);
		backgroundPanel.add(password_new);
		backgroundPanel.add(password_verify);
		backgroundPanel.add(button_ok);
		backgroundPanel.add(button_cancel);

		this.add(backgroundPanel);
		this.setTitle("修改密码");
		this.setSize((int) (width * 0.22f), (int) (height * 0.50f));
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_ok) {
//			String input_password = (String) JOptionPane.showInputDialog(null, "Please input original password", "User verification",
//					JOptionPane.PLAIN_MESSAGE);
			String password1 = password_old.getText();
			String password2 = password_new.getText();
			String password3 = password_verify.getText();

			if (password1.equals("原始密码") || password2.equals("新密码") || password3.equals("验证密码")) {
				JOptionPane.showMessageDialog(null, "密码不能为空!");
			} else {
				if (!password1.equals(emp.getEmployee_passwd())) {
					JOptionPane.showMessageDialog(null, "原始密码错误!");
				} else if (!password3.equals(password2)) {
					JOptionPane.showMessageDialog(null, "修改失败，两次输入密码不一致!");
				} else if (password3.length() < 6) {
					JOptionPane.showMessageDialog(null, "密码强度不够，请重新修改!");
				} else {
					JOptionPane.showMessageDialog(null, "修改成功，请重新登录...");
					emp.setEmployee_passwd(password2);
					new EmployeeSrv().modify(emp);
					this.dispose();
					this.jframe.dispose();
					Entrance.main(null);
				}
				password_old.setText("");
				password_new.setText("");
				password_verify.setText("");
			}
		} else if (e.getSource() == button_cancel) {
			this.dispose();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
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
		if (e.getSource() == password_old) {
			if (password_old.getText().equals("原始密码")) {
				password_old.setText("");
				password_old.setEchoChar('*');
			}
		} else if (e.getSource() == password_new) {
			if (password_new.getText().equals("新密码")) {
				password_new.setText("");
				password_new.setEchoChar('*');
			}
		} else if (e.getSource() == password_verify) {
			if (password_verify.getText().equals("验证密码")) {
				password_verify.setText("");
				password_verify.setEchoChar('*');
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == password_old) {
			if (password_old.getText().equals("")) {
				password_old.setText("原始密码");
				password_old.setEchoChar('\0');
			}
		} else if (e.getSource() == password_new) {
			if (password_new.getText().equals("")) {
				password_new.setText("新密码");
				password_new.setEchoChar('\0');
			}
		} else if (e.getSource() == password_verify) {
			if (password_verify.getText().equals("")) {
				password_verify.setText("验证密码");
				password_verify.setEchoChar('\0');
			}
		}
	}
	
//	public static void main(String[] args) {
//		new ModifyPasswdUI();
//	}
}
