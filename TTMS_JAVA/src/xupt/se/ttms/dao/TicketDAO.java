package xupt.se.ttms.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.border.TitledBorder;

import xupt.se.ttms.idao.iTicketDAO;
import xupt.se.ttms.model.Ticket;
import xupt.se.util.ConnectionManager;

public class TicketDAO implements iTicketDAO {
	
	@Override
	public int insert(Ticket ticket) {
		int rtn = 0;
		if (ticket == null) {
			return rtn;
		}
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
 		try {
			String sql = "insert into ticket (seat_id, sched_id, ticket_price, ticket_status) values(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ticket.getSeatId());
			pstmt.setInt(2, ticket.getScheduleId());
			pstmt.setDouble(3, ticket.getPrice());
			pstmt.setInt(4, 1);
			
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
	public int update(Ticket ticket) {
		int rtn = 0;
		if (ticket == null) {
			return rtn;
		}
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update ticket set ticket_status = 9 where ticket_id=" + ticket.getId();
//			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			
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
			String sql = "delete from ticket where ticket_id=" + ID;
			pstmt = con.prepareStatement(sql);
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
	public List<Ticket> select(String condt) {
		List<Ticket> ticketList = null;
		ticketList = new LinkedList<Ticket>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from ticket ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				
				Ticket ticket = new Ticket();
				ticket.setId(rst.getInt("ticket_id"));
				ticket.setSeatId(rst.getInt("seat_id"));
				ticket.setScheduleId(rst.getInt("sched_id"));
				ticket.setPrice(rst.getFloat("ticket_price"));
				ticket.setStatus(rst.getInt("ticket_status"));
				ticket.setLocked_time(rst.getTimestamp("ticket_locked_time"));
				ticketList.add(ticket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return ticketList;
        }
	}
	
	@Override
	public Ticket selectone(String condt) {
		
		Ticket ticket = new Ticket();
		Connection con = ConnectionManager.getInstance().getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rst = null;
		try {
			String sql = "select distinct play_name, ticket_id, sched_time, sched_ticket_price, studio_name, seat_row, seat_column "
					+ "from play,schedule,ticket,studio,seat where schedule.sched_id = ticket.sched_id and "
					+ "schedule.studio_id = studio.studio_id and schedule.play_id = "
					+ "play.play_id and ticket." + condt + " and seat." + condt + ";";	
//			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {	
//				System.out.println(rst.getInt("ticket_id"));
				ticket.setId(rst.getInt("ticket_id"));
				ticket.setPlayName(rst.getString("play_name"));
				ticket.setScheduleTime(rst.getString("sched_time"));
				ticket.setStudioName(rst.getString("studio_name"));
				ticket.setPrice(rst.getDouble("sched_ticket_price"));
				ticket.setSeatRow(rst.getInt("seat_row"));
				ticket.setSeatCol(rst.getInt("seat_column"));
				
//				System.out.println(ticket.getPlayName());
//				System.out.println(ticket.getScheduleTime());
//				System.out.println(ticket.getStudioName());
//				System.out.println(ticket.getPrice());
//				System.out.println(ticket.getSeatRow());
//				System.out.println(ticket.getSeatCol());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return ticket;
		}
	}

	@Override
	public int lockTicket(int ID, String time) {
		int rtn = 0;
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update ticket set ticket_status=1, ticket_locked_time=? where ticket_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, time);
			pstmt.setInt(2, ID);

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
	public int unlockTicket(int ID) {
		int rtn = 0;
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update ticket set ticket_status=0 where ticket_id=" + ID;
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}
	
	public boolean legalselect(Ticket ticket) {
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from ticket where seat_id=? and and sched_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ticket.getSeatId());
			pstmt.setInt(2, ticket.getScheduleId());
			
			rst = pstmt.executeQuery();
			if(rst.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
		}
		return true;
	}


	public List<Ticket> myselect(int id,String date) {
		List<Ticket> stuList = null;
		stuList = new LinkedList<Ticket>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select ticket_id,seat_id,ticket.sched_id,ticket_price from ticket,schedule "
					+ "where ticket.sched_id=schedule.sched_id and schedule.studio_id=? and sched_time=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setString(2, date);
			
			rst = pstmt.executeQuery();
			while (rst.next()) {
				Ticket stu = new Ticket();
				stu.setId(rst.getInt("ticket_id"));
				stu.setSeatId(rst.getInt("seat_id"));
				stu.setScheduleId(rst.getInt("sched_id"));
				stu.setPrice(rst.getFloat("ticket_price"));
				//stu.setStatus(rst.getInt("ticket_status"));
				//stu.setLocked_time(rst.getTimestamp("ticket_locked_time"));
				stuList.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}
	}
	
	public Ticket myselectoneticket(int seat_id,int sched_id) {
        Ticket ticket = new Ticket();
        
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from ticket where seat_id=? and sched_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seat_id);
			pstmt.setInt(2, sched_id);

			rst = pstmt.executeQuery();
			while (rst.next()) {
				Ticket stu = new Ticket();
				stu.setId(rst.getInt("ticket_id"));
				stu.setSeatId(rst.getInt("seat_id"));
				stu.setScheduleId(rst.getInt("sched_id"));
				stu.setPrice(rst.getFloat("ticket_price"));
				stu.setStatus(rst.getInt("ticket_status"));
				//stu.setLocked_time(rst.getTimestamp("ticket_locked_time"));
				return stu;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
		}
		return null;
	}
	
	public void temp() {
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String seatSql = "select seat_id from seat where studio_id = 4";
			System.out.println(seatSql);
			pstmt = con.prepareStatement(seatSql);
			
			rst = pstmt.executeQuery();
			int seat_id;
			while (rst.next()) {
				seat_id = rst.getInt("seat_id");
		        PreparedStatement ps = null;
		        try {
		        	String sql = "insert into ticket values(null,?,18,60,0,null)";
		        	ps = con.prepareStatement(sql);
		        	ps.setInt(1, seat_id);
		        	ps.executeUpdate();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        } finally {
		        	 ConnectionManager.close(null, ps, null);
		        }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
		}		
	}
	
	@Override
	public Object createticket(int ID) {
		Ticket ticket = new Ticket();
		 
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from seat where studio_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ID);
			
			rst = pstmt.executeQuery();
//			System.out.println("已完成try查询块1--->"+sql);
			while (rst.next()) {
				try {
					Ticket ticket2 = new Ticket();
					System.out.println("执行try块2--->");
					ticket2.setScheduleId(rst.getInt("sched_id"));
					ticket2.setSeatId(rst.getInt("seat_id"));
					ticket2.setPrice(rst.getFloat("ticket_price"));
					
					if (rst.getInt("seat_status") == 0) {	
						PreparedStatement ps1 = null;
						PreparedStatement ps2 = null;
						ResultSet rs = null;
						try {
							String strsql = "select * from schedule where studio_id=" + ID;
							ps1 = con.prepareStatement(strsql);
							
							rs = ps1.executeQuery();
							
							String sqlstr = "insert into ticket values(null,?,?,?,0,null)";
							ps2 = con.prepareStatement(sqlstr);
							ps2.setInt(1, rst.getInt("seat_id"));
							ps2.setInt(2, rst.getInt("sched_id"));
							ps2.setDouble(3, rst.getFloat("ticket_price"));
							
							ps2.executeUpdate();
							
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							ConnectionManager.close(rs, ps1, null);
							ConnectionManager.close(null, ps2, null);
						}
//						addTicket(sched_id, seat_id, price);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
				return ticket;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		new TicketDAO().selectone("seat_id = 1"); 
//		new TicketDAO().temp(); 
//	}
	
}
