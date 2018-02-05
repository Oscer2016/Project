package xupt.se.ttms.dao;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.IUserDAO;
import xupt.se.ttms.model.User;
import xupt.se.util.ConnectionManager;


public class UserDAO implements IUserDAO {
	
    public User select(String condt) {
        User user = null;
        
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
        	String sql = "select * from user ";
        	if (!condt.isEmpty()) {
        		sql += "where " + condt;
        	}
        	user = new User();
        	pstmt = con.prepareStatement(sql);
        	rst = pstmt.executeQuery();
            if(rst.next()) {
                user.setUserRank(rst.getString("user_rank"));
                user.setUserId(rst.getInt("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	 ConnectionManager.close(rst, pstmt, con);
        	 return user;
        }
    }
    
    public int insert(User user) {
        int rtn = 0;
        if (user == null) {
        	return rtn;
        }
        
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        try {
            String sql = "insert user(user_name,user_sex,user_birthday,user_tel_num,user_email) values(?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getUserSex());
            pstmt.setString(3, user.getUserBirthday());
            pstmt.setString(4, user.getUserTelnum());
            pstmt.setString(5, user.getUserEmail());
            
            pstmt.executeUpdate();
            rtn = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	ConnectionManager.close(null, pstmt, con);
        	return rtn;
        }
    }
    
//    public static void main(String[] args) {
//    	User user = new User();
//    	user = new UserDAO().select("user_tel_num = 18709261963");
//    	System.out.println(user.getUserRank());
//    }
}
