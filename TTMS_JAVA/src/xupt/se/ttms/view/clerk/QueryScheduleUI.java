package xupt.se.ttms.view.clerk;

import java.awt.*;
import java.util.List;
import java.util.Vector;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableColumnModel;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Studio;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.seat.SeatUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;


public class QueryScheduleUI extends JFrame implements MouseListener, ActionListener, DocumentListener {

	private int studio_id, row, col;
	String studio_name = "";

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JPanel backgroundPanel, topPanel, bottomPanel, centerPanel, tablePanel;
	JTabbedPane jTabbedPane;
	JScrollPane jScrollPane;
	BaseTableModule baseTableModule;
	JTable table;

	JTextField input_name;
	JComboBox<String> select_condition;
	JButton button_search, button_choose, button_sale;
	
	List<Studio> list;


	// 定义用户对象
	private Employee emp;
	private String con;
	public QueryScheduleUI(Employee emp, String con) {

		this.emp = emp;
		this.con = con;
		
		new WindowOpacity(this);
		
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

		try {
			Image imgae = ImageIO.read(new File("image/logo.png"));
			this.setIconImage(imgae);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initBackgroundPanel();

		this.setTitle("查询演出计划");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化顶部面板
	public void initTop() {

		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(width, 40));
		
		String conditions[] = {"按剧目名称", "按演出厅名称"};
		
		input_name = new JTextField(20);
		input_name.setBounds(130, 20, 200, 33);
		input_name.getDocument().addDocumentListener(this);

		select_condition = new JComboBox<String>(conditions);
		select_condition.setBounds(350, 20, 90, 33);
		select_condition.addActionListener(this);
		
		button_search = new JButton();
		//button_search.setIcon(new ImageIcon("resource/image/search.png"));
		button_search.setBounds(440, 20, 80, 28);
		button_search.setFont(MyFont.Static);
		button_search.setText("查询");
		button_search.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_search.addMouseListener(this);
		
		topPanel.add(input_name);
		topPanel.add(select_condition);
		topPanel.add(button_search);
	}

	// 初始化中心面板
	public void initCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(false);
	}
	
	// 初始化底部面板
	public void initBottom() {

		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(width, 50));
		
		button_choose = new JButton();
		button_choose.setBounds(200, height-20, 180, 40);
		button_choose.setFont(MyFont.Static);
		button_choose.setText("选择座位");
		button_choose.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_choose.addMouseListener(this);
		
//		button_sale = new JButton();
//		button_sale.setBounds(400, height-20, 100, 40);
//		button_sale.setFont(MyFont.Static);
//		button_sale.setText("出票");
//		button_sale.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
//		button_sale.addMouseListener(this);
		
		bottomPanel.add(button_choose);
//		bottomPanel.add(button_sale);
	}
	
	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());
		initTop();
		initBottom();
		initCenterPanel();
		initTablePanel(con);

		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");
		backgroundPanel.add(bottomPanel, "South");

		this.add(backgroundPanel);
	}

	// 初始化数据表格面板
	public void initTablePanel(String condt) {

		String params[] = {"演出计划ID", "演出厅ID", "演出厅名称", "剧目名称", "剧目时长", "演出时间", "票价",  "已售", "剩余"};

		Vector<Vector> vector = new Vector<Vector>();
		vector = new ScheduleSrv().FetchSched(condt);
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object col_studio_id = baseTableModule.getValueAt(selectedRow, 1);
				studio_id = Integer.parseInt(col_studio_id.toString()); 
			}
		});
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
//		dcm.getColumn(0).setMinWidth(0); // 将第1列的最小宽度、最大宽度都设置为0
//		dcm.getColumn(0).setMaxWidth(0);
//		dcm.getColumn(2).setMinWidth(0); // 将第3列的最小宽度、最大宽度都设置为0
//		dcm.getColumn(2).setMaxWidth(0);


		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		tablePanel.add(jScrollPane);
		centerPanel.add(tablePanel);
	}
	
	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_choose) {	
			try{
				String condt = "studio_id=" + studio_id;
				list = new StudioSrv().Fetch(condt);

				Studio stu = list.get(0);
				row = stu.getRowCount();
				col = stu.getColCount();
				studio_name = stu.getName();
			} catch (Exception e1) {
//				e1.printStackTrace();
			}
			
			if (list.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请选择演出计划!!!");
			} else {
				new SeatUI(emp, studio_id, studio_name, row, col);
			}
		} else if (e.getSource() == button_search) {
			String condt = select_condition.getSelectedItem().toString();
			String name = input_name.getText();
			String condition = "";
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "查询内容为空!!!");
				this.dispose();
				new QueryScheduleUI(emp, "");
			} else {
				if (condt.equals("按剧目名称")) {
					condition = "play.play_name = '" + name + "'";
				} else if (condt.equals("按演出厅名称")) {
					condition = "studio.studio_name = '" + name + "'";;
				}
			
	//			System.out.println(condition);
				this.dispose();
				new QueryScheduleUI(emp, condition);
	//			JOptionPane.showMessageDialog(null, "!!!");
			}
		}
	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	// 鼠标划出事件
	@Override
	public void mouseExited(MouseEvent e) {
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

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}	
}
