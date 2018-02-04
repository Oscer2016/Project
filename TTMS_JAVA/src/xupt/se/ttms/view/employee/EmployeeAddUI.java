package xupt.se.ttms.view.employee;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Play;
import xupt.se.ttms.service.EmployeeSrv;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class EmployeeAddUI extends JFrame implements ActionListener , MouseListener {
	
	public EmployeeAddUI() {
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Employee emp = new Employee();
	private JPanel contentPane;
	private JTextField workid, name, passwd, sex, position, salary, tel, address;
	private JButton button_cancel, button_add;
	JComboBox<String> select_sex, select_position;
	JFrame frame;
//	JTextArea textArea;
	
//	public static void main(String[] args) {
//		new EmployeeAddUI();
//	}
	
	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	public void AddUser(AdminUI frame) {		
		this.frame = frame;
		String positions[] = {"clerk", "manager"};
		String sexs[] = {"male", "female"};
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("添加员工");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		label.setBounds(140, 20, 104, 30);
		contentPane.add(label);
		
		workid = new JTextField();
		workid.setBounds(70, 70, 240, 30);
		contentPane.add(workid);
		workid.setColumns(10);
		
		JLabel label_1 = new JLabel("工号");
		label_1.setFont(MyFont.Item);
		label_1.setBounds(20, 70, 60, 20);
		contentPane.add(label_1);


		name = new JTextField();
		name.setBounds(70, 110, 240, 30);
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel label_2 = new JLabel("姓名");
		label_2.setFont(MyFont.Item);
		label_2.setBounds(20, 110, 60, 20);
		contentPane.add(label_2);

		passwd = new JTextField();
		passwd.setBounds(70, 150, 240, 30);
		contentPane.add(passwd);
		passwd.setColumns(10);
		
		JLabel label_3 = new JLabel("密码");
		label_3.setFont(MyFont.Item);
		label_3.setBounds(20, 150, 60, 20);
		contentPane.add(label_3);
		
//		sex = new JTextField();
//		sex.setBounds(70, 190, 240, 30);
//		contentPane.add(sex);
//		sex.setColumns(10);
		
		select_sex = new JComboBox<String>(sexs);
		select_sex.setBounds(70, 190, 150, 30);
		contentPane.add(select_sex);
		select_sex.addActionListener(this);
		
		JLabel label_4 = new JLabel("性别");
		label_4.setFont(MyFont.Item);
		label_4.setBounds(20, 190, 60, 20);
		contentPane.add(label_4);
		
//		position = new JTextField();
//		position.setBounds(70, 230, 240, 30);
//		contentPane.add(position);
//		position.setColumns(10);
		
		select_position = new JComboBox<String>(positions);
		select_position.setBounds(70, 230, 150, 30);
		contentPane.add(select_position);
		select_position.addActionListener(this);
		
		JLabel label_5 = new JLabel("职位");
		label_5.setFont(MyFont.Item);
		label_5.setBounds(20, 230, 60, 20);
		contentPane.add(label_5);
		
		salary = new JTextField();
		salary.setBounds(70, 270, 240, 30);
		contentPane.add(salary);
		
		JLabel label_6 = new JLabel("薪资");
		label_6.setFont(MyFont.Item);
		label_6.setBounds(20, 270, 60, 20);
		contentPane.add(label_6);
		
		tel = new JTextField();
		tel.setBounds(70, 310, 240, 30);
		contentPane.add(tel);
		tel.setColumns(10);
		
		JLabel label_7 = new JLabel("电话");
		label_7.setFont(MyFont.Item);
		label_7.setBounds(20, 310, 60, 20);
		contentPane.add(label_7);
		
		address = new JTextField();
		address.setBounds(70, 350, 240, 30);
		contentPane.add(address);
		address.setColumns(10);
		
		JLabel label_8 = new JLabel("地址");
		label_8.setFont(MyFont.Item);
		label_8.setBounds(20, 350, 60, 20);
		contentPane.add(label_8);
		
		button_add = new JButton("确定");
		button_add.setBounds(65, 450, 72, 27);
		button_add.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		button_add.setForeground(Color.white);
		button_add.setFont(MyFont.Static);
		button_add.addMouseListener(this);
		
		button_cancel = new JButton("取消");
		button_cancel.setBounds(185, 450, 72, 27);
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_cancel.setForeground(Color.white);
		button_cancel.setFont(MyFont.Static);
		button_cancel.addMouseListener(this);
		

		contentPane.add(button_cancel);
		contentPane.add(button_add);
		
		this.setSize((int)(width * 0.28f), (int)(height * 0.75f));
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

	 
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_add) {
			this.dispose();
			emp.setEmployee_workid(workid.getText().toString());
			emp.setEmployee_name(name.getText().toString());
			emp.setEmployee_passwd(passwd.getText().toString());
			emp.setEmployee_sex(select_sex.getSelectedItem().toString());
			emp.setEmployee_position(select_position.getSelectedItem().toString());
			emp.setEmployee_salary(Integer.parseInt(salary.getText().toString()));
			emp.setEmployee_tel(tel.getText().toString());
			emp.setEmployee_address(address.getText().toString());
			
			new EmployeeSrv().add(emp);
			JOptionPane.showMessageDialog(null, "员工插入成功 !!!");
			
//			new EmployeeUI();
//			this.jframe.refreshTablePanel();
			this.frame.dispose();
			new AdminUI();
//			getParent().setVisible(true);
		}
		
		if (e.getSource() == button_cancel) {
			this.dispose();
		}
	}

	
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		
	}
	
}
