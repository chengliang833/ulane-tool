package wang.ulane.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import wang.ulane.util.regex.RegexUtil;

public class FileDeepCopy {
	public static void main(String[] args) throws Exception {
		System.out.println("start...");
		executeTest();
		System.out.println("finish...");
	}
	
	private List<String> includeSuf = new ArrayList<>();
	
	public static void executeTest() throws Exception {
		String fromPath = "D:\\My_Hire\\temp";
		String toPath = "C:\\Users\\Administrator\\Desktop\\temp\\temp";
		new FileDeepCopy().traverse(new File(fromPath), new File(fromPath), toPath);
	}

	public void traverseWithOwn(String moveAimPath, String toPath) throws IOException{
		File file = new File(moveAimPath);
		traverse(file, file.getParentFile(), toPath);
	}
	public void traverse(String fromPath, String toPath) throws IOException{
		File file = new File(fromPath);
		traverse(file, file.isFile()?file.getParentFile():file, toPath);
	}
    public void traverse(File file, File fromDirFile, String toPath) throws IOException{
		if(!file.exists()){
			System.out.println("文件/文件夹不存在；"+file.getAbsolutePath());
			return;
		}
    	if(file.isDirectory()){
    		for(File f:file.listFiles()){
    			traverse(f, fromDirFile, toPath);
    		}
    	}else{
			if(includeSuf == null || includeSuf.isEmpty()){
				copy(file, fromDirFile, toPath);
			}else{
				for(String suf:includeSuf){
					if(file.getName().endsWith(suf)){
						copy(file, fromDirFile, toPath);
					}
				}
			}
    	}
    }
    
    public static void copy(File file, File fromDirFile, String toPath) throws IOException{
    	String toPathFile = file.getPath().replace(fromDirFile.getPath(), toPath);
//    	System.out.println("from:"+file.getPath()+"\r\nto:"+toPathFile);
    	
    	FileManage.saveFile(toPathFile, new FileInputStream(file));
    }

	public FileDeepCopy includeSuf(String ...sufs){
		for(String suf:sufs){
			includeSuf.add(suf);
		}
		return this;
	}

//    public static String changeFile(FileInputStream fs) throws IOException{
//    	String allStr = ConvertUtil.parseString(fs);
//		BufferedReader br = new BufferedReader(new InputStreamReader(ConvertUtil.parseInputStream(allStr), "UTF-8"));
//		StringBuilder sb = new StringBuilder();
//		String line = null;
//		while((line = br.readLine()) != null){
//			String wrap = null;
//			if(Pattern.compile(".*\r\n.*", Pattern.DOTALL).matcher(allStr).matches()){
//				wrap = "\r\n";
//			}else if(Pattern.compile(".*\r.*", Pattern.DOTALL).matcher(allStr).matches()){
//				wrap = "\r";
//			}else{
//				wrap = "\n";
//			}
//			line = replaceLine(line, wrap) + wrap;
//			sb.append(line);
//		}
//		br.close();
//    	return sb.toString();
//    }
//    public static String replaceLine(String line, String wrap){
//		if(line.matches("\\s*@TableField\\(\\\"\\w+\\\"\\)")){
//			line = RegexUtil.replaceAll(line, "(\\s*)@TableField\\(\\\"(\\w+)\\\"\\)", (matchStr, matchI, matcher) -> {
//				String name = RegexUtil.replaceAll(matcher.group(2), "_([a-z])", (matchStr2, matchI2, matcher2) -> {
//					return matcher2.group(1).toUpperCase();
//				});
//				String add = matcher.group(1)+"public static final String _" + name + " = \""+matcher.group(2)+"\";"+wrap;
//				return add + matchStr;
//			});
//		}
//    	return line;
//    }
    
}
