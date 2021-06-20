package wang.ulane.api.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
	public static void main(String[] args) {
		String strs = "1,2,3,4,5";
		List<Integer> list = Arrays.asList(strs.split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
		int a = Arrays.asList(strs.split(",")).stream().mapToInt(str -> Integer.parseInt(str)).sum();
		System.out.println(list);
		System.out.println(a);
	}
}
