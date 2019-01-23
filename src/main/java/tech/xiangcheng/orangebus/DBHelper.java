package tech.xiangcheng.orangebus;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
 
@Deprecated
public class DBHelper {  
	/*uat
    public static final String url = "jdbc:mysql://120.77.54.108:3306/jeeplus_schema";  
    public static final String name = "orangebus-uat";  
    public static final String user = "root";  
    public static final String password = "rootorange";  
  */
	/*dev*/
    public static final String url = "jdbc:mysql://localhost:3306/jeeplus_schema";  
    public static final String name = "orangebus-dev";  
    public static final String user = "root";  
    public static final String password = "hhh";  
    
    public static String driver = "com.mysql.jdbc.Driver";
    
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public DBHelper(String sql) {  
    }  
  
    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}  