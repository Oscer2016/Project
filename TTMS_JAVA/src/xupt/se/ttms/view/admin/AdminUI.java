package xupt.se.ttms.view.admin;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.omg.PortableServer.THREAD_POLICY_ID;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xml.internal.security.Init;
import com.sun.xml.internal.bind.v2.runtime.Name;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.EmployeeSrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.employee.EmployeeAddUI;
import xupt.se.ttms.view.employee.EmployeeModifyUI;
import xupt.se.ttms.view.employee.SelectEm;
import xupt.se.ttms.view.seat.SeatMgrUI;
import xupt.se.ttms.view.studio.StudioAddUI;
import xupt.se.ttms.view.studio.StudioDeleteUI;
import xupt.se.ttms.view.studio.StudioModifyUI;
import xupt.se.ttms.view.studio.StudioSelectNameUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.DBUtil;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Studio;

public class AdminUI extends JFrame implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;
	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	
	protected JPanel panelleft = new JPanel();	
	protected JPanel panelcenter = new JPanel();
	private JPanel panelright = new JPanel();
	public JTable tableleft;
	public JScrollPane scrollPane;
	protected JTabbedPane tp;
	
	private JTextField textadmin;
	JComboBox jcb;
	protected JTree tree = new JTree();;
	int studio_id;
	protected JButton emp_addbtn;
	protected JButton emp_modbtn, emp_delbtn, emp_inqubtn;
	protected JTextField textField;
	protected BaseTableModule baseTableModule;
	protected  JTable table;
	Boolean Visible = true;
	protected JScrollPane jScrollPane;
	protected JComboBox comboBox;
	
	protected  int emp_id;
 
	public AdminUI() {
		//窗口淡入淡出
		new WindowOpacity(this);
		
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

		try {
			Image imgae = ImageIO.read(new File("image/logo.png"));
			this.setIconImage(imgae);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		panelleft.setLayout(null); 
		
		
		tp = new JTabbedPane();
		this.setContentPane(tp);
		
		
		tp.setPreferredSize(new Dimension(500,200));
		tp.setTabPlacement(JTabbedPane.TOP);
		tp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		
		panel1topdd();//管理演出厅
		panel2topdd();
		panel3dictionary();	//字典管理
		tp.addTab("panel1", panelleft);
		tp.setEnabledAt(0,true);
		tp.setTitleAt(0, "演出厅管理");
		tp.addTab("panelcenter", panelcenter);
		tp.setEnabledAt(1,true);
		tp.setTitleAt(1, "人员管理");
		tp.addTab("panelright", panelright);
		tp.setEnabledAt(2,true);
		tp.setTitleAt(2, "字典管理");
	}
	/**
	 * 字典管理
	 */
	public void panel3dictionary(){
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
            	do_DicTree_windowActivated(e);
            }
        });
		
		panelright.add(tree);
		
	}
	/**
	 * 种树
	 */
	protected void  do_DicTree_windowActivated(WindowEvent e){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根");
		DefaultMutableTreeNode type = new DefaultMutableTreeNode("类型");
		DefaultMutableTreeNode language = new DefaultMutableTreeNode("语言");
		type.add(new DefaultMutableTreeNode("古装剧"));
		type.add(new DefaultMutableTreeNode("悬疑剧"));
		type.add(new DefaultMutableTreeNode("喜剧"));
		type.add(new DefaultMutableTreeNode("生活剧"));
		type.add(new DefaultMutableTreeNode("惊悚剧"));
		type.add(new DefaultMutableTreeNode("动作剧"));
		
		language.add(new DefaultMutableTreeNode("汉语"));
		language.add(new DefaultMutableTreeNode("英语"));
		language.add(new DefaultMutableTreeNode("日语"));
		language.add(new DefaultMutableTreeNode("中英双语"));
		
		root.add(language);
		root.add(type);
		DefaultTreeModel model = new DefaultTreeModel(root);
	    tree.setModel(model);
	}
	/**
	 * 人员管理
	 */
	public void panel2topdd(){		
		initButtonPanel();
		initTablePanel();
	}
	public void initTablePanel() {
		panelcenter.setLayout(null);
		String params[] = {"员工ID", "员工姓名", "员工性别", "员工职位",  "员工薪资", "员工电话"};
		
		Vector<Vector> vector = new Vector<Vector>();		//创建二位数组
		vector = new EmployeeSrv().FetchEmp();				//行数据
		
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
	}

	
	// 初始化剧目管理面板
	public void initButtonPanel() {
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
		String[] string = {"","根据用户名查询","根据职位查询"};
		comboBox = new JComboBox<String>(string);
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
	}
	
	
	// 鼠标点击事件
	
	public void mouseClicked(MouseEvent e) {
		Object position = table.getValueAt(table.getSelectedRow(), 3);
		if (e.getSource() == emp_delbtn) {
			if(position.equals("admin")){
				JOptionPane.showConfirmDialog(null, "您没有此权限！！", "警告", JOptionPane.YES_NO_OPTION);
			}else{
				int confirm = JOptionPane.showConfirmDialog(null, "确认删除所选员工？", "删除", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					new EmployeeSrv().delete(emp_id);	
					this.dispose();
					new AdminUI();
				}
			}
		} else if (e.getSource() == emp_addbtn) {
			new EmployeeAddUI().AddUser(this);
		} else if (e.getSource() == emp_inqubtn) {
			String str1 = textField.getText();
        	String jcbStr = (String) comboBox.getSelectedItem();
			new SelectEm(str1,jcbStr);
			this.dispose();
		} else if (e.getSource() == emp_modbtn) {
			if(position.equals("admin")){
				JOptionPane.showConfirmDialog(null, "您没有此权限！！", "警告", JOptionPane.YES_NO_OPTION);
			}else{
				new EmployeeModifyUI(table,this);
			}
		}
	}

	
	
	
	/**
	 * 演出厅管理界面
	 */
	public void panel1topdd(){
		
		String[] columnNames = {"演出厅ID","演出厅名称","演出厅地点","座位行数","座位列数","备注"};
		Vector<Vector> vector = new Vector<Vector>();
		vector = new StudioSrv().Fetch();
		
		baseTableModule = new BaseTableModule(columnNames, vector);
		tableleft = new JTable(baseTableModule);
	    tableleft.setAutoCreateRowSorter(true);
	    Tools.setTableStyle(tableleft);
	    scrollPane = new JScrollPane(tableleft);
	    Tools.setJspStyle(scrollPane);
		
		//设置表格的属性
		tableleft.setFont(MyFont.TableBody);
		tableleft.setRowHeight(30);
		JTableHeader header= tableleft.getTableHeader();
		header.setFont(MyFont.TableHeader);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		
		//设置表格的列宽
		tableleft.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableleft.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableleft.getColumnModel().getColumn(2).setPreferredWidth(200);
		tableleft.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableleft.getColumnModel().getColumn(4).setPreferredWidth(150);
		tableleft.getColumnModel().getColumn(5).setPreferredWidth(400);
		
			
		JButton StudioFirst= new JButton("首页");
		StudioFirst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					new AdminUI();
					Dispose();
            }
        });
		
		JButton StudioAdd= new JButton("添加");
		StudioAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	studioAdd();
					
            }
        });
		
		JButton StudioUpdate= new JButton("修改");
		tableleft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		StudioUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudioModifyUI(tableleft);
//                Dispose();
            }
        });
	
		
		JButton buttonDeleteStudio = new JButton("删除");
		buttonDeleteStudio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new StudioDeleteUI(tableleft);
            	Dispose();
            }
        });
		
		JButton buttonSize = new JButton("座位管理");
		buttonSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	seatMgrUI();
            	
            }
        });
		textadmin = new JTextField(15);
		String string[] = {"", "名称", "地点"};  
        jcb = new JComboBox(string); 

		JButton buttonSelect= new JButton("查询");
		buttonSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String str1 = textadmin.getText();
            	String jcbStr = (String) jcb.getSelectedItem();
                new StudioSelectNameUI(str1,jcbStr);
                Dispose();
            }
        });
		StudioFirst.setBounds(70,30,50,30);
		panelleft.add(StudioFirst);
		StudioAdd.setBounds(140,30,50,30);
		panelleft.add(StudioAdd); 
		StudioUpdate.setBounds(210,30,50,30);
		panelleft.add(StudioUpdate); 
		buttonDeleteStudio.setBounds(280,30,50,30);
		panelleft.add(buttonDeleteStudio);
		buttonSize.setBounds(350,30,90,30);
		panelleft.add(buttonSize);
		textadmin.setBounds(650,30,120,30);
		panelleft.add(textadmin);
		jcb.setBounds(800,30,80,30);
		panelleft.add(jcb);
		buttonSelect.setBounds(900,30,50,30);
		panelleft.add(buttonSelect);
		scrollPane.setBounds(0,90,1035,400);
		panelleft.add(scrollPane);
		
		this.setTitle("Admin");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void seatMgrUI(){
		new SeatMgrUI(this);
	}
	public void studioAdd(){
		new StudioAddUI(this);
	}
	public void Dispose() {
		this.dispose();
	}
	

	
	// 鼠标划入事件
	 
	public void mouseEntered(MouseEvent e) {
		
	}

	// 鼠标划出事件
	 
	public void mouseExited(MouseEvent e) {
		
	}

	 
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	 
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	 
	public void actionPerformed(ActionEvent e) {

	}
	
//	public static void main(String [] args){
//		new AdminUI();
//	}
}

