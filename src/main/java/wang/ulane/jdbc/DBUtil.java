package wang.ulane.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

import wang.ulane.init.PropLoad;

public class DBUtil {
	
	private static Logger log = LoggerFactory.getLogger(DBUtil.class);  

	private static DruidDataSource ds;
	
	static{
		try {
			String driverName = PropLoad.getProperty("drivername");
			String host = PropLoad.getProperty("host");
			String username = PropLoad.getProperty("username");
			String password = PropLoad.getProperty("password");
			int maxActive = PropLoad.getInt("maxActive");
			long maxWait = Long.parseLong(PropLoad.getProperty("maxWait"));
			
			ds = new DruidDataSource();
			ds.setDriverClassName(driverName);
			ds.setUrl(host);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setMaxActive(maxActive);
			ds.setMaxWait(maxWait);
			
			log.debug("database 初始化完毕！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	public static void closeConnection(Connection conn){
		try {
			if(conn != null){
				conn.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private static Map<String, Object> putOneResultSetToMap(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		Map<String, Object> values = new HashMap<String, Object>();
		for(int i=0,length=rsmd.getColumnCount(); i<length; i++){
			// 循环，获取列及对应的列名
			String columnLabel = rsmd.getColumnLabel(i + 1);
			// 循环，根据列名从ResultSet结果集中获得对应的值
			Object columnValue = rs.getObject(columnLabel);
			// 列名为key,列的值为value
			values.put(columnLabel, columnValue);
		}
		log.debug("<------EachOne: {}",values.toString());
		return values;
	}
	
	public static ResultSet readResult(Connection con, String sql, Object... params) throws SQLException, ClassNotFoundException {
		log.debug("==>  Preparing: {}", sql);
//		log.debug("==> Parameters: {}", JSON.toJSONString(params));
		//测试，打印mybatis形式sql用
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Object param:params){
			if(first){
				first = false;
			}else{
				sb.append(", ");
			}
			if(param == null){
				throw new DBException("参数不能为空");
			}
			sb.append(param).append("(").append(param.getClass().getName().substring(param.getClass().getName().lastIndexOf(".")+1)).append(")");
		}
		log.debug("==> Parameters: {}", sb.toString());

		PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		for(int i=0,length=params.length; i<length; i++){
			ps.setObject(i+1, params[i]);
		}
		return ps.executeQuery();
	}
	
	private static Map<String, Object> changeAffirm(String sql, Object...params){
		Map<String, Object> paramMap = new HashMap<>();
		if(params == null || params.length == 0 || !(params[0] instanceof Param)){
			return null;
		}
		for(Object param:params){
			paramMap.put(((Param)param).getName(), ((Param)param).getValue());
		}

		List<Object> paramList = new ArrayList<>();
		Pattern pat = Pattern.compile("#\\{(.*?)\\}");
		Matcher m = pat.matcher(sql);
		while(m.find()){
//			for(int i=0,length=m.groupCount()+1; i<length; i++){
				//group(0),匹配到的整体字符串，group(1)-$1
//			}
			paramList.add(paramMap.get(m.group(1)));
		}
		//需重新matcher
		Map<String, Object> map = new HashMap<>();
		map.put("sql", pat.matcher(sql).replaceAll("?"));
		map.put("params", paramList.toArray());
		return map;
	}
	
	public static List<Map<String, Object>> readList(String sql, Object... params) {
		Map<String, Object> map = changeAffirm(sql, params);
		if(map != null){
			sql = (String)map.get("sql");
			params = (Object[])map.get("params");
		}
		try {
			long start = System.currentTimeMillis();
			Connection con = DBUtil.getConnection();
			List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
			ResultSet rs = readResult(con, sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				Map<String, Object> rmap = putOneResultSetToMap(rs, rsmd);
				rlist.add(rmap);
			}
			con.close();
			log.debug("<==      Total: {}, time:{}ms", rlist.size(), System.currentTimeMillis()-start);
			return rlist;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DBException("数据库连接异常");
		}
	}
	
	public static Map<String,Object> readMap(String sql, Object... params){
		Map<String, Object> map = changeAffirm(sql, params);
		if(map != null){
			sql = (String)map.get("sql");
			params = (Object[])map.get("params");
		}
		try {
			long start = System.currentTimeMillis();
			Connection con = DBUtil.getConnection();
			ResultSet rs = readResult(con, sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String, Object> rmap = null;
			while(rs.next()){
				if(rmap != null){
					throw new DBException("查到了多条记录");
				}
				rmap = putOneResultSetToMap(rs, rsmd);
			}
			con.close();
			log.debug("<==      Total: {}, time:{}ms", rmap==null?0:1, System.currentTimeMillis()-start);
			return rmap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DBException("数据库执行异常");
		}
	}
	
//	public static void main(String[] args) throws SQLException, ClassNotFoundException {
////		System.out.println(System.getProperty("java.library.path"));
//		String sql = "select * from DR_Patient where Pat_CaseNo = ?";
//		Connection con = DBUtil.getConnection();
//		PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
////		ps.setString(1, "20200701101000");
//		ps.setInt(1, 100);
//		ResultSet rs = ps.executeQuery();
//		ResultSetMetaData rsmd = rs.getMetaData();
//		
//		while(rs.next()){
//			Map<String, Object> map = putOneResultSetToMap(rs, rsmd);
//			System.out.println(map.toString());
//		}
//		
//	}

}
