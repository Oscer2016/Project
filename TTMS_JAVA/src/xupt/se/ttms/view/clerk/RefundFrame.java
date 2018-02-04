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
import xupt.se.ttms.service.SaleSrv;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;

public class RefundFrame extends JFrame implements MouseListener, ActionListener, DocumentListener {

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JPanel backgroundPanel, topPanel, bottomPanel, centerPanel, tablePanel, refundTablePanel;
	JTabbedPane jTabbedPane;
	JScrollPane jScrollPane;
	BaseTableModule baseTableModule;
	JTable table;

	JTextField input_name;
	JComboBox<String> select_condition;
	JButton button_search, button_ok, button_cancel;

	List<Studio> list;
	private int sale_id;
	private Employee emp;

	public RefundFrame(Employee emp) {
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

		this.setTitle("退票");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化顶部面板
	public void initTop() {

		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(width, 40));
		
		String conditions[] = {"按用户名", "按剧目名", "按演出厅"};
		
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
		
		button_ok = new JButton();
		button_ok.setBounds(200, height-20, 100, 40);
		button_ok.setFont(MyFont.Static);
		button_ok.setText("退款");
		button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_ok.addMouseListener(this);
		
		button_cancel = new JButton();
		button_cancel.setBounds(400, height-20, 100, 40);
		button_cancel.setFont(MyFont.Static);
		button_cancel.setText("返回");
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_cancel.addMouseListener(this);
		
		bottomPanel.add(button_ok);
		bottomPanel.add(button_cancel);
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

	// 创建全部订单面板
	public void creatTotalOrderTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(MyFont.Static);

		jTabbedPane.addTab("全部订单", tablePanel);				// 全部订单面板
		jTabbedPane.addTab("退款单", refundTablePanel);			// 退款订单面板
		
		centerPanel.add(jTabbedPane, "Center");
	}
		
	// 初始化数据表格面板
	public void initTablePanel() {

		String params[] = {"订单号", "剧目名称", "演出厅名称", "用户名", "消费金额", "订单类型"};

		Vector<Vector> vector = new Vector<Vector>();
		vector = new SaleSrv().Fetch();
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object col_sale_id = baseTableModule.getValueAt(selectedRow, 0);
				sale_id = Integer.parseInt(col_sale_id.toString()); 
			}
		});
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel(); // 获取列模型

		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		tablePanel.add(jScrollPane);
		creatTotalOrderTab();
//		centerPanel.add(tablePanel);
	}
	
	// 初始化数据表格面板
	public void initRefundTablePanel() {

		jScrollPane = new JScrollPane();
		Tools.setJspStyle(jScrollPane);

		refundTablePanel = new JPanel(new BorderLayout());
		refundTablePanel.setOpaque(false);

		refundTablePanel.add(jScrollPane);
		creatTotalOrderTab();
//		centerPanel.add(tablePanel);
	}
	
	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_ok) {	
			try{
				int confirm = JOptionPane.showConfirmDialog(null,"确认退票?", "退票", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					new SaleSrv().Fetch(sale_id); 		// 退款
				}
			} catch (Exception e1) {
//					e1.printStackTrace();
			} finally {	
			}
			JOptionPane.showMessageDialog(null, "退款成功!!!");
			this.dispose();
			new RefundFrame(emp);
		} else if (e.getSource() == button_cancel) {
			this.dispose();
//			JOptionPane.showMessageDialog(null, "!!!");
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