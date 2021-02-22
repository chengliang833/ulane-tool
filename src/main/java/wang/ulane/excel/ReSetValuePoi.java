package wang.ulane.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import wang.ulane.rsa.DesUtil;

/***
 * poi支持xls和xlsx
 *
 */
public class ReSetValuePoi {
	
	public static void main(String[] args) {
		System.out.println("start...");
		String folder = "D:\\Develop\\work\\Z_wy\\prg\\99_all\\平江签约导出\\test";
		for(File file:new File(folder).listFiles()){
			reset(file);
		}
		
		System.out.println("finish...");
	}

	public static void reset(File file) {
        OutputStream out = null;
		try {
            Workbook workBook = WriteExcel.getWorkbok(file);
            Sheet sheet = workBook.getSheetAt(0);
			
            int rowNum = 1;
            int colNum = 1;
            
            Row row = sheet.getRow(rowNum);
            while(row != null){
            	Cell cell = row.getCell(colNum);
            	if(cell == null){
            		break;
            	}
            	String value = cell.getStringCellValue();
            	if(StringUtils.isBlank(value)){
            		break;
            	}
            	
            	cell.setCellValue(DesUtil.desDecode(value));
            }
            
            out =  new FileOutputStream(file);
            workBook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
}
