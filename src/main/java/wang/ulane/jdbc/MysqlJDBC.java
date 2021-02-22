package wang.ulane.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlJDBC {
	public static void main(String[] args){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/test", "ulane", "123456");
			System.out.println("与数据库建立连接！");
			System.out.println(conn);
			
			Statement state = conn.createStatement();
			
			String sql = "SELECT ename,job,sal,deptno FROM emp_Ulane";
			ResultSet rs = state.executeQuery(sql);
			
			while(rs.next()){
				//	获取信息
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				int sal = rs.getInt("sal");
				int deptno = rs.getInt("deptno");
				System.out.println(ename + "，" + job + "，" + sal + "，" + deptno);
			}
			System.out.println("查找完成");
			
			//	与数据库断开连接
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}