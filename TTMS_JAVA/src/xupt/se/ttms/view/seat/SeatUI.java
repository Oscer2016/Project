package xupt.se.ttms.view.seat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.view.clerk.SaleUI;
import xupt.se.ttms.view.clerk.SellTicket;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Seat;
import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.SeatSrv;
import xupt.se.ttms.service.StudioSrv;

public class SeatUI extends JDialog implements MouseListener, ActionListener {

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	final ImageIcon siteimgwhite = new ImageIcon("image/white.png");
	final ImageIcon siteimggreen = new ImageIcon("image/green.png");
	final ImageIcon siteimgred = new ImageIcon("image/red.png");
	final ImageIcon siteimglock = new ImageIcon("image/lock.png");

	// 定义全局组件
	JPanel backgroundPanel, topPanel, centerPanel, bottomPanel, bottomPanel1, bottomPanel2;
	JScrollPane scrollPanel;
	JButton button_ok, button_cancel;
	
	private int m, n;
	final private int studio_id;
	String studio_name = "";
	int[][] state; 				// 存储座位状态
	int[] seat_id; 				// 存储选中座位的ID
	private Employee emp;
	
	public SeatUI(Employee emp, final int studio_id, String studio_name, int m, int n) {
		
		this.emp = emp;
		this.studio_id = studio_id;
		this.studio_name = studio_name;
		this.m = m;
		this.n = n;
		
		state = new int[m+1][n+1];
		seat_id = new int[m*n];
		
//		System.out.println(m);
//		System.out.println(n);
//		System.out.println(studio_name);

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
//		initialize(10, 7, 10);
		this.setTitle("选择座位");
		this.setSize((int) (width * 0.75f), (int) (height * 0.82f));
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 初始化顶部面板
	public void initTop() {
		
//		topPanel.setPreferredSize(new Dimension(width, 40));
		
		topPanel = new JPanel(new FlowLayout());
		topPanel.setBackground(Color.gray);
		JLabel jLabel = new JLabel(studio_name + "演出厅",SwingConstants.CENTER);
		jLabel.setFont(new Font("微软雅黑 Light", Font.BOLD, 30));
		jLabel.setForeground(Color.blue);	
		topPanel.add(jLabel);
	}

	// 初始化中心面板
	public void initCenterPanel() {
		
		centerPanel = new JPanel(new BorderLayout());
		
		JLabel lmainview = new JLabel();

		ImageIcon selectsite = new ImageIcon("image/seatback.png");
		lmainview.setIcon(selectsite);

		JPanel sites = new JPanel();
		GridLayout gridLayout = new GridLayout(m+1, n+1);
		gridLayout.setHgap(3);
		gridLayout.setVgap(3);
		sites.setLayout(gridLayout);
		
		Action act = new AbstractAction() {
			
			private static final long serialVersionUID = -144569051730123316L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JButton site = (JButton) e.getSource();
				String name = site.getName();

				String tmp[] = name.split(",");
				int i = Integer.valueOf(tmp[0]);
				int j = Integer.valueOf(tmp[1]);
				int t = Integer.valueOf(tmp[2]);

				if (t == 0) {
					if (state[i][j] == 0) {
						site.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
						site.setIcon(siteimggreen);
					} else {
						site.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
						site.setIcon(siteimgwhite);
					}	
					state[i][j] ^= 1;
//					System.out.println(state[i][j]);
				}
			}
		};

		for (int i = 0; i < m+1; i++) {
			for (int j = 0; j < n+1; j++) {	
				if (i == 0) {
					if(j == 0) {
						sites.add(new JLabel("  "));
					} else {
						sites.add(new JLabel("C" + j,SwingConstants.CENTER));
					}
				} else if (j == 0) {
					if (i > 0) {
						sites.add(new JLabel("R" + i,SwingConstants.CENTER));
					}
				} else {
					int status = -2;
					Seat seat = null;
					if (new SeatSrv().Fetch("studio_id = " + studio_id + " and seat_row = " + i + " and seat_column = " + j).size() > 0) {
						
						seat = new SeatSrv().Fetch("studio_id = " + studio_id + " and seat_row = " + i +" and seat_column = " + j).get(0);
						status = seat.getSeatStatus();
						
						System.out.println(status);
						if (status == -1) {
							JButton site = new JButton(act);
							site.setBackground(Color.WHITE);
							site.setIcon(siteimgred);
							site.setName(i + "," + j + "," + status);
							sites.add(site);
						} else if (status == 0) {
							JButton site = new JButton(act);
							site.setBackground(Color.WHITE);
							site.setIcon(siteimgwhite);
							site.setName(i + "," + j + "," + status);
							sites.add(site);
						} else if (status == 1) {
							JButton site = new JButton(act);
							site.setBackground(Color.WHITE);
							site.setIcon(siteimggreen);
							site.setName(i + "," + j + "," + status);
							sites.add(site);
						} else if (status == 2) {
							JButton site = new JButton(act);
							site.setBackground(Color.WHITE);
							site.setIcon(siteimglock);
							site.setName(i + "," + j + "," + status);
							sites.add(site);
						}
					}
					
					if (status == -2) {
						JButton site = new JButton(act);
						site.setBackground(Color.white);
						site.setIcon(siteimgwhite);
						site.setName(i + "," + j + "," + status);
						sites.add(site);
					}
				}
			}	
		}
		
		centerPanel.add(sites);
		scrollPanel = new JScrollPane(centerPanel);
		centerPanel.updateUI();	
	}
	
	// 初始化底部面板
	public void initBottom() {

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(width, 70));
		
		bottomPanel1 = new JPanel();
		bottomPanel1.setPreferredSize(new Dimension(width, 30));
		
		bottomPanel2 = new JPanel();
		bottomPanel2.setPreferredSize(new Dimension(width, 40));
		

		
		JLabel b2 = new JLabel("已售");
		JLabel b1 = new JLabel(siteimggreen);
		b2.setSize(70, 30);
		b1.setSize(70, 30);

		
		JLabel b3 = new JLabel(siteimgred);
		JLabel b4 = new JLabel("损坏");
		b2.setSize(70, 30);
		b1.setSize(70, 30);
		
		
		JLabel b5 = new JLabel(siteimgwhite);
		JLabel b6 = new JLabel("可售");
		b2.setSize(70, 30);
		b1.setSize(70, 30);
		
		JLabel b7 = new JLabel(siteimglock);
		JLabel b8 = new JLabel("锁定");
		b2.setSize(70, 30);
		b1.setSize(70, 30);

		button_ok = new JButton();
		button_ok.setSize(200,50);
//		button_ok.setBounds(200, height, 120, 40);
		button_ok.setFont(MyFont.Static);
		button_ok.setText("确认出票");
		button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_ok.addMouseListener(this);
		
		button_cancel = new JButton();
//		button_cancel.setBounds(400, height, 120, 40);
		button_cancel.setSize(200,50);
		button_cancel.setFont(MyFont.Static);
		button_cancel.setText("重新选择");
		button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_cancel.addMouseListener(this);
		
		bottomPanel1.add(b1);
		bottomPanel1.add(b2);
		bottomPanel1.add(b3);
		bottomPanel1.add(b4);
		bottomPanel1.add(b5);
		bottomPanel1.add(b6);
		bottomPanel1.add(b7);
		bottomPanel1.add(b8);
		
		bottomPanel2.add(button_ok);
		bottomPanel2.add(button_cancel);
		
		bottomPanel.add(bottomPanel1, "Center");
		bottomPanel.add(bottomPanel2, "South");
	}
	
	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());
		
		initTop();
		initCenterPanel();
		initBottom();
		
		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(scrollPanel, "Center");
		backgroundPanel.add(bottomPanel, "South");

		this.add(backgroundPanel);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == button_ok) {
			int t = 0;
			for (int i = 1; i < m+1; i++) {
				for (int j = 1; j < n+1; j++) {
					if (state[i][j] == 1) {
						Seat seat = new SeatSrv().Fetch("studio_id = " + studio_id + " and seat_row = " + i +" and seat_column = " + j).get(0);
						int id = seat.getId();
						seat_id[t++] = id;
						String date = null;
						try{
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							date = df.format(new Date());
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						System.out.println("加锁时间: " + date);
						new SeatSrv().lockSeat(id, date); 	// 加锁座位,设置座位状态为2,即加锁状态
					}
				}
			}
			System.out.println(emp.getEmployee_name());
			new SellTicket(emp, seat_id).handleSale();
//			JOptionPane.showMessageDialog(null, "座位选择成功!!!");
			this.dispose();
		} else {
			this.dispose();
		}
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}	
//	public static void main(String[] args) {
//		new SeatUI(6,"601",4,4);
//	}
}