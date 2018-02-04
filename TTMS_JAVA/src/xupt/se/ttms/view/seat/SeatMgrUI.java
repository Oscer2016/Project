package xupt.se.ttms.view.seat;

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
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.seat.SeatUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;

public class SeatMgrUI extends JDialog implements MouseListener, ActionListener, DocumentListener {

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
	JButton button_search, button_choose, button_back;
	JLabel studio_total, studio_noseat;
	
	List<Studio> list;
	JFrame frame;
	public SeatMgrUI(AdminUI frame) {
		this.frame = frame;
		
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

		this.setTitle("演出厅座位管理");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化顶部面板
	public void initTop() {

		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(width, 40));
		
		String[] nameStrings = { "所有演出厅", "未安排座位演出厅" };

		studio_total = CreateMenuLabel(studio_total, nameStrings[0], "studio_total", topPanel);
		studio_total.setName("studio_total");
		studio_noseat = CreateMenuLabel(studio_noseat, nameStrings[1], "studio_noseat", topPanel);
		studio_noseat.setName("studio_noseat");
		
		String conditions[] = {"按名称", "按地点"};
		
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

	// 创建顶部菜单Label
	public JLabel CreateMenuLabel(JLabel jlb, String text, String name, JPanel who) {
		JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
//			Icon icon = new ImageIcon("image/" + name + ".png");
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
		button_choose.setBounds(200, height-20, 150, 40);
		button_choose.setFont(MyFont.Static);
		button_choose.setText("选择");
		button_choose.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_choose.addMouseListener(this);
		
		button_back = new JButton();
		button_back.setBounds(400, height-20, 100, 40);
		button_back.setFont(MyFont.Static);
		button_back.setText("返回");
		button_back.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_back.addMouseListener(this);
		
		bottomPanel.add(button_choose);
		bottomPanel.add(button_back);
	}
	
	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());
		initTop();
		initBottom();
		initCenterPanel();
		initTablePanel();

		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");
		backgroundPanel.add(bottomPanel, "South");

		this.add(backgroundPanel);
	}

	// 初始化数据表格面板
	public void initTablePanel() {

		String params[] = {"演出厅", "演出厅名称", "地点", "座位行数", "座位列数", "座位状况"};

		Vector<Vector> vector = new Vector<Vector>();
		vector = new StudioSrv().FetchAll("");
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object col_studio_id = baseTableModule.getValueAt(selectedRow, 0);
				studio_id = Integer.parseInt(col_studio_id.toString()); 
			}
		});
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型

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
		if (e.getSource() == studio_total) {
			System.out.println("hp");
		} else if (e.getSource() == studio_noseat) {
			System.out.println("ph");
		} else if (e.getSource() == button_choose) {	
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
				JOptionPane.showMessageDialog(null, "请选择演出厅!!!");
			} else {
				new SeatModifyUI(studio_id, studio_name, row, col);
			}
		} else if (e.getSource() == button_back) {
			this.dispose();
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