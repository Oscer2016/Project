package xupt.se.ttms.dao;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import xupt.se.util.ConnectionManager;

public class LogsDAO {
    public int insert (String time,String str) {
    	int rtn = 0;
    	
        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;

        try {
            String sql = "insert into mylog(log_time,log_content) values(?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, time);
            pstmt.setString(2, str);
            
            pstmt.executeUpdate();
            rtn = 1;    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	ConnectionManager.close(null, pstmt, con);
        	return rtn;
        }
    }
}
