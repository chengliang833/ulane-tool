package wang.ulane.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import wang.ulane.init.PropLoad;

public class TraverseFileFromSign {

	private static List<String> getFilesFromFolder(String fromPath, String path) {
		File fromFile = new File(fromPath+path);
		List<String> files = new ArrayList<String>();
		findFilesFromFolder(fromFile, new File(fromPath).getPath(), files);
		return files;
	}
	private static void findFilesFromFolder(File fromFile, String fromPath, List<String> files) {
		if (fromFile.isDirectory()) {
			for (File file : fromFile.listFiles()) {
				findFilesFromFolder(file, fromPath, files);
			}
		} else {
			files.add(fromFile.getPath().replace(fromPath, "").substring(1).replace("\\", "/"));
		}
	}

	private static List<String> getFilesFromJarFile(String jarPath, String filePath) {
		List<String> files = new ArrayList<String>();
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
		Enumeration<JarEntry> ee = jarFile.entries();
		while (ee.hasMoreElements()) {
			JarEntry entry = (JarEntry) ee.nextElement();
			// 过滤我们出满足我们需求的东西
			if (entry.getName().startsWith(filePath) && !entry.getName().endsWith("/")) {
//				jarEntryList.add(entry);
				files.add(entry.getName());
			}
		}
//		for (JarEntry entry : jarEntryList) {
//			if (!entry.getName().endsWith("/")) {
//				files.add(entry.getName());
//			}
//		}
		return files;
	}
	
	public static List<String> getFiles(String path){
		if(path.startsWith("/")){
			path = path.substring(1);
		}
		String rootPath = PropLoad.getExcutePath();
//		System.out.println(rootPath);
//		System.out.println(path);
		if(rootPath.endsWith(".jar")){
			return getFilesFromJarFile(rootPath, path);
		}
		return getFilesFromFolder(rootPath, path);
	}
	
	public static void main(String[] args) {
//		 List<String> files = TraverseFromPackage.getFilesFromFolder("");
//		List<String> files = TraverseFromPackage.getFilesFromJarFile(
//				"D:\\Develop\\work\\developfile\\git\\fortest\\target\\fortest-0.0.1-SNAPSHOT-jar-with-dependencies.jar",
//				"sql");
		
		List<String> files = TraverseFileFromSign.getFiles("sql");
		for (String file : files) {
			System.out.println(file);
		}
		System.out.println("finish...");
	}
}
