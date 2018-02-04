package xupt.se.ttms.view.clerk;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.UserSrv;
import xupt.se.ttms.service.verifyEmployee;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;
import xupt.se.ttms.view.tmpl.WindowOpacity;

// 添加VIP用户界面
public class UserAddUI extends JFrame implements ActionListener, MouseListener, FocusListener {

	// 定义全局组件
	ImagePanel backgroundPanel;
	JTextField username, tel, email;
	JComboBox<String> select_sex, select_year, select_month, select_day;

	JButton button_ok, button_reset;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	// 定义用户对象
	User user = null;

	public UserAddUI() {

		new WindowOpacity(this);
		
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

//		try {
//			Image bgimg = ImageIO.read(new File("image/main2.png"));
//			backgroundPanel = new ImagePanel(bgimg);
//			// 获取背景面板大小
//			this.width = backgroundPanel.getWidth();
//			this.height = backgroundPanel.getHeight();
//			backgroundPanel.setLayout(new BorderLayout());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		backgroundPanel = new ImagePanel(null);
		backgroundPanel.setLayout(null);
		
		initContentPanel();
		
		this.add(backgroundPanel);
		this.setTitle("添加VIP用户");
		this.setSize((int) (width * 0.28f), (int) (height * 0.60f));
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
	}

	public void initContentPanel() {
		
		String sexs[] = {"male", "female"};
		String years[] = {"1986","1987", "1988", "1989", "1990", "1991","1992", "1993", "1994", "1995", "1996","1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004",
				"2005", "2006", "2007", "2008", "2009", "2010", "2012", "2013", "2014",
				"2015", "2016", "2017"};
		String months[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		String days[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
				"25", "26", "27", "28", "29", "30", "31"};
		
		JLabel label = new JLabel();
		label.setText("添加VIP用户");
		label.setBounds(110, 15, 140, 30);
		label.setFont(MyFont.Title);

		username = new JTextField(30);
		username.setBounds(25, 60, 280, 30);
        username.setFont(MyFont.Static);
        //username.setForeground(Color.gray);
        username.addFocusListener(this);
        username.setText("用户名");

		JLabel sex = new JLabel("性别");
		sex.setFont(MyFont.Item);
		sex.setBounds(25, 110, 60, 30);
		
		select_sex = new JComboBox<String>(sexs);
		select_sex.setBounds(95, 110, 210, 30);
		select_sex.addActionListener(this);
        
		JLabel birth = new JLabel("生日");
		birth.setFont(MyFont.Item);
		birth.setBounds(25, 160, 40, 30);
		
		select_year = new JComboBox<String>(years);
		select_year.setBounds(70, 160, 80, 30);
		select_year.addActionListener(this);
		
		JLabel label_year = new JLabel("-");
		label_year.setFont(MyFont.Item);
		label_year.setBounds(155, 160, 8, 30);
		
		select_month = new JComboBox<String>(months);
		select_month.setBounds(168, 160, 70, 30);
		select_month.addActionListener(this);
		
		JLabel label_month = new JLabel("-");
		label_month.setFont(MyFont.Item);
		label_month.setBounds(240, 160, 8, 30);
		
		select_day = new JComboBox<String>(days);
		select_day.setBounds(252, 160, 70, 30);
		select_day.addActionListener(this);
		
//		JLabel label_day = new JLabel("日");
//		birth.setFont(MyFont.Item);
//		birth.setBounds(300, 160, 10, 30);
		
		tel = new JTextField(30);
		tel.setBounds(25, 210, 280, 30);
		tel.setFont(MyFont.Static);
        //tel.setForeground(Color.gray);
		tel.addFocusListener(this);
		tel.setText("手机号");
        
        
		email = new JTextField(30);
		email.setBounds(25, 260, 280, 30);
		email.setFont(MyFont.Static);
        //email.setForeground(Color.gray);
		email.addFocusListener(this);
		email.setText("邮箱");

        button_ok = new JButton("添加");
        button_ok.setBounds(55, 310, 65, 30);
        button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_ok.setForeground(Color.white);
        button_ok.setFont(MyFont.Static);
        button_ok.addMouseListener(this);

        button_reset = new JButton("重置");
        button_reset.setBounds(185, 310, 65, 30);
        button_reset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        button_reset.setForeground(Color.white);
        button_reset.setFont(MyFont.Static);
        button_reset.addMouseListener(this);

        backgroundPanel.add(label);
        backgroundPanel.add(username);
        backgroundPanel.add(sex);
        backgroundPanel.add(select_sex);
        backgroundPanel.add(birth);
        backgroundPanel.add(select_year);
        backgroundPanel.add(select_month);
        backgroundPanel.add(select_day);
        backgroundPanel.add(label_year);
        backgroundPanel.add(label_month);
        backgroundPanel.add(tel);
        backgroundPanel.add(email);

        backgroundPanel.add(button_ok);
        backgroundPanel.add(button_reset);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_ok) {
			String birthday = select_year.getSelectedItem().toString() + "-" + select_month.getSelectedItem().toString() + "-" + select_day.getSelectedItem().toString();
			String params[] = { username.getText(), select_sex.getSelectedItem().toString(),birthday, tel.getText(), email.getText()};
			if (params.length < 5) {
				JOptionPane.showMessageDialog(null, "请完善用户信息 !!!");
			} else {
				user = new User();
				user.setUsername(params[0]);
				user.setUserSex(params[1]);
				user.setUserBirthday(params[2]);
				user.setUserTelnum(params[3]);
				user.setuserEmail(params[4]);
				new UserSrv().add(user);
				JOptionPane.showMessageDialog(null, "VIP用户插入成功 !!!");
				this.dispose();	
			}
		}
		
		if (e.getSource() == button_reset) {
			username.setText("用户名");
			tel.setText("手机号");
			email.setText("邮箱");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

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
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == username) {
			if (username.getText().equals("用户名")) {
				username.setText("");
			}
		} else if (e.getSource() == tel) {
			if (tel.getText().equals("手机号")) {
				tel.setText("");
			}
		} else if (e.getSource() == email) {
			if (email.getText().equals("邮箱")) {
				email.setText("");
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == username) {
			if (username.getText().equals("")) {
				username.setText("用户名");
			}
		} else if (e.getSource() == tel) {
			if (tel.getText().equals("")) {
				tel.setText("手机号");
			}
		} else if (e.getSource() == email) {
			if (email.getText().equals("")) {
				email.setText("邮箱");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
	}
	
//	public static void main(String[] args) {
//		new UserAddUI();
//	}
}
