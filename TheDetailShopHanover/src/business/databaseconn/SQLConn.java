package business.databaseconn;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConn {
	
	public static Connection getMySqlConnection() throws Exception {
	    String driver = "org.gjt.mm.mysql.Driver";
	    String url = "jdbc:mysql://localhost/tiger";
	    String username = "root";
	    String password = "root";
	    
	    Class.forName(driver); // load MySQL driver
	    
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	  }
}
