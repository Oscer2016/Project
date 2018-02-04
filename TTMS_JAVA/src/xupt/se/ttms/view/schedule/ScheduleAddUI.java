package xupt.se.ttms.view.schedule;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.bcel.internal.generic.NEW;

import xupt.se.ttms.model.Play;
import xupt.se.ttms.model.Schedule;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.service.TicketSrv;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.print.attribute.TextSyntax;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class ScheduleAddUI extends JFrame implements ActionListener,MouseListener {
	private JPanel contentPane, tablePanel, schedulePanel;
	private JComboBox<String> play_comboBox, studio_comboBox;
	private JButton addbtn, resetbtn, timebtn;
	private BaseTableModule baseTableModule;
	private JTextField price_textField;
	private JScrollPane jScrollPane;
	private JTable table;
	private int item = 0;
//	private Vector playname, studioname;
	
	//创建对象
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Schedule schedule = new Schedule();
	
	/**
	 * 测试函数
	 */
	public static void main(String[] args) {
		new ScheduleAddUI();
	}

	/**
	 * 创建框架
	 */
	public ScheduleAddUI(){
		
		this.setBounds(450, 100, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		schedulePanel();
//		initTable();
		
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void schedulePanel() {
		
		schedulePanel = new JPanel();
		schedulePanel.setBounds(0, 0, 600, 800);			//565为schedulePanel的最大高度
		
		contentPane.add(schedulePanel);
		schedulePanel.setLayout(null);
		
		JLabel label = new JLabel("添加演出计划");
		schedulePanel.add(label);
		label.setBounds(240, 50, 140, 30);
		label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		
		play_comboBox = new JComboBox<>();
		play_comboBox.setFont(MyFont.PLAIN);
		play_comboBox.setMaximumRowCount(8);
		play_comboBox.setBounds(220, 160, 220, 30);
		schedulePanel.add(play_comboBox);
		initplay_comboBox();
		
		
//		sched_time_textField = new JTextField();
//		sched_time_textField.setFont(MyFont1.PLAIN);
//		sched_time_textField.setBounds(150, 260, 220, 30);
//		sched_time_textField.setText("YYYY-MM-DD hh:mm:ss");
//		sched_time_textField.setToolTipText("请输入完整的时间");
//		schedulePanel.add(sched_time_textField);
//		sched_time_textField.setColumns(10);
		
		price_textField = new JTextField();
		price_textField.setFont(MyFont.PLAIN);
		price_textField.setBounds(220, 310, 220, 30);
		schedulePanel.add(price_textField);
		price_textField.setColumns(10);
		
		JLabel label1 = new JLabel("剧目名称");
		label1.setFont(MyFont.PLAIN);
		label1.setBounds(100, 160, 90, 30);
		schedulePanel.add(label1);
		
		JLabel label2 = new JLabel("演出厅名称");
		label2.setFont(MyFont.PLAIN);
		label2.setBounds(100, 230, 90, 30);
		schedulePanel.add(label2);
		
		JLabel label3 = new JLabel("时间安排");
		timebtn = new DateChooserJButton();
		timebtn.setFont(MyFont.PLAIN);
		timebtn.setBounds(220, 380, 220, 30);
		schedulePanel.add(timebtn);
//		timebtn.setText(timebtn.getText());
		
		label3.setFont(MyFont.PLAIN);
		label3.setBounds(100, 380, 120, 30);
		schedulePanel.add(label3);
		
		JLabel label4 = new JLabel("票      价");
		label4.setFont(MyFont.PLAIN);
		label4.setBounds(100, 310, 90, 30);
		schedulePanel.add(label4);
		
		addbtn = new JButton("添加");
		addbtn.setBounds(200, 450, 60, 25);
		schedulePanel.add(addbtn);
		addbtn.addMouseListener(this);
		
		resetbtn = new JButton("重置");
		resetbtn.setBounds(350, 450, 60, 25);
		schedulePanel.add(resetbtn);
		resetbtn.addMouseListener(this);
		
		JTextField textField = new JTextField();
		textField.setFont(MyFont.PLAIN);
		textField.setBounds(150, 200, 220, 30);
		
		studio_comboBox = new JComboBox<>();
		studio_comboBox.setFont(MyFont.PLAIN);
		studio_comboBox.setMaximumRowCount(8);
		studio_comboBox.setBounds(220, 230, 220, 30);
		schedulePanel.add(studio_comboBox);
		initstudio_comboBox(item);
		studio_comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				contentPane.remove(tablePanel);
//				contentPane.add(tablePanel);
//				contentPane.repaint();
			}
		});
		
		
//		studio_comboBox.addItemListener(new ItemListener() {
			
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				SwingUtilities.updateComponentTreeUI(table);				
//				
//			}
//		});
	}
	
	//初始化演出厅下拉框
//	@SuppressWarnings("unused")
	private void initstudio_comboBox(int ID) {
		Vector studioname = new Vector<>();
		studioname = new PlaySrv().listingStudio(item);
		for (int i = 0; i < studioname.size(); i++) {
			 studio_comboBox.addItem(studioname.elementAt(i).toString());
			
		}
		
	}
	//初始化剧目下拉框
	private void initplay_comboBox() {
		Vector playname = new Vector<>();
		playname = new ScheduleSrv().listing();
		for (int i = 0; i < playname.size(); i++) {
			play_comboBox.addItem(playname.elementAt(i).toString());
		}
	}

	public void initTable(){
		
		JLabel label = new JLabel("当前演出厅演出计划");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		label.setBounds(600, 50, 200, 30);
		contentPane.add(label);
		
		String params[] = {"演出厅名称", "演出剧目", "演出时间", "票价"};
		Vector<Vector> vector = new Vector<Vector>();		//创建二位数组
		vector = new ScheduleSrv().Showinfo();					//行数据
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		
		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel();
		tablePanel.setOpaque(false);
		tablePanel.setBounds(400, 120, 600, 400);
		tablePanel.setLayout(new BorderLayout(0, 0));
		
		tablePanel.add(jScrollPane);
		contentPane.add(tablePanel);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == resetbtn) {
			price_textField.setText(null);
		}
		else if (e.getSource() == addbtn) {
			try {
				schedule.setPlay_id(play_comboBox.getSelectedIndex()+1);
				schedule.setSched_ticket_price(Double.parseDouble(price_textField.getText()));
				schedule.setStudio_id(studio_comboBox.getSelectedIndex()+1);
				try {
					schedule.setSched_time(sdf.parse(timebtn.getText()));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				new ScheduleSrv().add(schedule);
				createTicket();
				this.dispose();new Schedule();
				JOptionPane.showMessageDialog(null, "剧目添加成功！！！");
				
				
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, "剧目添加失败！！！");
			}
		}
		else if (e.getSource() == timebtn) {
			new DateChooserJButton();
		}
		
	}
	public void createTicket(){
		new TicketSrv().CreateTicket(schedule.getStudio_id());
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	public void ItemListener(ItemListener e){
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		
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
}
