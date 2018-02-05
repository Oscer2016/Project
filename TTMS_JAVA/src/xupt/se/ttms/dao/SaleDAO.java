package xupt.se.ttms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.xml.crypto.Data;

import xupt.se.ttms.idao.iSaleDAO;
import xupt.se.ttms.model.Sale;
import xupt.se.ttms.model.SaleItem;
import xupt.se.ttms.model.Seat;
import xupt.se.ttms.model.Studio;
import xupt.se.ttms.model.Ticket;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.util.ConnectionManager;
import xupt.se.util.DateUtil;

public class SaleDAO implements iSaleDAO {

	@Override
	public boolean doSale(List<Ticket> tickets, Sale sale) {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        
		try {
			int id = -1;
			int rtn = -1;

			// 生成销售记录
	        String sqlSale = "insert into sale(emp_id, user_id, sale_time, sale_payment, sale_change, sale_type, sale_status) VALUES(?,?,?,?,?,1,1)";  
	        System.out.println(sqlSale);
	        PreparedStatement prep = con.prepareStatement(sqlSale, PreparedStatement.RETURN_GENERATED_KEYS);  ;
	        prep.setInt(1, sale.getEmpId());  
	        prep.setInt(2, sale.getUserId()); 
	        prep.setTimestamp(3, new Timestamp(new Date().getTime()));  
	        prep.setFloat(4, sale.getPayment());  
	        prep.setFloat(5, sale.getChange());
	        
	        prep.executeUpdate();  
	        ResultSet rs = prep.getGeneratedKeys();
	        if (rs.next()) {  
	            id = rs.getInt(1);  
	        }

	        // 生成账单
	        if (id > 0) {
	        	for (Ticket t : tickets) {
		        	double price = t.getPrice();
			        String sqlSaleItem = "insert into sale_item(ticket_id, sale_ID, sale_item_price) values(?,?,?)";
			        pstmt = con.prepareStatement(sqlSaleItem);
			        pstmt.setInt(1, t.getId());
			        pstmt.setInt(2, id);
			        pstmt.setDouble(3, price);
			       
			        pstmt.executeUpdate();
		        }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(null, pstmt, con);
		}
		return true;
	}

	@Override
	public boolean doRefund(int id) {
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String modsql = "update sale set sale_type = -1 where sale_id = " + id;
//			String delsql = "delete from sale_item where sale_id = " + id;
			System.out.println(modsql);
			
			pstmt = con.prepareStatement(modsql);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			 ConnectionManager.close(null, pstmt, con);
		}
		return true;
	}
	
	@Override
	public int update(Sale sale) {
		int rtn = 0;
		if (sale == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			
			String sql = "update sale set emp_id=?, sale_payment=?, sale_change=?, sale_type=?,"
					+ "sale_time=?, sale_status=? where sale_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, sale.getEmpId());
			pstmt.setFloat(2, sale.getPayment());
			pstmt.setFloat(3, sale.getChange());
			pstmt.setInt(4, sale.getType());
			pstmt.setString(5, DateUtil.dateToStr(sale.getTime()));
			pstmt.setInt(6, sale.getStatus());
			pstmt.setInt(7, sale.getId());
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}

	@Override
	public int delete(int ID) {
		int rtn = 0;	
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "delete from sale where sale_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ID);
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}	
	}
	
	@Override
	public List<Sale> select(String condt){
		List<Sale> saleList = null;
		saleList = new LinkedList<Sale>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from sale ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where sale_id=" + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			
			while (rst.next()) {
				Sale sale = new Sale();
				sale.setId(rst.getInt("sale_id"));
				sale.setEmpId(rst.getInt("emp_id"));
				sale.setTime(rst.getTimestamp("sale_time"));
				sale.setPayment(rst.getFloat("sale_payment"));
				sale.setChange(rst.getFloat("sale_change"));
				sale.setStatus(rst.getInt("sale_status"));
				sale.setType(rst.getInt("sale_type"));
				saleList.add(sale);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
            return saleList;
		}
	}

	@Override
	public boolean doSale(List<Ticket> tickets) {
		return false;
	}

	@Override
	public List<SaleItem> findSaleItemById(String condt) {
		List<SaleItem> saleItemList = null;
		saleItemList = new LinkedList<SaleItem>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from sale_item ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where sale_ID=" + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				SaleItem saleItem = new SaleItem();
				saleItem.setId(rst.getInt("sale_item_id"));
				saleItem.setTicketId(rst.getInt("ticket_id"));
				saleItem.setSaleId(rst.getInt("sale_ID"));
				saleItem.setPrice(rst.getFloat("sale_item_price"));
				
				saleItemList.add(saleItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return saleItemList;
		}

	}
	
	@Override
	public Vector<Vector> selectOrder() {
		
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select sale.sale_ID, play_name, studio_name, user_name, sale_item_price, sale_type "
					+ "from sale, play, studio, schedule, ticket, user, sale_item where "
					+ "sale_item.sale_ID=sale.sale_id and sale_item.ticket_id=ticket.ticket_id "
					+ "and ticket.sched_id=schedule.sched_id and schedule.play_id=play.play_id and "
					+ "schedule.studio_id=studio.studio_id and sale.user_id = user.user_id";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getInt("sale.sale_ID"));
				temp.add(rst.getString("play_name"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("user_name"));
				temp.add(rst.getString("sale_item_price"));
				int s = rst.getInt("sale_type");
				
				if (s == 1) {
					temp.add("销售单");
				} else if (s == -1) {
					temp.add("退款单");
				}
	
//				System.out.println(temp);
				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
//				System.out.println("-->" + t);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return vector;
		}
	}

	@Override
	public Vector<Vector> selectSale() {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select distinct(play_name), studio_name,sched_time, sale_time,"
					+ "sched_ticket_price, emp_name from play,studio,schedule,sale,sale_item, "
					+ "ticket,employee where sale.sale_ID=sale_item.sale_ID and "
					+ "ticket.sched_id=schedule.sched_id and schedule.studio_id=studio.studio_id "
					+ "and schedule.play_id=play.play_id and sale.emp_id=employee.emp_id;";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getString("play_name"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("sched_time"));
				temp.add(rst.getString("sale_time"));
				temp.add(rst.getFloat("sched_ticket_price"));
				temp.add(rst.getString("emp_name"));
	
//				System.out.println(temp);
				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
//				System.out.println("-->" + t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
            return vector;
		}
	}
	
//	public static void main(String[] args) {
//		Vector<Vector> vector = new SaleDAO().selectOrder();
//		System.out.println(vector);
//	}
}
