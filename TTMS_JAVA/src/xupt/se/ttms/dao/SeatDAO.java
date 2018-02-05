package xupt.se.ttms.dao;

import java.util.LinkedList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.iSeatDAO;
import xupt.se.ttms.model.Seat;
import xupt.se.util.ConnectionManager;

public class SeatDAO implements iSeatDAO {
	
	@Override
	public int insert(Seat seat) {
		int rtn = 0;
		if (seat == null) {
			return rtn;
		}
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into seat (studio_id, seat_row, seat_column, seat_status) values(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seat.getStudioId());
			pstmt.setInt(2, seat.getRow());
			pstmt.setInt(3, seat.getColumn());
			pstmt.setInt(4, seat.getSeatStatus());
			
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
	public int update(Seat seat) {
		int rtn = 0;
		if (seat == null) {
			return rtn;
		}
		
		Connection con = ConnectionManager.getInstance().getConnection();
	    PreparedStatement pstmt = null;
		try {
			String sql = "update seat set studio_id=?, seat_row=?, seat_column=?, seat_status=? where seat_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seat.getStudioId());
			pstmt.setInt(2, seat.getRow());
			pstmt.setInt(3, seat.getColumn());
			pstmt.setInt(4, seat.getSeatStatus());
			pstmt.setInt(5, seat.getId());

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
	public int update(int studio_id, int row, int col) {
		int rtn = 0;
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update seat set seat_status=1 where studio_id=? and seat_row=? and seat_column=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, studio_id);
			pstmt.setInt(2, row);
			pstmt.setInt(3, col);

			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
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
			String sql = "delete from seat where seat_id=?";
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
	public List<Seat> select(String condt) {
		List<Seat> stuList = null;
		stuList = new LinkedList<Seat>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select * from seat ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				Seat stu = new Seat();
				stu.setId(rst.getInt("seat_id"));
				stu.setStudioId(rst.getInt("studio_id"));
				stu.setRow(rst.getInt("seat_row"));
				stu.setColumn(rst.getInt("seat_column"));
				stu.setSeatStatus(rst.getInt("seat_status"));
				stuList.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}
	}

	public int getSeatStatus(Seat seat){
		int status = -2;
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select seat_status from seat where seat_id=" + seat.getId();
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			status = rst.getInt("seat_status");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return status;
		}
	}

	@Override
	public int lockSeat(int id, String date) {
		int rtn = 0;
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update seat set seat_status=2, seat_locked_time=? where seat_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setInt(2, id);
			
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
	public int unlockSeat(int iD) {
		int rtn = 0;
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update seat set seat_status = 0, seat_locked_time = null where seat_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, iD);
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}

	
//	public static void main(String[] args) {
//		new SeatDAO().update(1, 1, 1);
//	}
}