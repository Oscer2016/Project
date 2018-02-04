package xupt.se.ttms.view.clerk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.User;
import xupt.se.ttms.service.UserSrv;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;

/**
 * VIP用户添加面板
 */
public class UserAddJpanel implements MouseListener, FocusListener, ActionListener {

	// 定义全局组件
	ImagePanel backgroundPanel;
	JPanel centerPanel;
	JTextField username, birthday, tel, email;
	JComboBox<String> select_sex;

	JButton button_ok, button_reset;

	// 定义大小变量
	int width;
	int height;

	// 定义用户对象
	User user = null;
	JFrame jframe = null;

	public UserAddJpanel(User user, JFrame jframe) {
		
		this.user = user;
		this.jframe = jframe;


		try {
			Image bgimg = ImageIO.read(new File("image/main2.png"));
			backgroundPanel = new ImagePanel(bgimg);
			// 获取背景面板大小
			this.width = backgroundPanel.getWidth();
			this.height = backgroundPanel.getHeight();
//			backgroundPanel.setLayout(new BorderLayout());
		} catch (IOException e) {
			e.printStackTrace();
		}

		initContentPanel();
	}

	public void initContentPanel() {

		backgroundPanel.removeAll();
		String sexs[] = {"male", "female"};

		centerPanel = new JPanel();
		centerPanel.setLayout(null);

		JLabel label = new JLabel();
		label.setText("<html><h2 style='text-align:center;'>添加VIP用户</h2></html>");
		label.setFont(MyFont.Static);

		username = new JTextField(30);
		username.setBounds(100, 60, 120, 30);
        username.setFont(MyFont.Static);
        //username.setForeground(Color.gray);
        username.addFocusListener(this);
        username.setText("用户名");

		JLabel sex = new JLabel("性别");
		sex.setFont(MyFont.Item);
		sex.setBounds(100, 110, 60, 30);
		
		select_sex = new JComboBox<String>(sexs);
		select_sex.setBounds(180, 110, 100, 30);
		select_sex.addActionListener(this);
        
		birthday = new JTextField(30);
		birthday.setBounds(100, 160, 120, 30);
		birthday.setFont(MyFont.Static);
        //birthday.setForeground(Color.gray);
		birthday.addFocusListener(this);
		birthday.setText("生日(yyyy-mm-dd)");
		
		tel = new JTextField(30);
		tel.setBounds(100, 210, 120, 30);
		tel.setFont(MyFont.Static);
        //tel.setForeground(Color.gray);
		tel.addFocusListener(this);
		tel.setText("手机号");
        
        
		email = new JTextField(30);
		email.setBounds(100, 260, 120, 30);
		email.setFont(MyFont.Static);
        //email.setForeground(Color.gray);
		email.addFocusListener(this);
		email.setText("邮箱");

        button_ok = new JButton("添加");
        button_ok.setBounds(100, 310, 65, 30);
        button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_ok.setForeground(Color.white);
        button_ok.setFont(MyFont.Static);
        button_ok.addMouseListener(this);

        button_reset = new JButton("重置");
        button_reset.setBounds(190, 310, 65, 30);
        button_reset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        button_reset.setForeground(Color.white);
        button_reset.setFont(MyFont.Static);
        button_reset.addMouseListener(this);

		centerPanel.add(label);
		centerPanel.add(username);
		centerPanel.add(sex);
		centerPanel.add(select_sex);
		centerPanel.add(birthday);
		centerPanel.add(tel);
		centerPanel.add(email);

		centerPanel.add(button_ok);
		centerPanel.add(button_reset);

		backgroundPanel.add(centerPanel);
		backgroundPanel.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_ok) {
			user.setUsername(username.getText().toString());
			user.setUserSex(select_sex.getSelectedItem().toString());
			user.setUserBirthday(birthday.getText().toString());
			user.setUserTelnum(tel.getText().toString());
			user.setuserEmail(email.getText().toString());
			new UserSrv().add(user);
			JOptionPane.showMessageDialog(null, "VIP用户插入成功 !!!");
			username.setText("用户名");
			birthday.setText("生日(yyyy-mm-dd)");
			tel.setText("手机号");
			email.setText("邮箱");
		}
		
		if (e.getSource() == button_reset) {
			username.setText("用户名");
			birthday.setText("生日(yyyy-mm-dd)");
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
		} else if (e.getSource() == birthday) {
			if (birthday.getText().equals("生日(yyyy-mm-dd)")) {
				birthday.setText("");
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
		} else if (e.getSource() == birthday) {
			if (birthday.getText().equals("")) {
				birthday.setText("生日(yyyy-mm-dd)");
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

}
