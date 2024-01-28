package wang.ulane.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReadExcel {
    public static void main(String[] args) {
//        ReadExcel obj = new ReadExcel();
//
//        File file = new File("e:/template.xls");
//        List<StudentInfoDO> excelList = obj.readExcel(file);
//        System.out.println("list中的数据打印出来");
//        for (int i = 0; i < excelList.size(); i++) {
//            List list = (List) excelList.get(i);
//            for (int j = 0; j < list.size(); j++) {
//                System.out.print(list.get(j));
//            }
//            System.out.println();
//        }

    }


    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
//    public List<StudentInfoDO> readExcel(File file) {
//        List<StudentInfoDO> result = new ArrayList();
//        try {
//            // 创建输入流，读取Excel
//            InputStream is = new FileInputStream(file.getAbsolutePath());
//            // jxl提供的Workbook类
//            Workbook wb = Workbook.getWorkbook(is);
//            // Excel的页签数量
//            int sheet_size = wb.getNumberOfSheets();
//            for (int index = 0; index < sheet_size; index++) {
//                List<List> outerList = new ArrayList<List>();
//                // 每个页签创建一个Sheet对象
//                jxl.Sheet sheet = wb.getSheet(index);
//                // sheet.getRows()返回该页的总行数
//                for (int i = 1; i < sheet.getRows(); i++) {
//                    StudentInfoDO infoDO = new StudentInfoDO();
//                    infoDO.setRealName(sheet.getCell(0, i).getContents());
//                    infoDO.setIdcard(sheet.getCell(1, i).getContents());
//                    infoDO.setMobile(sheet.getCell(2, i).getContents());
//                    infoDO.setGender(sheet.getCell(3, i).getContents());
//                    infoDO.setBirthday(sheet.getCell(4, i).getContents());
//                    infoDO.setProvinceCode(sheet.getCell(5, i).getContents());
//                    infoDO.setProvinceName(sheet.getCell(6, i).getContents());
//                    infoDO.setCityCode(sheet.getCell(7, i).getContents());
//                    infoDO.setCityName(sheet.getCell(8, i).getContents());
//                    infoDO.setDistrictCode(sheet.getCell(9, i).getContents());
//                    infoDO.setDistrictName(sheet.getCell(10, i).getContents());
//                    infoDO.setTownCode(sheet.getCell(11, i).getContents());
//                    infoDO.setTownName(sheet.getCell(12, i).getContents());
//                    infoDO.setCommitteeCode(sheet.getCell(13, i).getContents());
//                    infoDO.setCommitteeName(sheet.getCell(14, i).getContents());
//                    infoDO.setCensusAddress(sheet.getCell(15, i).getContents());
//                    infoDO.setHomeAddress(sheet.getCell(16, i).getContents());
//                    infoDO.setAge(Integer.valueOf(sheet.getCell(17, i).getContents()));
//                    infoDO.setHomeName(sheet.getCell(18, i).getContents());
//                    infoDO.setHomeMobile(sheet.getCell(19, i).getContents());
//                    infoDO.setNation(Integer.valueOf(sheet.getCell(20, i).getContents()));
//                    result.add(infoDO);
//                }
//                return result;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public <T> List<T> readExcelWjw(Integer beginRow, File file, Class<T> clazz) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file.getAbsolutePath()));
            Sheet sheet = wb.getSheetAt(0);
            Map<Integer, String> beanpros = new HashMap<Integer, String>();
            Field[] field = clazz.getDeclaredFields();
            for (int i = 0; i < field.length; i++) {
                //设置可以访问私有变量
                field[i].setAccessible(true);
                String name = field[i].getName();
                beanpros.put(i, name);
            }
            Set<Integer> set = beanpros.keySet();
            List<T> list = new ArrayList<>();
            Row row = sheet.getRow(beginRow);
            cont1:
        	while (row != null) {
                T t = (T) clazz.newInstance();
                for (Integer key : set) {
                    if (row != null) {
                        Cell cell = row.getCell(key);
                        String attributeName = beanpros.get(key);
                        if ("id".equals(attributeName) && cell == null) {
                        	break cont1;
                        }
                        if (cell != null) {
                        	int type = 0;
//                            int type = cell.getCellType();
                            String value = "";
//                            if (type == Cell.CELL_TYPE_STRING) {
//                                value = cell.getStringCellValue();
//                            } else if (type == Cell.CELL_TYPE_NUMERIC || type == Cell.CELL_TYPE_FORMULA) {
//                        		try {
//                        			value = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
//                        		} catch (IllegalStateException e) {
//                        			try {
//                        				value = String.valueOf(cell.getRichStringCellValue());
//                        			} catch (Exception e2) {
//                        				value = "";
//                        			}
//                        		}
//                            } else if (type == Cell.CELL_TYPE_BOOLEAN) {
//                                value = String.valueOf(cell.getBooleanCellValue());
//                            }

                            Field fieldValue = t.getClass().getDeclaredField(attributeName);
                            fieldValue.setAccessible(true);
                            if ("id".equals(attributeName) && StringUtils.isBlank(value)) {
                            	break cont1;
                            }else if ("age".equals(attributeName) || "nation".equals(attributeName)) {
                                if (!StringUtils.isBlank(value)) {
                                    fieldValue.set(t, Integer.valueOf(value));
                                }
                            }else if("id_no".equals(attributeName) && !Pattern.matches("^[0-9Xx]{18}$", value)){
                            	fieldValue.set(t, value);
                            	break;
                            } else if(("censusAddress".equals(attributeName) || "c_homeAddress".equals(attributeName))
                            		&& value.contains("-")
                            		){
                            	String[] addrs = value.split("-");
                            	if(addrs.length > 3){
                            		List addrsList = new ArrayList<>(Arrays.asList(addrs));
                            		addrsList.subList(0, 3).clear();
                            		fieldValue.set(t, addrsList.stream().collect(Collectors.joining("-")));
                            	}else{
                            		fieldValue.set(t, value);
                            	}
                            } else {
                                fieldValue.set(t, value);
                            }
                        }
                    }
                }
                list.add(t);
                row = sheet.getRow(++beginRow);
            }
            return list;
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


}