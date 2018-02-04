package xupt.se.ttms.view.clerk;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;


import xupt.se.util.MyFont;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Sale;
import xupt.se.ttms.model.Ticket;
import xupt.se.ttms.model.User;
import xupt.se.ttms.service.SaleSrv;
import xupt.se.ttms.service.SeatSrv;
import xupt.se.ttms.service.TicketSrv;
import xupt.se.ttms.service.UserSrv;
import xupt.se.ttms.view.tmpl.WindowOpacity;

public class SaleUI extends JDialog implements MouseListener, ActionListener, FocusListener {

    private static final long serialVersionUID = 1L;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    // 定义全局组件
    JTextField price = new JTextField(16);
    JTextField vipNum = new JTextField(20);
    
    JPanel backgroundPanel;
    JLabel title, play_name, sched_time, studio_info, seat, amount,
    		play_price, price_total, price_paid, price_change, payee, 
    		paytime, sale_item, user_type;
    JButton button_ok, button_cancel;
    JComboBox<String> select_type;
    
    private Employee emp;
    private User user;
    private double percent = 0;
    int[] seat_id;
    String item = null; 		// 存储票的信息
	String date = null; 		// 存储当前时间
	double totalPrice;			// 总价
	double price_pay; 			// 支付金额
	List<Ticket> curTickets;

    public SaleUI(Employee emp, int[] seat_id) {
    	
    	this.emp = emp;
//    	System.out.println(emp.getEmployee_name());
    	this.seat_id = new int[seat_id.length];
		System.arraycopy(seat_id, 0, this.seat_id, 0, seat_id.length);
		curTickets = new LinkedList<Ticket>();
		curTickets = new SellTicket(emp, seat_id).addTickets();
		item = new SellTicket(emp, seat_id).getSaleItem(curTickets);
		totalPrice = curTickets.get(0).getPrice()*curTickets.size();
//		System.out.println(item);
		
		String types[] = {"临时用户", "会员用户"};
    	
        //窗口淡入淡出
        new WindowOpacity(this);

        // 窗口背景面板
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        
		sale_item = new JLabel(item);
		sale_item.setFont(MyFont.Item);
		sale_item.setBounds(10, 5, 220, 510);
		
		user_type = new JLabel("用户类型: ");
		user_type.setFont(MyFont.Item);
		user_type.setBounds(10, 310, 90, 30);
		
		select_type = new JComboBox<String>(types);
		select_type.setBounds(90, 310, 120, 30);
		select_type.addActionListener(this);
		
        vipNum.setBounds(10, 350, 200, 30);
        vipNum.setText("会员帐号(临时用户可忽略)");
        vipNum.setFont(MyFont.Static);
        vipNum.addFocusListener(this);
        
		price_paid = new JLabel("实收: ");
		price_paid.setFont(MyFont.Item);
		price_paid.setBounds(10, 390, 90, 50);
        price.setBounds(55, 400, 160, 30);
        price.setFont(MyFont.Static);

        button_ok = new JButton("结算");
        button_ok.setBounds(80, (int) (height * 0.82f)-100, 70, 30);
        button_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_ok.setForeground(Color.white);
        button_ok.setFont(MyFont.Static);
        button_ok.addMouseListener(this);

        button_cancel = new JButton("取消");
        button_cancel.setBounds(200, (int) (height * 0.82f)-100, 70, 30);
        button_cancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        button_cancel.setForeground(Color.white);
        button_cancel.setFont(MyFont.Static);
        button_cancel.addMouseListener(this);
        
        backgroundPanel.add(price_paid);
        backgroundPanel.add(price);
        backgroundPanel.add(sale_item);
        backgroundPanel.add(user_type);
        backgroundPanel.add(select_type);
        backgroundPanel.add(vipNum);
        backgroundPanel.add(button_ok);
        backgroundPanel.add(button_cancel);

        this.add(backgroundPanel);
        this.setTitle("销售单");
        this.setSize((int) (width * 0.30f), (int) (height * 0.86f));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == button_ok) {
        	for (int i = 0; i < seat_id.length; i++) {    	
        		if (seat_id[i] != 0) {
        			int id = seat_id[i];
//        			System.out.println(id);
        			price_pay = Double.parseDouble(price.getText());
    				new SeatSrv().unlockSeat(id); 		// 解锁座位,设置座位状态为1,已售
    				Ticket ticket = new Ticket();
    				ticket = new TicketSrv().Fetchone("seat_id = " + id);
    				ticket.setStatus(9); 				
    				new TicketSrv().modify(ticket); 	// 更改票的状态为9,已售
    				
    				String user_type = select_type.getSelectedItem().toString();
//    				"临时用户", "会员用户"
    				
    				Sale sale = new Sale();
    				
    				if (user_type.equals("会员用户")) {
    					String num = vipNum.getText();
    					String rank = null;
    					user = new User();
    					
    					try {
    						user = new UserSrv().Fetch("user_tel_num = " + num);
    						rank = user.getUserRank();
    					} catch (Exception ex) {
    						ex.printStackTrace();
    					}
    					//System.out.println("----->" + user.getUserId());
    					
    					if (rank.equals("VIP1")) {
    						percent = 1;
    					} else if (rank.equals("VIP2")) {
    						percent = 0.95;
    					} else if (rank.equals("VIP3")) {
    						percent = 0.90;
    					} else if (rank.equals("VIP4")) {
    						percent = 0.85;
    					} else if (rank.equals("VIP5")) {
    						percent = 0.80;
    					} else if (rank.equals("VIP6")) {
    						percent = 0.75;
    					} else if (rank.equals("VIP7")) {
    						percent = 0.70;
    					}
    					sale.setUserId(user.getUserId());
    				} else if (user_type.equals("临时用户")) {
    					percent = 1;
    					sale.setUserId(1);
    				} 
    				
    				sale.setEmpId(emp.getId());
    				sale.setTime(new Date());
    				sale.setPayment((float) (price_pay));
    				sale.setChange((float) (price_pay-totalPrice*percent));
    				new SaleSrv().add(curTickets, sale);
    				
    	        	JOptionPane.showMessageDialog(null, "应找零 ￥" + (price_pay-totalPrice*percent));
    	        	this.dispose();
        		}
        	}
		} else if (e.getSource() == button_cancel) {
			for (int i = 0; i < seat_id.length; i++) {    	
        		if (seat_id[i] != 0) {
        			int id = seat_id[i];
    				new SeatSrv().unlockSeat(id); 		// 解锁座位,设置座位状态为0,待售
    	        	this.dispose();
        		}	
        	}
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
//		vipNum.setText("会员帐号（临时用户可忽略）");
		if (e.getSource() == vipNum) {
			if (vipNum.getText().equals("会员帐号(临时用户可忽略)")) {
				vipNum.setText("");
			}
		} 
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == vipNum) {
			if (vipNum.getText().equals("")) {
				vipNum.setText("会员帐号(临时用户可忽略)");
			}
		} 
	}

	
//	public static void main(String[] args) {
//		int[] seat_id = new int[2];
//		seat_id[0]=1;
//		Employee emp = new Employee();
//		new SaleUI(emp, seat_id);
//	}
}