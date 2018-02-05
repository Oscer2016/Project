package xupt.se.ttms.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.iStudioDAO;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Seat;
import xupt.se.ttms.model.Studio;
import xupt.se.util.ConnectionManager;


public class StudioDAO implements iStudioDAO {
	
	SeatDAO seatDAO = new SeatDAO();
	
	@Override
	public int insertSeat(Studio stu){
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into seat(studio_id, seat_row, seat_column,seat_status) values(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, stu.getID());
			pstmt.setInt(2, stu.getRowCount());
			pstmt.setInt(3, stu.getColCount());
			pstmt.setInt(4, stu.getFlag());
			
			pstmt.executeUpdate();
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            // 关闭连接
            ConnectionManager.close(null, pstmt, con);
            return rtn;
        }
	}
	
	@Override
	public int insert(Studio stu) {
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into studio(studio_name,studio_position, studio_row_count, studio_col_count, studio_introduction, studio_flag) values(?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, stu.getName());
			pstmt.setString(2, stu.getPosition());
			pstmt.setInt(3, stu.getRowCount());
			pstmt.setInt(4, stu.getColCount());
			pstmt.setString(5, stu.getIntroduction());
			pstmt.setInt(6, stu.getStudioFlag());
			
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
	public int update(Studio stu) {
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update studio set studio_name =?, studio_row_count=?, studio_col_count=?,"
					+ " studio_introduction=?, studio_flag=? where studio_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, stu.getName());
			pstmt.setInt(2, stu.getRowCount());
			pstmt.setInt(3, stu.getColCount());
			pstmt.setString(4, stu.getIntroduction());
			pstmt.setInt(5, stu.getStudioFlag());
			pstmt.setInt(6, stu.getID());
			
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
		try{
			String sql = "delete from studio where studio_id=?";
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
	public List<Studio> select(String condt) {
		List<Studio> stuList = null;
		stuList=new LinkedList<Studio>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id, studio_name, studio_row_count, studio_col_count, studio_introduction , studio_flag from studio ";
			condt.trim();
			if(!condt.isEmpty())
				sql+= " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while(rst.next()){
				Studio stu = new Studio();
				stu.setID(rst.getInt("studio_id"));
				stu.setName(rst.getString("studio_name"));
				stu.setRowCount(rst.getInt("studio_row_count"));
				stu.setColCount(rst.getInt("studio_col_count"));
				stu.setIntroduction(rst.getString("studio_introduction"));
				stu.setFlag(rst.getInt("studio_flag"));
				stuList.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}
	}
	
	public Studio myselect(String condt) {
		Studio stu = new Studio();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id, studio_name, studio_row_count, studio_col_count, studio_introduction , studio_flag from studio ";
			condt.trim();
			if(!condt.isEmpty())
				sql+= " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while(rst.next()){
				stu.setID(rst.getInt("studio_id"));
				stu.setName(rst.getString("studio_name"));
				stu.setRowCount(Integer.parseInt(rst.getString("studio_row_count")));
				stu.setColCount(Integer.parseInt(rst.getString("studio_col_count")));
				stu.setIntroduction(rst.getString("studio_introduction"));
				stu.setStudioFlag(rst.getInt("studio_flag"));
				//System.out.println(Integer.parseInt(rst.getString("studio_row_count"))+ "  "+rst.getInt("studio_row_count")+ "  "+ stu.getColCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stu;
		}
	}

	@Override
	public int createSeats(Studio stu) {
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
		Seat seat = new Seat();
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {	
			for (int i = 1; i <= stu.getRowCount(); i++) {
				for(int j = 1; j <= stu.getColCount(); j++) {
					seat.setRow(i);
					seat.setColumn(j);
					seat.setSeatStatus(1);
					seatDAO.insert(seat);
				} 
			}
			rtn = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}
	
	@Override
	public Vector<Vector> selectStudio(String condt) {
		
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
//			String sql = "select studio_name,studio_position,studio_row_count,studio_col_count,studio_flag";
//			System.out.println(sql);
			
			String sql = "select * from studio ";
			if (!condt.isEmpty()) {
				sql += "where " + condt;
			}
			
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("studio_position"));
				temp.add(rst.getInt("studio_row_count"));
				temp.add(rst.getInt("studio_col_count"));
				int t = rst.getInt("studio_flag");
				if (t == 0) {
					temp.add("待安排座位");
				} else if (t == 1) {
					temp.add("已安排座位");
				} else if (t == -1) {
					temp.add("影厅已废弃");
				}
				
//				System.out.println(temp);
				vector.add(temp);
				Vector vec = new Vector<String>();
				temp = vec;
//				System.out.println("-->" + vec);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return vector;
		}	
	}

	@Override
	public boolean makeSeat(int iD) {
		boolean rtn = false;
		int row = 0, col = 0;
		
	    Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_row_count, studio_col_count from studio where studio_id="+iD;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				row = rst.getInt("studio_row_count");
				col = rst.getInt("studio_col_count");
			}

			for (int i = 1; i <= row; i++) {
				for (int j = 1; j <= col; j++) {
					PreparedStatement ps = null;
					try {
						String seatSql = "insert into seat(studio_id, seat_row, seat_column, seat_status) values(?,?,?,?)";
						ps = con.prepareStatement(seatSql);
						ps.setInt(1, iD);
						ps.setInt(2, i);
						ps.setInt(3, j);
						ps.setInt(4, 0);
						ps.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ConnectionManager.close(null, ps, null);
						// 生成座位后修改状态为1
						try {
							String strSql = "update studio set studio_flag=1 where studio_id=" + iD;
							System.out.println("------------->" + strSql);
							ps = con.prepareStatement(strSql);
							ps.executeUpdate();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							ConnectionManager.close(null, ps, null);
						}
					}
				}
			}
			rtn = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return rtn;
		}	
	}

	@Override
	public boolean changeSeat(int studio_id, int row, int col, int status) {
		boolean rtn = false;
		
	    Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = null;
			if (status == 0) {
				sql = "update seat set seat_status = 0 where studio_id = " + studio_id 
						+ " and seat_row = " + row + " and seat_column = " + col;
			} else if (status == 1) {
				sql = "update seat set seat_status = -1 where studio_id = " + studio_id
						+ " and seat_row = " + row + " and seat_column = " + col;
			} else if (status == 2) {
				sql = "insert into seat(studio_id, seat_row, seat_column, seat_status) values("
						+ studio_id + "," + row + "," + col + "," + 0 +")";
			} else if (status == 3) {
				sql = "delete from seat where studio_id = " + studio_id 
						+ " and seat_row = " + row + " and seat_column = " + col;
			} 
			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			rtn = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, pstmt, con);
			return rtn;
		}
	}

	@Override
	public List<Studio>  selectID(int ID,Studio stu) {
		List<Studio> stuList = null;
		stuList=new LinkedList<Studio>();
		
	    Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_name, studio_row_count, studio_col_count, studio_introduction,studio_position from studio where studio_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ID);
			
			rst = pstmt.executeQuery();
			while (rst.next()) {
				stu.setName(rst.getString("studio_name"));
				stu.setRowCount(Integer.parseInt(rst.getString("studio_row_count")));
				stu.setColCount(Integer.parseInt(rst.getString("studio_col_count")));
				stu.setIntroduction(rst.getString("studio_introduction"));
				stu.setPosition(rst.getString("studio_position"));
				//System.out.println(Integer.parseInt(rst.getString("studio_row_count"))+ "  "+rst.getInt("studio_row_count")+ "  "+ stu.getColCount());
				stuList.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}
	}

	@Override
	public List<Studio> selectStudioList(String condt) {
		List<Studio> stuList = null;
		stuList=new LinkedList<Studio>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id, studio_name, studio_row_count, studio_col_count,studio_position, studio_introduction from studio where studio_name=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, condt);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				Studio stu = new Studio();
				stu.setID(rst.getInt("studio_id"));
				stu.setName(rst.getString("studio_name"));
				stu.setRowCount(rst.getInt("studio_row_count"));
				stu.setColCount(rst.getInt("studio_col_count"));
				stu.setPosition(rst.getString("studio_position"));
				stu.setIntroduction(rst.getString("studio_introduction"));
				stuList.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}
	}

	@Override
	public Vector<Vector> selectFetch(String condt) {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id, studio_name, studio_row_count, studio_col_count,studio_position, studio_introduction from studio where studio_name=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, condt);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("studio_position"));
				temp.add(rst.getInt("studio_row_count"));
				temp.add(rst.getInt("studio_col_count"));
				temp.add(rst.getString("studio_introduction"));
				
				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return vector;
		}		
		
	}

	@Override
	public Vector<Vector> selectFetchPosition(String condt) {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id, studio_name, studio_row_count, studio_col_count,studio_position, studio_introduction from studio where studio_position=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, condt);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				temp.add(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("studio_position"));
				temp.add(rst.getInt("studio_row_count"));
				temp.add(rst.getInt("studio_col_count"));
				temp.add(rst.getString("studio_introduction"));
				
				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return vector;
		}
	}

	@Override
	public Vector<Vector> selectSched() {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_id,studio_name,studio_position,studio_row_count,studio_col_count,"	
					+ "studio_introduction from studio";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
//				System.out.println(rst.getInt("studio_id"));
				temp.add(rst.getInt("studio_id"));
				temp.add(rst.getString("studio_name"));
				temp.add(rst.getString("studio_position"));
				temp.add(rst.getInt("studio_row_count"));
				temp.add(rst.getInt("studio_col_count"));
				temp.add(rst.getString("studio_introduction"));

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

//    public static void main(String[] args) {
//    	
//        StudioDAO d = new StudioDAO();
//        Studio stu = new Studio();
//        
//        stu.setName("studio");
//        stu.setRowCount(5);
//        stu.setColCount(7);
//       
//		System.out.println("hello");
//		d.insert(stu);
//        
//    }
}
