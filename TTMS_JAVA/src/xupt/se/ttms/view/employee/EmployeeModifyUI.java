package xupt.se.ttms.view.employee;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Play;
import xupt.se.ttms.service.EmployeeSrv;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.service.StudioSrv;
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

public class EmployeeModifyUI extends JFrame implements ActionListener , MouseListener {

	private Employee emp = new Employee();
	private JPanel contentPane;
	private JTextField name, passwd, position, salary, tel;
	private JButton button_cancel, button_add;
	JComboBox<String> select_sex, select_position;

	JFrame frame;

	
	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	public EmployeeModifyUI(JTable table,AdminUI frame) {
		this.frame=frame;
		Object idObject = table.getValueAt(table.getSelectedRow(), 0);//读取ID 
        final int idInt = Integer.parseInt(String.valueOf(idObject));
        Employee employee = new Employee();
        new EmployeeSrv().FetchEm(idInt,employee);
		String positions[] = {"clerk", "manager"};
		String sexs[] = {"male", "female"};
		System.out.println(">>>"+idInt+employee.getEmployee_name());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("修改员工信息");
		label.setFont(MyFont.Title);
		label.setBounds(130, 20, 134, 30);
		contentPane.add(label);
		
		name = new JTextField();
		name.setBounds(70, 70, 225, 30);
		name.setText(employee.getEmployee_name());
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel label_1 = new JLabel("姓名");
		label_1.setFont(MyFont.Item);
		label_1.setBounds(20, 70, 60, 20);
		contentPane.add(label_1);


		passwd = new JTextField();
		passwd.setBounds(70, 110, 225, 30);
		passwd.setText(employee.getEmployee_passwd());
		contentPane.add(passwd);
		passwd.setColumns(10);
		
		JLabel label_2 = new JLabel("密码");
		label_2.setFont(MyFont.Item);
		label_2.setBounds(20, 110, 60, 30);
		contentPane.add(label_2);
		
		select_position = new JComboBox<String>(positions);
		select_position.setBounds(70, 150, 150, 30);
		select_position.setToolTipText(employee.getEmployee_position());
		contentPane.add(select_position);
		select_position.addActionListener(this);
		
		JLabel label_3 = new JLabel("职位");
		label_3.setFont(MyFont.Item);
		label_3.setBounds(20, 150, 60, 30);
		contentPane.add(label_3);
		
		salary = new JTextField();
		salary.setBounds(70, 190, 225, 30);
		salary.setText(String.valueOf(employee.getEmployee_salary()));
		contentPane.add(salary);
		
		JLabel label_4 = new JLabel("薪资");
		label_4.setFont(MyFont.Item);
		label_4.setBounds(20, 190, 60, 30);
		contentPane.add(label_4);
		
		tel = new JTextField();
		tel.setBounds(70, 230, 225, 30);
		tel.setText(employee.getEmployee_tel());
		contentPane.add(tel);
		tel.setColumns(10);
		
		JLabel label_5 = new JLabel("电话");
		label_5.setFont(MyFont.Item);
		label_5.setBounds(20, 230, 60, 30);
		contentPane.add(label_5);

		
		button_add = new JButton("确定");
		button_add.setBounds(65, 320, 72, 27);
		button_add.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		button_add.setForeground(Color.white);
		button_add.setFont(MyFont.Static);
		button_add.addMouseListener(this);
		
		button_cancel = new JButton("取消");
		button_cancel.setBounds(185, 320, 72, 27);
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_cancel.setForeground(Color.white);
		button_cancel.setFont(MyFont.Static);
		button_cancel.addMouseListener(this);
		

		contentPane.add(button_cancel);
		contentPane.add(button_add);
		
		this.setSize((int)(width * 0.27f), (int)(height * 0.62f));
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_add) {
			emp.setEmployee_name(name.getText().toString());
			emp.setEmployee_passwd(passwd.getText().toString());
			emp.setEmployee_position(select_position.getSelectedItem().toString());
			emp.setEmployee_salary(Integer.parseInt(salary.getText().toString()));
			emp.setEmployee_tel(tel.getText().toString());
		
			
			new EmployeeSrv().modify(emp);
			JOptionPane.showMessageDialog(null, "员工信息修改成功 !!!");
//			new EmployeeUI();
//			this.jframe.refreshTablePanel();
//			this.jframe.dispose();
			new AdminUI();
			this.frame.dispose();
			this.dispose();
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
//	
//	public static void main(String[] args) {
//		new EmployeeModifyUI();
//	}
}
