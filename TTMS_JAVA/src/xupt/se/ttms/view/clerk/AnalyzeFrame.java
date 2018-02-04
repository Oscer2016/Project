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
import xupt.se.ttms.service.SaleSrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.seat.SeatUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;


public class AnalyzeFrame extends JFrame implements MouseListener, ActionListener, DocumentListener {

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
	
	public AnalyzeFrame(Employee emp) {

		this.emp = emp;
		
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

		this.setTitle("个人销售数据分析");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化顶部面板
	public void initTop() {

		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(width, 40));
		
		JLabel title = new JLabel("个人销售数据");
		title.setFont(MyFont.Title);
		
		topPanel.add(title);
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
		button_choose.setText("返回");
		button_choose.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_choose.addMouseListener(this);
		
		bottomPanel.add(button_choose);
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

		String params[] = {"剧目名称", "演出厅名称", "演出时间", "售票时间", "票价"};

		Vector<Vector> vector = new Vector<Vector>();
		vector = new SaleSrv().FetchSale();
		
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
		if (e.getSource() == button_choose) {	
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
