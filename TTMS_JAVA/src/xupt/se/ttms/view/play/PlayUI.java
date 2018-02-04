package xupt.se.ttms.view.play;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.naming.directory.SearchResult;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableColumnModel;

import com.sun.org.apache.bcel.internal.generic.NEW;

import xupt.se.ttms.model.Play;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.view.tmpl.BaseTableModule;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.util.Tools;

public class PlayUI extends JFrame implements MouseListener, ActionListener {

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JButton play_addbtn, play_modbtn, play_delbtn, play_inqubtn;
	JPanel play_panel, schedule_panel, analyise_panel, btnpanel;
	JPanel backgroundPanel,btnPanel,tablePanel;
	BaseTableModule baseTableModule;
	JTabbedPane jTabbedPane;
	JScrollPane jScrollPane;
	JTextField textField;
	JComboBox comboBox ;
	JTable table;
	private Play play = new Play();
	
	public PlayUI() {
		//窗口淡入淡出
		new WindowOpacity(this);
		
		// 设置tab面板缩进
//		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));
		
		play_panel = new JPanel();
		initButtonPanel();
		initTablePanel();
		
		this.add(play_panel);
		this.setTitle("TTMS");
		this.setSize((int)(width * 0.7f), (int)(height * 0.77f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	//初始化剧目信息表
	private void initTablePanel() {
		String params[] = { "剧目ID", "剧目语言","剧目名称", "剧目时长","剧目简介"};

		Vector<Vector> vector = new Vector<Vector>();		//创建二维数组
		vector = new PlaySrv().Fecth();						//行数据
		
		baseTableModule = new BaseTableModule(params, vector);
		table = new JTable(baseTableModule);
		
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object playid = baseTableModule.getValueAt(selectedRow, 0);
				Object playlang = baseTableModule.getValueAt(selectedRow, 1);
				Object playname = baseTableModule.getValueAt(selectedRow, 2);
				Object playtime = baseTableModule.getValueAt(selectedRow, 3);
				Object playintroduction = baseTableModule.getValueAt(selectedRow, 4);
				play.setId(Integer.parseInt(playid.toString()));
				play.setLangId(Integer.parseInt(playlang.toString()));
				play.setName(playname.toString());
				play.setLength(Integer.parseInt(playtime.toString()));
				play.setIntroduction(playintroduction.toString());
			}
		});
		
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		
		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		
		play_panel.add(jScrollPane);
		
	}

	
	// 初始化剧目管理面板
	private void initButtonPanel() {
		play_panel.setLayout(new BorderLayout());
		play_addbtn = new JButton("添加");
		play_modbtn = new JButton("修改");
		play_delbtn = new JButton("删除");
		play_inqubtn = new JButton("查询");
		
		textField = new JTextField(20);
		textField.setText("输入查询的剧目名称");
		textField.addMouseListener(this);
		JLabel label = new JLabel("                                                                     ");		//作为占位符
		String[] string = {"按剧目名","按剧目类型", "按剧目语言"};
		JComboBox comboBox = new JComboBox(string);

		JPanel btnpanel = new JPanel();
		btnpanel.setLayout(new FlowLayout());
		play_panel.add(btnpanel, BorderLayout.NORTH);
		btnpanel.add(play_addbtn);
		btnpanel.add(play_modbtn);
		btnpanel.add(play_delbtn);
		btnpanel.add(label);
		btnpanel.add(textField);
		btnpanel.add(comboBox);
		btnpanel.add(play_inqubtn);	
		play_addbtn.addMouseListener(this);
		play_delbtn.addMouseListener(this);
		play_inqubtn.addMouseListener(this);
		play_modbtn.addMouseListener(this);
		
	}
	
	
	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == play_delbtn) {
			try {
				new PlaySrv().delete(play);
				this.dispose();
				new PlayUI();
//				JOptionPane.showMessageDialog(null, "删除成功！");
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, "删除失败!!!剧目正在上映中...");
			}
			
		} else if (e.getSource() == play_addbtn) {
			try {
				new PlayaddUI(this);
			} catch (Exception e2) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "添加失败！");
			}
		}
		else if (e.getSource() == play_inqubtn) {
			if (textField.getText() != null) {
//				new SearchResult(textField.getText());
			}
		}
		else if (e.getSource() == play_modbtn) {
			new ModifyPlayUI(this, play);
		}
		else if (e.getSource() == textField) {
			textField.setText(null);
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
	
	//测试函数
//	public static void main(String[] args) {
//		 new PlayUI();
//	}
}
