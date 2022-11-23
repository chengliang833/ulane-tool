package wang.ulane.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqlStructChange {
	
	public static List<TableCont> tableConts = new ArrayList<>();
	public static Map<String, TableCont> tableContMap = new HashMap<>();
	public static String currentTable = null;
	/**
	 * create
	 * drop
	 * ctl
	 */
	public static String type = "ctl";
	public static boolean append = true;
	public static List<String> usetables = Arrays.asList("A");
	public static void main(String[] args) throws IOException {
		String startpath = "D:\\Develop\\work\\document\\";
		String inpath = startpath+"表结构ods.txt";
		String outpath = startpath+"newcreatetable.sql";
		String ctloutpath = startpath+"ctls\\";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inpath), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			cacheline(line);
		}
		String inpathods = startpath+"表结构.txt";
		refreshFields(inpathods);
		
		Iterator<TableCont> it = tableConts.iterator();
		while(it.hasNext()){
			TableCont tableCont = it.next();
			if(usetables.size() > 0 && !usetables.contains(tableCont.tablename)){
				it.remove();
				continue;
			}
			if(append){
				tableCont.tablename = tableCont.tablename + "_APPEND";
			}
		}
		
		switch (type) {
			case "create":
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outpath), "UTF-8"));
				for(TableCont tableCont:tableConts){
					printLine(tableCont, pw);
				}
				pw.close();
				break;
			case "drop":
				PrintWriter pwdrop = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outpath), "UTF-8"));
				for(TableCont tableCont:tableConts){
					printDropLine(tableCont, pwdrop);
				}
				pwdrop.close();
				break;
			case "ctl":
				File file = new File(ctloutpath);
				if(!file.exists()){
					file.mkdirs();
				}
				for(TableCont tableCont:tableConts){
					createCtl(tableCont, ctloutpath);
				}
				break;
			default:
				System.out.println("错误！！！");
				break;
		}
		br.close();
		System.out.println("out:"+outpath);
		System.out.println("ctlout:"+ctloutpath);
		System.out.println("finish...");
	}
	
	public static void refreshFields(String inpath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inpath), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			if(line.matches("\\s*[A-Za-z_]+-.*")){
				String tablename = line.trim().replaceAll("\\s*([A-Za-z_]+)-.*", "$1").toUpperCase();
				currentTable = tablename;
				if(tableContMap.containsKey(tablename)){
					TableCont tableCont = tableContMap.get(tablename);
					tableCont.tablecomment = line.trim().replaceAll(".*-(.*)", "$1");
				}else{
					throw new RuntimeException("没这表");
				}
			}else{
				TableCont tableCont = tableContMap.get(currentTable);
				if(tableCont == null){
					throw new RuntimeException("没这表");
				}
				String[] linefields = null;
				linefields = line.split("	");
				if(linefields[0].matches("\\d+") && linefields.length >= 4){
					TableField tableField = tableCont.fieldMap.get(linefields[1].toUpperCase());
					if(tableField == null){
						throw new RuntimeException("没这字段");
					}
					tableField.comments = linefields[2].toUpperCase();
					if(linefields.length >= 7){
						tableField.remark = linefields[6].toUpperCase();
					}
				}
			}
		}
	}
	
	public static void createCtl(TableCont tableCont, String ctloutpath) throws IOException{
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ctloutpath+tableCont.tablename.toUpperCase()+".ctl"), "UTF-8"));
		pw.println("OPTIONS(ROWS=1000,SKIP=0,ERRORS=0)");
		pw.println("load data");
		pw.println("CHARACTERSET ZHS16GBK");
		pw.println("truncate into table "+tableCont.tablename.toUpperCase());
		pw.println("fields terminated by ',' optionally enclosed by '\"'");
		pw.println("trailing nullcols");
		pw.println("(");
		for(int i=0,length=tableCont.fields.size(); i<length; i++){
			String line = "  "+tableCont.fields.get(i).fieldname.toUpperCase();
			if(i < length - 1){
				line = line + ",";
			}
			pw.println(line);
		}
		pw.println(")");
		pw.close();
	}
	
	public static void cacheline(String line){
		if(line.matches("--.*\\..*")){
			String tablename = line.trim().replaceAll("--.*\\.(.*)", "$1").toUpperCase();
			currentTable = tablename;
			TableCont tableCont = null;
			if(tableContMap.containsKey(tablename)){
				tableCont = tableContMap.get(tablename);
			}else{
				tableCont = new TableCont();
				tableConts.add(tableCont);
				tableContMap.put(tablename, tableCont);
				tableCont.tablename = tablename;
			}
		}else{
			TableCont tableCont = tableContMap.get(currentTable);
			if(tableCont == null){
				throw new RuntimeException("没这表");
			}
			String[] linefields = null;
			linefields = line.trim().split("\\s+");
			if(linefields.length >= 2){
				TableField tableField = new TableField();
				tableField.fieldname = linefields[0].toUpperCase();
				if(tableCont.fieldMap.containsKey(tableField.fieldname)){
					throw new RuntimeException("字段重复");
				}
				tableCont.fieldMap.put(tableField.fieldname, tableField);
				tableCont.fields.add(tableField);
				tableField.types = linefields[1].toUpperCase();
			}
		}
	}

	public static void printDropLine(TableCont tableCont, PrintWriter pw){
		pw.println("drop table " + tableCont.tablename + ";");
	}
	
	public static void printLine(TableCont tableCont, PrintWriter pw){
		pw.println("create table " + tableCont.tablename + " (");
		for(int i=0,length=tableCont.fields.size(); i<length; i++){
			String line = "  "+tableCont.fields.get(i).fieldname+" "+tableCont.fields.get(i).types;
			if(i < length - 1){
				line = line + ",";
			}
			pw.println(line);
		}
		pw.println(")");
		pw.println("TABLESPACE \"ULANEDATA\";");
		pw.println();
		pw.println("CREATE INDEX \""+tableCont.tablename+"_PKIDX\" ON "+tableCont.tablename+" (\""+tableCont.fields.get(0).fieldname+"\") TABLESPACE \"ULANEIDX\";");
		pw.println();
		pw.println("COMMENT ON TABLE "+tableCont.tablename+" IS '"+tableCont.tablecomment+"';");
		for(int i=0,length=tableCont.fields.size(); i<length; i++){
			TableField tableField = tableCont.fields.get(i);
			pw.println("COMMENT ON COLUMN "+tableCont.tablename+"."+tableField.fieldname+" IS '"
					+tableField.comments
					+(tableField.remark==null?"":("["+tableField.remark+"]"))+"';");
		}
		pw.println();
	}
	
	private static class TableCont {
		public String tablename;
		public String tablecomment;
		public List<TableField> fields = new ArrayList();
		public Map<String, TableField> fieldMap = new HashMap<>();
	}

	private static class TableField {
		public String fieldname;
		public String types;
		public String comments = "";
		public String remark;
	}
	
}
