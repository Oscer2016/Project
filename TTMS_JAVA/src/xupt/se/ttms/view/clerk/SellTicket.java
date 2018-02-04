package xupt.se.ttms.view.clerk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Ticket;
import xupt.se.ttms.service.TicketSrv;
import xupt.se.ttms.view.clerk.SaleUI;


public class SellTicket {
	
	private Employee emp;
	int[] seat_id; 			// 存储座位ID
	private double price; 	// 总票价
	
	public SellTicket() {
	}

	
	public SellTicket(Employee emp, int[] seat_id) {
		this.emp = emp;
//		System.out.println(emp.getEmployee_name());
		this.seat_id = new int[seat_id.length];
		System.arraycopy(seat_id, 0, this.seat_id, 0, seat_id.length);
	}

	
	public double getTotalPrice() {
		return price;
	}
	
	public List<Ticket> addTickets() {
		
		List<Ticket> curTickets = new LinkedList<Ticket>();
		
		for (int i = 0; i < seat_id.length; i++) {
			if (seat_id[i] != 0) {
//				System.out.println(new TicketSrv().Fetchone("seat_id = " + seat_id[i]).getPlayName());
				curTickets.add(new TicketSrv().Fetchone("seat_id = " + seat_id[i]));
			}	
		}
//		System.out.println(curTickets.get(0).getPlayName());
		return curTickets;
	}
	
	public String getSaleItem(List<Ticket> curTickets) {
		
		int i = 0; 		//票数
		
		String item = "<html>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; FY 票<br><br>";
		item += "剧目名称：" + curTickets.get(0).getPlayName() + "<br>";
		item += "演出时间：" + curTickets.get(0).getScheduleTime().substring(0,16) + "<br>";
		item += "演出地点：" + curTickets.get(0).getStudioName() + "<br>座位: ";
		
		for (Ticket t: curTickets) {
			item += "&emsp;" + t.getSeatRow() + "&nbsp;&nbsp;行&nbsp;&nbsp;" + t.getSeatCol() + "&nbsp;&nbsp;列<br>";
			i++;
		}
		
		if (curTickets.size() > 0){
			price = curTickets.get(0).getPrice();
			item += "票数: " + i + "<br>";
			item += "票价：" + price + " 元<br>";
			item += "应收: " + i*price + " 元<br>";
//			item += "找零: " + price_change + " 元\n";
			item += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>收款人: " + emp.getEmployee_name() + "<br>";

			String date = null;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				date = df.format(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			item += "收款时间: " + date + "<br></html>";
			System.out.println(date);
		}
		return item;
	}

	public void handleSale() {
		SaleUI handleSaleUI = new SaleUI(emp, seat_id);	
		handleSaleUI.toFront();
		handleSaleUI.setModal(true);
	}
}