package wang.ulane.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class JDBCDemo {
	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.22.10.79:1521:orcl", "mobile", "123456");
//			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@(description=(address=(protocol=tcp)(port=1521)(host=192.168.10.18))(connect_data=(service_name=mspdb)))", "mobile", "mobile");
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery("SELECT ID FROM T_BANNER_MESSAGE WHERE STATE=1");
			ResultSetMetaData rsm = rs.getMetaData();
			System.out.println("1111!" + rsm.toString());
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
