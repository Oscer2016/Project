package xupt.se.ttms.view.seat;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.sun.awt.AWTUtilities;

import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.tmpl.ImagePanel;
import xupt.se.util.MyFont;
import xupt.se.ttms.view.tmpl.WindowOpacity;


public class ChangeStatus extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	// 全局的位置变量，用于表示鼠标在窗口上的位置
	//	static Point origin = new Point();

	// 定义全局组件
	JComboBox<String> select_statu;
	ImagePanel backgroundPanel = null;
	JButton button_ok, button_cancel;
	JLabel label_position, label_title;
	
	private String studio_name;
	private int studio_id;
	private int row;
	private int col;

	public ChangeStatus(int studio_id, String studio_name, int row, int col) {
		
		//窗口淡入淡出
		new WindowOpacity(this);
		
		this.studio_id = studio_id;
		this.studio_name = studio_name;
		this.row = row;
		this.col = col;
		
		Image backgrounImage = null;
		String status[] = {"正常", "损坏", "安排座位", "移除座位"};
		
//		try {
//			backgrounImage = ImageIO.read(new File("image/main.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// 窗口背景面板
		backgroundPanel = new ImagePanel(backgrounImage);
		backgroundPanel.setLayout(new FlowLayout());
		
		label_title = new JLabel(studio_name);
		label_title.setFont(MyFont.Title);
		label_title.setBounds(74, 10, 150, 30);
		
		label_position = new JLabel("座位: " + row + " 行 " + col + " 列");
		label_position.setFont(MyFont.Item);
		label_position.setBounds(50, 50, 150, 30);
		
		select_statu = new JComboBox<String>(status);
		select_statu.setSize(350,30);
//		select_statu.setBounds(10, 100, 220, 30);
		select_statu.addActionListener(this);

		button_ok = new JButton("确认");
		button_ok.setBounds(40, 160, 70, 30);
		button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_ok.setForeground(Color.white);
		button_ok.setFont(MyFont.Static);
		button_ok.addMouseListener(this);

		button_cancel = new JButton("放弃");
		button_cancel.setBounds(120, 160, 70, 30);
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_cancel.setForeground(Color.white);
		button_cancel.setFont(MyFont.Static);
		button_cancel.addMouseListener(this);

		backgroundPanel.add(label_title);
		backgroundPanel.add(label_position);
		backgroundPanel.add(select_statu);
		backgroundPanel.add(button_ok);
		backgroundPanel.add(button_cancel);

		this.add(backgroundPanel);
		this.setTitle("座位修改");
		this.setSize((int)(width * 0.13f), (int)(height * 0.26f));
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	// 鼠标点击事件
	@SuppressWarnings("deprecation")
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_ok) {
			String statu = select_statu.getSelectedItem().toString();
			if (statu.equals("正常")) {
				new StudioSrv().changeSeat(studio_id, row, col, 0);
			} else if (statu.equals("损坏")) {
				new StudioSrv().changeSeat(studio_id, row, col, 1);
			} else if (statu.equals("安排座位")) {
				new StudioSrv().changeSeat(studio_id, row, col, 2);
			} else if (statu.equals("移除座位")) {
				new StudioSrv().changeSeat(studio_id, row, col, 3);
			}
			JOptionPane.showMessageDialog(null, "操作成功!!!");
			this.dispose();
		} else if (e.getSource() == button_cancel) {
			this.dispose();
		}

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
//	public static void main(String[] args) {
//		new ChangeStatus("601演出厅", 3, 4);
//	}
}

