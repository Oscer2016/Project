package xupt.se.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public final class ConnectionManager {
    private static ConnectionManager instance;
    private static DruidDataSource ds;

    // 初始化,只执行一次
    static {
        ResourceBundle rb = ResourceBundle.getBundle("druid");
        ds = new DruidDataSource();
        try {
            ds.setDriverClassName(rb.getString("driver"));
        } catch(Exception e) {
            e.printStackTrace();
        }
        ds.setUrl(rb.getString("url"));
        ds.setUsername(rb.getString("username"));
        ds.setPassword(rb.getString("password"));
    }

    /**
     * 获取数据库实例
     * @return 连接对象ConnectionManager
     */
    public synchronized static final ConnectionManager getInstance() {
        if (instance == null) {
            try {
                instance = new ConnectionManager();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象Connection
     */
    public synchronized final Connection getConnection() {
        try {
            // 查看活动链接数
            // System.out.println("------->busy connections: " + ds.getNumBusyConnections());
            return ds.getConnection();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭数据库连接
     * @return void
     */
    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();
            if(con != null)
                con.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放数据库资源
     * @return void
     */
    @Override
    protected void finalize() throws Throwable {
        // 关闭datasource
        //DruidDataSource.DestroyTask(ds);
        super.finalize();
    }
}
