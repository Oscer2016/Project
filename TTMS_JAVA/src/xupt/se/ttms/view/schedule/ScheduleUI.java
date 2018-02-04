package xupt.se.ttms.view.schedule;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;

import com.mysql.fabric.xmlrpc.base.Data;
import com.sun.org.apache.bcel.internal.generic.NEW;
import xupt.se.ttms.model.Schedule;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;

public class ScheduleUI extends JFrame implements ActionListener, MouseListener {

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	//定义全局部件
	private Object studio_name, play_name, schedule_time, price;
	private JButton addbtn, modifybtn, detelebtn, inquerybtn;
	private JPanel schedule_panel, buttonPanel, tablePanel;
	private BaseTableModule baseTableModule;
	private JScrollPane jScrollPane;
	private JTabbedPane tabbedPane;
	private JTextField textField;
	private JTable table;
	private JLabel lable;
	private int id_studio, id_play;
	// 创建对象
	 
	private Schedule schedule, schedule_tmp;
	SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public ScheduleUI() {
		
		schedule_panel = new JPanel();
//		tabbedPane = new JTabbedPane();
//		tabbedPane.addTab("演出计划管理", schedule_panel);
		this.add(schedule_panel);
		
		initButtonPanel();
		initTablePanel();
		
		this.setTitle("TTMS");
		this.setSize((int)(width * 0.7f), (int)(height * 0.77f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//初始化按钮面板
	public void initButtonPanel(){
		schedule_panel.setLayout(new BorderLayout());
		
		addbtn = new JButton("添加");
		modifybtn = new JButton("修改");
		detelebtn = new JButton("删除");
		lable = new JLabel("                                                                     ");
		textField = new JTextField(20);
		textField.setText("输入查询的剧目");
		textField.setFont(MyFont.PLAIN);
		textField.addMouseListener(this);
		inquerybtn = new JButton("查询");
		
		buttonPanel = new JPanel();
		schedule_panel.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(addbtn);
		buttonPanel.add(modifybtn);
		buttonPanel.add(detelebtn);
		buttonPanel.add(lable);
		buttonPanel.add(textField);
		buttonPanel.add(inquerybtn);
		
		//添加监听器
		addbtn.addMouseListener(this);
		detelebtn.addMouseListener(this);
		modifybtn.addMouseListener(this);
		inquerybtn.addMouseListener(this);
		
		schedule  = new Schedule();
		
		
	}
	
	//初始化数据表面板
	private void initTablePanel() {
		String params[] = {"演出厅名称", "剧目名称", "演出时间", "票价"};
		
		Vector<Vector> vector = new Vector<Vector>();		//创建二位数组
		vector = new ScheduleSrv().Showinfo();						//行数据
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object studio_name = baseTableModule.getValueAt(selectedRow, 0);
				Object play_name = baseTableModule.getValueAt(selectedRow, 1);
				Object schedule_time = baseTableModule.getValueAt(selectedRow, 2);
				Object price = baseTableModule.getValueAt(selectedRow, 3);
				
				schedule_tmp = new Schedule();
				schedule_tmp.setPlay_name(play_name.toString());
				schedule_tmp.setStudio_name(studio_name.toString());
				java.util.Date date = null;
				try {
					date = sdf.parse(schedule_time.toString());
				} catch (ParseException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				schedule_tmp.setSched_time(date);
				schedule_tmp.setSched_ticket_price(Double.parseDouble(price.toString()));
//				schedule.setStudio_name(col_schedule(selectedRow,0));
//				setId(Integer.parseInt(col_schedule.toString()));
//				System.out.println(schedule.getSched_id());
			}
		});
		
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		
		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		schedule_panel.add(jScrollPane);
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == addbtn) {
//			System.out.println("===========Add Schedule===========");
			new ScheduleAddUI();
			SwingUtilities.updateComponentTreeUI(table);
		}
		else if (e.getSource() == detelebtn) {
			
			System.out.println("===========Delete Schedule===========");
			try {
				schedule  = new Schedule();
				schedule.setPlay_name(schedule_tmp.getPlay_name());
				schedule.setStudio_name(schedule_tmp.getStudio_name());
				schedule.setSched_time(schedule_tmp.getSched_time());
				schedule.setSched_ticket_price(schedule_tmp.getSched_ticket_price());
				new ScheduleSrv().delete(schedule);
				
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, "选择剧目！！！");
			}
//			System.out.println(schedule.getPlay_name());
//			System.out.println(schedule.getStudio_name());
//			System.out.println(schedule.getSched_time());
//			System.out.println(schedule.getSched_ticket_price());
		}
		else if (e.getSource() == modifybtn) {
			System.out.println("===========Modify Schedule===========");
//			try {
				schedule  = new Schedule();
				schedule.setPlay_name(schedule_tmp.getPlay_name());
				schedule.setStudio_name(schedule_tmp.getStudio_name());
				schedule.setSched_time(schedule_tmp.getSched_time());
				schedule.setSched_ticket_price(schedule_tmp.getSched_ticket_price());
				new ModifyScheduleUI(schedule,id_studio,id_play);
//			} catch (Exception e2) {
//				JOptionPane.showMessageDialog(null, "选择剧目！！！");
//			}
		}
		else if (e.getSource() == inquerybtn) {
//			System.out.println("===========Inquery Schedule===========");
			SwingUtilities.updateComponentTreeUI(table);
			new ScheduleSrv().Fetch(textField.getText());
			this.dispose();
			
		}
		else if (e.getSource() == textField) {
			textField.setText(null);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		
	}
	
	//测试函数
//	public static void main(String[] args) {
//		new ScheduleUI();
//	}
}
