package wang.ulane.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import wang.ulane.file.ConvertUtil;

public class Export {
	
	private static String filename = "健路外检数据0930113900-1012092900.txt";
	private static String rootPath = "D:\\Develop\\work\\Z_wy\\prg\\0_common\\08_签约系统_体检随访等后续\\外检数据导出\\";
//	private static String rootPath = "D:\\Develop\\Install\\eclipse_pristine\\workspace\\fortest\\src\\main\\resources\\";
	
	public static void main(String[] args) throws IOException {
		System.out.println("start");
		String filePath = rootPath + filename;
		String temp = ConvertUtil.parseString(new FileInputStream(new File(filePath)));
//		System.out.println(temp.substring(0, 1000));
//		toExcel(JSONObject.parseObject(temp).getJSONArray("data"));
//		toExcelSignle(JSONObject.parseObject(temp).getJSONArray("data"));
//		toExcelSignle(JSONArray.parseArray(temp));
//		toExcelSignle(readFormLine(filePath));
//		saveToLine(readFormLine(filePath), rootPath + "无标题2.txt");
		toExcelSecondArray(JSONObject.parseObject(temp).getJSONArray("data"));
		System.out.println("finish...");
	}
	
	
	public static void saveToLine(JSONArray arr, String filePath) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filePath)));
		List<String> arrExists = new ArrayList<String>();
		for(Object obj:arr){
			String str = (String) obj;
			if(str.length() < 6){
				arrExists.add(str);
				pw.println(str);
			}else if(!arrExists.contains(str.substring(0, 3))){
				arrExists.add(str.substring(0, 3));
				pw.println(str);
			}
		}
		pw.close();
	}
	public static JSONArray readFormLine(String filePath) throws IOException{
		JSONArray jsonArray = new JSONArray();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
//			jsonArray.add(JSONObject.parseObject(line));
			jsonArray.add(line);
		}
		br.close();
		return jsonArray;
	}
	
	public static void toExcelSignle(JSONArray arr) {
		// 选择模板文件：
		String ppath = rootPath;
		String newpath = ppath + filename.substring(0, filename.lastIndexOf(".")) + ".xls";
		try {
//			Workbook wb = Workbook.getWorkbook(new File(realpath));
			// 第二步：通过模板得到一个可写的Workbook：第一个参数是一个输出流对象,第二个参数代表了要读取的模板
			File targetFile = new File(newpath);
			WritableWorkbook wwb = Workbook.createWorkbook(targetFile);
			// 第三步：选择模板中名称为StateResult的Sheet（第0个sheet）：
			//改为直接创建sheet
			WritableSheet wws = wwb.createSheet("Sheet1", 0);
			// 第四步：选择单元格，写入动态值，根据单元格的不同类型转换成相应类型的单元格：
			/**
			 * ******************************public***************** ****
			 * *******
			 */
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
			
			jxl.write.WritableCellFormat wcf_cn = new jxl.write.WritableCellFormat(wf);
			wcf_cn.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);//设置字体
			wcf_cn.setAlignment(jxl.format.Alignment.CENTRE);// 把水平对齐方式指定为居中
			wcf_cn.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);;// 把垂直对齐方式指定为居中
			
			/**
			 * ******************************condition************** ****
			 * **********
			 */
			Iterator it =  arr.iterator();
			int row = 1;//数据行
			List<String> datakeys = null;
			int keyCol = 0;
			while(it.hasNext()){
				JSONObject obj = (JSONObject) it.next();
//				JSONArray objList = obj.getJSONArray("data");
//				JSONObject data = JSONObject.parseObject(JSONObject.parseObject(obj.getString("message")).getString("data"));
//				data = data.getJSONObject("data");//包了两层
				
//				for(Object dataObj:objList){
					int col = 0;
					JSONObject data = (JSONObject) obj;
					if(datakeys == null){
						datakeys = new ArrayList<String>(data.keySet());
//						datakeys.addAll(data.keySet());
						for(String key:datakeys){
							wws.addCell(new Label(keyCol, 0, key, wcf_cn));
							keyCol++;
						}
					}
					
					for(String key:datakeys){
						String value = data.getString(key);
						wws.addCell(new Label(col, row, value == null ? data.getString(key) : value, wcf_cn));
						col++;
					}
					
					row++;
//				}
				
			}
			wwb.write();
			wwb.close();
//			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void toExcel(JSONArray arr) {
		// 选择模板文件：
		String ppath = rootPath;
		String newpath = ppath + filename.substring(0, filename.lastIndexOf(".")) + ".xls";
//		String ppath = "D:\\Develop\\work\\Z_wy\\prg\\0_common\\01_公卫3.0数据转换\\";
//		String realpath = ppath + "template1.xls";
//		String newpath = ppath + filename + ".xls";
		try {
//			Workbook wb = Workbook.getWorkbook(new File(realpath));
			// 第二步：通过模板得到一个可写的Workbook：第一个参数是一个输出流对象,第二个参数代表了要读取的模板
			File targetFile = new File(newpath);
			WritableWorkbook wwb = Workbook.createWorkbook(targetFile);
			// 第三步：选择模板中名称为StateResult的Sheet（第0个sheet）：
			WritableSheet wws = wwb.createSheet("Sheet1", 0);
			// 第四步：选择单元格，写入动态值，根据单元格的不同类型转换成相应类型的单元格：
			/**
			 * ******************************public***************** ****
			 * *******
			 */
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
			
			jxl.write.WritableCellFormat wcf_cn = new jxl.write.WritableCellFormat(wf);
			wcf_cn.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);//设置字体
			wcf_cn.setAlignment(jxl.format.Alignment.CENTRE);// 把水平对齐方式指定为居中
			wcf_cn.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);;// 把垂直对齐方式指定为居中
			
			/**
			 * ******************************condition************** ****
			 * **********
			 */
			Iterator it =  arr.iterator();
			int row = 1;//数据行
			Set<String> datakeys = null;
			Set<String> secondDatakeys = null;
			int keyCol = 0;
			while(it.hasNext()){
				int col = 0;
//				JSONObject msg = JSONObject.parseObject(((JSONObject) it.next()).getString("msg"));
				JSONObject msg = (JSONObject) it.next();
				JSONObject data = msg.getJSONObject("data");
				if(data == null){
					data = msg;
				}
				JSONObject secondData = msg.getJSONObject("itemResult");
				if(datakeys == null && data != null){
					datakeys = data.keySet();
//					if(!datakeys.contains("doctorIdCard")){
//						datakeys = new HashSet<String>(datakeys);
//						datakeys.add("doctorIdCard");
//					}
					for(String key:datakeys){
						int length = calculateCellLength(key) + 2;
						if(wws.getColumnView(keyCol).getSize() < length * 256){
							wws.setColumnView(keyCol, length);
						}
						wws.addCell(new Label(keyCol, 0, key, wcf_cn));
						keyCol++;
					}
					
					wws.addCell(new Label(keyCol, 0, null, wcf_cn));
					keyCol++;
				}
				if(secondDatakeys == null && secondData != null){
					secondDatakeys = secondData.keySet();
					for(String key:secondDatakeys){
						wws.addCell(new Label(keyCol, 0, key, wcf_cn));
						keyCol++;
					}
				}
				
				for(String key:datakeys){
					wws.addCell(new Label(col, row, data.getString(key), wcf_cn));
					col++;
				}
				
				wws.addCell(new Label(col, row, null, wcf_cn));
				col++;
				
				if(secondData != null){
					for(String key:secondDatakeys){
						wws.addCell(new Label(col, row, secondData.getString(key), wcf_cn));
						col++;
					}
				}
				row++;
			}
			wwb.write();
			wwb.close();
//			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void toExcelSecondArray(JSONArray arr) {
		// 选择模板文件：
		String ppath = rootPath;
		String newpath = ppath + filename.substring(0, filename.lastIndexOf(".")) + ".xls";
//		String ppath = "D:\\Develop\\work\\Z_wy\\prg\\0_common\\01_公卫3.0数据转换\\";
//		String realpath = ppath + "template1.xls";
//		String newpath = ppath + filename + ".xls";
		try {
//			Workbook wb = Workbook.getWorkbook(new File(realpath));
			// 第二步：通过模板得到一个可写的Workbook：第一个参数是一个输出流对象,第二个参数代表了要读取的模板
			File targetFile = new File(newpath);
			WritableWorkbook wwb = Workbook.createWorkbook(targetFile);
			// 第三步：选择模板中名称为StateResult的Sheet（第0个sheet）：
			WritableSheet wws = wwb.createSheet("Sheet1", 0);
			// 第四步：选择单元格，写入动态值，根据单元格的不同类型转换成相应类型的单元格：
			/**
			 * ******************************public***************** ****
			 * *******
			 */
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
			
			jxl.write.WritableCellFormat wcf_cn = new jxl.write.WritableCellFormat(wf);
			wcf_cn.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);//设置字体
			wcf_cn.setAlignment(jxl.format.Alignment.CENTRE);// 把水平对齐方式指定为居中
			wcf_cn.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);;// 把垂直对齐方式指定为居中
			
			/**
			 * ******************************condition************** ****
			 * **********
			 */
			Iterator it =  arr.iterator();
			int row = 1;//数据行
			List<String> datakeys = null;
			List<String> secondDatakeys = null;
			int keyCol = 0;
			while(it.hasNext()){
				int col = 0;
//				JSONObject msg = JSONObject.parseObject(((JSONObject) it.next()).getString("msg"));
				JSONObject msg = (JSONObject) it.next();
				JSONObject data = msg.getJSONObject("data");
				if(data == null){
					data = msg;
				}
				JSONArray secondDataArray = msg.getJSONArray("itemResult");
				JSONObject secondData = jsonArrToObj(secondDataArray, "itemName");
				if(datakeys == null && data != null){
					datakeys = new ArrayList<>(data.keySet());
//					if(!datakeys.contains("doctorIdCard")){
//						datakeys = new HashSet<String>(datakeys);
//						datakeys.add("doctorIdCard");
//					}
					for(String key:datakeys){
						wws.addCell(new Label(keyCol, 0, key, wcf_cn));
						keyCol++;
					}
					
//					wws.addCell(new Label(keyCol, 0, null, wcf_cn));
					keyCol++;
				}
				if(secondDatakeys == null && secondData != null){
					secondDatakeys = new ArrayList<>(secondData.keySet());
					for(String key:secondDatakeys){
						wws.addCell(new Label(keyCol, 0, key, wcf_cn));
						keyCol++;
					}
				}else{
					//检查是否有新增key
					for(String newKey:secondData.keySet()){
						if(!secondDatakeys.contains(newKey)){
							secondDatakeys.add(newKey);
							wws.addCell(new Label(keyCol, 0, newKey, wcf_cn));
							keyCol++;
						}
					}
				}
				
				for(String key:datakeys){
					wws.addCell(new Label(col, row, data.getString(key), wcf_cn));
					col++;
				}
				
//				wws.addCell(new Label(col, row, null, wcf_cn));
				col++;
				
				if(secondData != null){
					for(String key:secondDatakeys){
						JSONObject secDataSub = secondData.getJSONObject(key);
						if(secDataSub != null){
							wws.addCell(new Label(col, row, secDataSub.getString("result"), wcf_cn));
						}
						col++;
					}
				}
				row++;
			}
			wwb.write();
			wwb.close();
//			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static JSONObject jsonArrToObj(JSONArray arr, String keyName){
		JSONObject obj = new JSONObject();
		for(Object arrObj:arr){
			obj.put(((JSONObject)arrObj).getString(keyName), arrObj);
		}
		return obj;
	}

	public static int calculateCellLength(String str){
		return str.replaceAll("[^_\\u4e00-\\u9fa5]+", "").length()*2 //保留中文下划线
				+ str.replaceAll("[_\\u4e00-\\u9fa5]+", "").length(); //保留非中文下划线
//		return str.replaceAll("[^a-zA-Z0-9]+", "").length() //保留字母数字
//				+ str.replaceAll("[a-zA-Z0-9]+", "").length()*2; //保留非字母数字
	}
	
}
