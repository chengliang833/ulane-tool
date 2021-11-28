package wang.ulane.custom.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import wang.ulane.util.ClipboardUtil;

public class FileListAll {
	
	private List<String> filePaths = new ArrayList<String>();
	private String rootPath;
	private String pathType;
	
	public FileListAll(String rootPath, String pathType) {
		if(rootPath == null || pathType == null){
			throw new RuntimeException("路径和类型不可为空");
		}
		this.rootPath = rootPath;
		this.pathType = pathType;
	}

	/**
	 * 
	 * @param filePath
	 * @param pathType absolute relate filename
	 */
	public void execute(){
		File file = new File(rootPath);
		if(file.isFile()){
			addPath(file);
		}else{
			traverse(file);
		}
		String text = filePaths.stream().collect(Collectors.joining(","));
		ClipboardUtil.setSysClipboardText(text);
	}

    public void traverse(File file){
    	if(file.isDirectory()){
    		for(File f:file.listFiles()){
    			traverse(f);
    		}
    	}else{
    		addPath(file);
    	}
    }
    
    public void addPath(File file){
    	String path = null;
    	switch(pathType){
    		case "absolute":
    			path = file.getAbsolutePath();
    			break;
    		case "filename":
    			path = file.getName();
    			break;
    		case "relate":
			default:
				path = file.getAbsolutePath().replace(rootPath, "");
				if(path.startsWith(File.separator)){
					path = path.substring(1);
				}
    	}
    	filePaths.add(path.replace("\\", "/"));
    }
    
    public static void main(String[] args) {
		new FileListAll("D:\\Develop\\work\\document\\01_shrcb\\上线\\20211112\\SIT_SH_RCB_P_V1.0.0.40_20211102\\04-源码", "filename").execute();;
		System.out.println(ClipboardUtil.getSysClipboardText());
	}
    
}
