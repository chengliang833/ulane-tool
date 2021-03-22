package wang.ulane.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleJDBCDemo {
	public static void main(String[] args) {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@***:1521/helowinXDB", "ULANE", "***");
//			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@(description=(address=(protocol=tcp)(port=1521)(host=192.168.10.18))(connect_data=(service_name=mspdb)))", "mobile", "mobile");
//			conn.setAutoCommit(false);
			Statement ps = conn.createStatement();
			int i = ps.executeUpdate("begin "
					+ "INSERT INTO MY_TABLE (ID,AUTHOR,CREATED) VALUES (3,'Pete 单独',to_date('2013-01-31 00:00:00','yyyy-mm-dd hh24:mi:ss')); "
					+ "INSERT INTO MY_TABLE (ID,AUTHOR,CREATED) VALUES (4,'Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独Pete 单独',to_date('2013-01-31 00:00:00','yyyy-mm-dd hh24:mi:ss')); "
					+ "end; ");
			System.out.println("result:" + i);
//			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try {
//					conn.rollback();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
