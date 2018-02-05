package xupt.se.ttms.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.iScheduleDAO;
import xupt.se.ttms.model.Play;
import xupt.se.ttms.model.Schedule;
import xupt.se.util.ConnectionManager;
import xupt.se.util.DateUtil;

public class ScheduleDAO implements iScheduleDAO {
	
	public int insert(Schedule sched) {
		int rtn = 0;
		if (sched == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into schedule(studio_id, play_id, sched_time, sched_ticket_price) values(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, sched.getStudio_id());
			pstmt.setInt(2, sched.getPlay_id());
			pstmt.setString(3, DateUtil.dateToStr(sched.getSched_time()));
			pstmt.setDouble(4, sched.getSched_ticket_price());
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}
	
	public int update(Schedule sched) {
		int rtn = 0;
		if (sched == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = " update schedule set studio_id=?, play_id=?, sched_time=?, sched_ticket_price=? where sched_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, sched.getStudio_id());
			pstmt.setInt(2, sched.getPlay_id());
			pstmt.setString(3, DateUtil.dateToStr(sched.getSched_time()));
			pstmt.setDouble(4, sched.getSched_ticket_price());
			pstmt.setInt(5, sched.getSched_id());
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}

	public int delete(int ID) {
		int rtn = 0;
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "delete from  Schedule where sched_id=?";
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

	public List<Schedule> select(String condt) {
		List<Schedule> schedList = null;
		schedList = new LinkedList<Schedule>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select sched_id, studio_id, play_id, sched_time, sched_ticket_price from schedule ";
			condt.trim();
			if (!condt.isEmpty()) {
				sql += " where " + condt;
			}
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				Schedule sched = new Schedule();
				sched.setSched_id(rst.getInt("sched_id"));
				sched.setStudio_id(rst.getInt("studio_id"));
				sched.setPlay_id(rst.getInt("play_id"));
				sched.setSched_time(rst.getTimestamp("sched_time"));
				sched.setSched_ticket_price(rst.getDouble("sched_ticket_price"));
				schedList.add(sched);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
            return schedList;
		}
	}


	@Override
	public List<Schedule> selectPlay(String condt) {
		List<Schedule> schedList = null;
		schedList = new LinkedList<Schedule>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
       	PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select distinct play_id from Schedule ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where " + condt;
			
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
		
			while (rst.next()) {
				Schedule sched = new Schedule();
				sched.setPlay_id(rst.getInt("play_id"));
				schedList.add(sched);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
            return schedList;
		}
	}

	public List<Schedule> myselect(String condt) {
		List<Schedule> schedList = null;
		schedList = new LinkedList<Schedule>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from Schedule ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				Schedule sched = new Schedule();
				sched.setPlay_id(rst.getInt("play_id"));
				schedList.add(sched);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
            return schedList;
		}
	}

	@Override
	public Vector<Vector> selectSched(String condt) {
		
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
//			String sql = "select studio.studio_name, play.play_name, schedule.sched_time, schedule.sched_ticket_price "
//					+ "from play, schedule,studio where schedule.play_id = play.play_id and studio.studio_id = schedule.studio_id;";
			
			String sql = "select schedule.studio_id,studio_name,sched_id,play_name,play_length,sched_time,"
					+ "sched_ticket_price,studio_row_count*studio_col_count as 'total'"
					+ "from schedule,play,studio where schedule.play_id = play.play_id "
					+ "and studio.studio_id = schedule.studio_id";
			if (!condt.isEmpty()) {
				sql += " and " + condt;
			}
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getInt("sched_id"));
				temp.add(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("play_name"));
				temp.add(rst.getInt("play_length"));
				temp.add(rst.getString("sched_time"));
				temp.add(rst.getInt("sched_ticket_price"));
				PreparedStatement ps = null;
				ResultSet res = null;
				try {
					String test = "select count(1) as sold from seat where studio_id=? and seat_status=1";
					ps = con.prepareStatement(test);
					ps.setInt(1, rst.getInt("studio_id"));
					res = ps.executeQuery();
					while (res.next()) {
						temp.add(res.getInt("sold"));
						temp.add(rst.getInt("total")-res.getInt("sold"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(res, ps, null);
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
	public Vector listing() {
		Vector<String> listPlay = new Vector<String>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select play_name from play";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while(rst.next()){
				Play play1=new Play();
				play1.setName(rst.getString("play_name"));
//				System.out.println(play1.getName());
				listPlay.add(play1.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return listPlay;
		}
	}
	
	@Override
	public Vector<Vector> showStuinfo() {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
	
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio.studio_name, play.play_name, schedule.sched_time, schedule.sched_ticket_price"
					+ " from studio, play, schedule "
					+ "where schedule.play_id = play.play_id and studio.studio_id = schedule.studio_id;";

			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("play_name"));
				temp.add(rst.getString("sched_time"));
				temp.add(rst.getInt("sched_ticket_price"));
				
//				temp.add(rst.getString("studio_name"));
//				temp.add(rst.getInt("sched_id"));
//				temp.add(rst.getString("play_name"));
//				temp.add(rst.getInt("play_length"));
//				temp.add(rst.getString("sched_time"));
//				temp.add(rst.getInt("sched_ticket_price"));
//				temp.add(rst.getInt("sold"));
//				temp.add(rst.getInt("remain"));
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
	public int delete(Schedule schedule) {
		int rtn = 0;
		if (schedule == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			/*
			String strsql = "select sched_id, play.play_id, studio.studio_id, sched_time from schedule, play, studio"
					+ " where schedule.play_id = play.play_id and schedule.studio_id = studio.studio_id and sched_time;";
			*/
			String strsql = "select sched_id from schedule where sched_time=?";
			pstmt = con.prepareStatement(strsql);
			pstmt.setString(1, DateUtil.dateToStr(schedule.getSched_time()));
			rst = pstmt.executeQuery();

			String sql = "";
			while (rst.next()) {
				sql += "delete from  schedule where sched_id = " + rst.getInt("sched_id") + ";";
			}
			PreparedStatement ps = null;
			try {
				ps = con.prepareStatement(sql);
				ps.executeUpdate();
				rtn = 1;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(null, ps, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return rtn;
		}
	}
	
//	public static void main(String[] args) {
//		Vector<Vector> vector = new ScheduleDAO().selectSched();
//		System.out.println(vector);
//	}

}
