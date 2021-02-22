package wang.ulane.excel;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import wang.ulane.rsa.DesUtil;

/***
 * jxl只支持xls
 *
 */
public class ReSetValueJxl {
	
	public static JSONArray arr = new JSONArray();
	
	public static void main(String[] args) {
		System.out.println("start...");
		String folder = "D:\\Develop\\work\\Z_wy\\prg\\99_all\\城步\\新建文件夹";
		for(File file:new File(folder).listFiles()){
			if(!file.getName().startsWith("_")){
				System.out.println("file:"+file.getName());
				reset(file);
//				extractField(file);
			}
		}
		System.out.println(arr.toJSONString());
		
		System.out.println("finish...");
	}
	
	public static void extractField(File file){
		try {
			Workbook wb = Workbook.getWorkbook(file);
			
			int i = 0;
			int col = 1;
			int row = 2;
			for(Sheet ws:wb.getSheets()){
				if(i != 0){
					row = 0;
				}
				int allRows = ws.getRows();
				String value = ws.getCell(col, row).getContents();
				while(StringUtils.isNotBlank(value)){
					arr.add(value);
					row++;
					if(row < allRows){
						value = ws.getCell(col, row).getContents();
					}else{
						value = null;
					}
				}

				i++;
			}
			
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reset(File file) {
		try {
			Workbook wb = Workbook.getWorkbook(file);
			
			File targetFile = new File(file.getParent()+"/_"+file.getName());
			WritableWorkbook wwb = Workbook.createWorkbook(targetFile, wb);
			
			int i = 0;
			for(WritableSheet wws:wwb.getSheets()){
				resetBySheet(wws, i == 0);
				i++;
			}
			
			wwb.write();
			wwb.close();
			
			String oldPath = file.getAbsolutePath();
			file.delete();
			targetFile.renameTo(new File(oldPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void resetBySheet(WritableSheet wws, boolean isFirst) throws RowsExceededException, WriteException{
		int col = 1;//只更改第二列
		
		int row = 0;//数据行
		if(isFirst){
			row = 1;//数据行
		}
		
		String value = wws.getCell(col, row).getContents();
		while(StringUtils.isNotBlank(value)){
//			System.out.println("row:"+row);
			wws.addCell(new Label(col, row, DesUtil.desDecode(value)));
			row++;
			value = wws.getCell(col, row).getContents();
		}
	}
	
}
