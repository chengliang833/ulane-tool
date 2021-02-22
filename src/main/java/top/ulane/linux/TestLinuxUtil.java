package top.ulane.linux;

public class TestLinuxUtil {
	public static void main(String[] args) {
		//&&按顺序执行完成后执行 &每一个同时执行
		JavaShellUtil.ExecCommand("cd /home/ulane/workspace/fortest/src/main/resources/sh && ./test.sh 1 \"呃啊 司法\" > ./shexec.log");
		
		System.out.println("finish...");
	}
}
