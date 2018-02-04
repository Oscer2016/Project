package xupt.se.ttms.view.employee;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.service.EmployeeSrv;
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.util.Tools;

public class SelectEm extends AdminUI{
	
	
	public SelectEm(String string, String jcb){
		panelcenter.removeAll();
		panelcenter.setLayout(null);
		
		emp_addbtn = new JButton("添加");
		emp_addbtn.setBounds(70,30,50,30);
		emp_modbtn = new JButton("修改");
		emp_modbtn.setBounds(140,30,50,30);
		emp_delbtn = new JButton("删除");
		emp_delbtn.setBounds(210,30,50,30);
		emp_inqubtn = new JButton("查询");
		emp_inqubtn.setBounds(900,30,50,30);
		
		textField = new JTextField(20);
		textField.setBounds(600,30,100,30);
		textField.setText("");
		String[] strings = {" ","根据用户名查询","根据职位查询"};
		comboBox = new JComboBox<String>(strings);
		comboBox.setBounds(730,30,150,30);
		panelcenter.add(emp_addbtn);
		panelcenter.add(emp_modbtn);
		panelcenter.add(emp_delbtn);
		panelcenter.add(textField);
		panelcenter.add(comboBox);
		panelcenter.add(emp_inqubtn);	
		panelcenter.setVisible(true);
		emp_addbtn.addMouseListener(this);
		emp_delbtn.addMouseListener(this);
		emp_inqubtn.addMouseListener(this);
		emp_modbtn.addMouseListener(this);	
		Vector<Vector> vector = new Vector<Vector>();
		
		if(jcb.equals("根据用户名查询") || jcb.equals("根据职位查询")){
			
			vector = new EmployeeSrv().FetchEmPloyee(string);
			panelcenter.setLayout(null);
			String params[] = {"员工ID", "员工姓名", "员工性别", "员工职位",  "员工薪资", "员工电话"};
			System.out.println(vector);
			baseTableModule = new BaseTableModule(params, vector);
			table = new JTable(baseTableModule);
			
			table.addMouseListener(new MouseAdapter() { // 鼠标事件
				public void mouseClicked(MouseEvent e) {
					int selectedRow = table.getSelectedRow(); // 获得选中行索引
					Object employe_id = baseTableModule.getValueAt(selectedRow, 0);
					emp_id = Integer.parseInt(employe_id.toString());
					System.out.println("emp_id " + emp_id);
				}
			});

			Tools.setTableStyle(table);
			
			jScrollPane = new JScrollPane(table);
			Tools.setJspStyle(jScrollPane);
			
			jScrollPane.setBounds(0,90,1035,400);
			panelcenter.add(jScrollPane);
			
		}else{
			JOptionPane.showConfirmDialog(null,"请选择查找方式", "【警告】", JOptionPane.YES_NO_OPTION);
		}
	}
}
