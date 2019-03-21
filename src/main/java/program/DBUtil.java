package program;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by admin on 2017/5/23.
 * 数据库工具类
 */
public class DBUtil {

    //声明连接池
    private static BasicDataSource ds;

    static{
        //读取连接参数
        Properties p = new Properties();
        try {
            p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            //数据库连接参数
            String driver = p.getProperty("driver");
            String url = p.getProperty("url");
            String user = p.getProperty("username");
            String pwd = p.getProperty("password");
            //连接池参数
            //String initSize = p.getProperty("initSize");
            String maxSize = p.getProperty("maxActive");
            //创建连接池,并给他设置参数
            ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pwd);
            //ds.setInitialSize(Integer.parseInt(initSize));
            ds.setMaxActive(Integer.parseInt(maxSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return 连接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 归还数据库连接
     * @param conn 连接
     */
    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
