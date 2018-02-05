package xupt.se.ttms.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.iPlayDAO;
import xupt.se.ttms.model.Play;
import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.DataDictSrv;
import xupt.se.util.ConnectionManager;


public class PlayDAO implements iPlayDAO {
	
	@Override
	public int insert(Play stu) {
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into play(play_type_id, play_lang_id, play_name, play_introduction,"
					+ "play_length,play_ticket_price,play_status) values(?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, stu.getTypeId());
			pstmt.setInt(2, stu.getLangId());
			pstmt.setString(3, stu.getName());
			pstmt.setString(4, stu.getIntroduction());
			pstmt.setInt(5, stu.getLength());
			pstmt.setFloat(6, stu.getTicketPrice());
			pstmt.setInt(7, stu.getStatus());

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
	public int update(Play stu) {
		int rtn = 0;
		if (stu == null) {
			return rtn;
		}
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update play set play_type_id=?, play_lang_id=?, play_name=?, play_introduction=?,"
					+ "play_length=?, play_status=? where play_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, stu.getTypeId());
			pstmt.setInt(2, stu.getLangId());
			pstmt.setString(3, stu.getName());
			pstmt.setString(4, stu.getIntroduction());
			pstmt.setInt(5, stu.getLength());
			pstmt.setInt(6, stu.getStatus());
			pstmt.setInt(7, stu.getId());
			
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
			String sql = "delete from  play where play_id=?";
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
	public List<Play> select(String condt) {
		List<Play> listPlay = null;
		listPlay = new LinkedList<Play>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select play_id, play_type_id, play_lang_id, play_name, play_introduction,"
					+ "play_length,play_ticket_price,play_status from play";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();
			
			while(rst.next()){
				Play play1 = new Play();
				play1.setId(rst.getInt("play_id"));
				play1.setTypeId(rst.getInt("play_type_id"));
				play1.setLangId(rst.getInt("play_lang_id"));
				play1.setName(rst.getString("play_name"));
				play1.setIntroduction(rst.getString("play_introduction"));
				play1.setLength(rst.getInt("play_length"));
				play1.setTicketPrice(rst.getFloat("play_ticket_price"));
				play1.setStatus(rst.getInt("play_status"));
				
				listPlay.add(play1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return listPlay;
		}
	}	
	
	@Override
	public List<Play> selectScheduledPlay(String condt) {
		List<Play> stuList = null;
		stuList = new LinkedList<Play>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select play.play_id, play_name from play, schedule " +
		       "where play.play_id=schedule.play_id ";
			condt.trim();
			if (!condt.isEmpty())
				sql += " where play_name=" + condt;
			sql += " group by play_name";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			
			while (rst.next()) {
				Play stu = new Play();
				stu.setId(rst.getInt("play_id"));
				stu.setName(rst.getString("play_name"));
				stuList.add(stu);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return stuList;
		}

	}

	public List<Play> selectmyScheduledPlay() {
 		List<Play> stuList = null;
 		stuList = new LinkedList<Play>();
 		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
 		try {
 			String sql = "select *,COUNT(DISTINCT play_name) from play, schedule " +
 					"where play.play_id=schedule.play_id GROUP BY play_name";
 			pstmt = con.prepareStatement(sql);
 			
 			rst = pstmt.executeQuery();
			while (rst.next()) {
				Play stu = new Play();
				stu.setId(rst.getInt("play_id"));
				stu.setSendtype(new DataDictSrv().Fetch(" dict_id = " + rst.getString("play_type_id")).get(0).getName());
				stu.setSendLang(new DataDictSrv().Fetch(" dict_id = " + rst.getString("play_lang_id")).get(0).getName());
				stu.setName(rst.getString("play_name"));
				stu.setIntroduction(rst.getString("play_introduction"));
				stu.setImage(rst.getString("play_image"));
				stu.setLength(rst.getInt("play_length"));
				stu.setTicketPrice(rst.getFloat("play_ticket_price"));
				stu.setStatus(rst.getInt("play_status"));
				
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
	public Vector listingtype() {
		Vector<String> listPlay = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select dict_value from data_dict where dict_parent_id = 2";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			while(rst.next()){
				Play play1=new Play();
				play1.setName(rst.getString("dict_value"));
				listPlay.add(play1.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return listPlay;
		}
	
	}

	public Vector<String> listing() {
		Vector<String> listPlay = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select dict_value from data_dict where dict_parent_id = 3";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			while(rst.next()){
				listPlay.add(rst.getString("dict_value"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return listPlay;
		}
	}
	
	@Override
	public int delete(Play play) {
		int rtn = 0;
		if (play == null) {
			return rtn;
		}
		
	    Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = " delete from  play where play_name=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, play.getName());
			
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
	public Vector<Vector> Fetch() {
		Vector<Vector> vector = new Vector<Vector>();
		Vector temp = new Vector<String>();
		
	    Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = " select * from play";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			while (rst.next()) {
				temp.add(rst.getInt("play_id"));
				temp.add(rst.getInt("play_lang_id"));
				temp.add(rst.getString("play_name"));
				temp.add(rst.getInt("play_length"));
				temp.add(rst.getString("play_introduction"));
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
	public Vector listingStudio(int ID) {
		Vector<String> listStudio = new Vector<String>();
		
		Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select studio_name from studio";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			while(rst.next()){
				Studio studio = new Studio();
				studio.setName(rst.getString("studio_name"));

				listStudio.add(studio.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return listStudio;
		}
	}

	@Override
	public Vector<String> ShowPlayName(Play play) {
		Vector<String> listPlayName = new Vector<String>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select play_name from play";
			pstmt = con.prepareStatement(sql);
			
			rst = pstmt.executeQuery();
			while(rst.next()){
				listPlayName.add(rst.getString("play_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rst, pstmt, con);
			return listPlayName;
		}	
	}
	
	/*
	 * public Vector<String> listing() {
		Vector<String> listPlay = new Vector<String>();
		try {
			String sql = "select dict_value from data_dict where dict_parent_id = 3";
//			System.out.println("2----->"+sql);
			DBUtil db = new DBUtil();
			if(!db.openConnection()){
				System.out.print("fail to connect database");
				return null;
			}
			ResultSet rst = db.execQuery(sql);
			if (rst!=null) {
				while(rst.next()){
					listPlay.add(rst.getString("dict_value"));
			
				}
			}
			db.close(rst);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(listPlay);
		return listPlay;
	}
	 * */
}
