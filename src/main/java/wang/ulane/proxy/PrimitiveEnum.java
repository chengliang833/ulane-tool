package wang.ulane.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PrimitiveEnum {
	_byte("byte", byte.class, "java.lang.Byte", Byte.class),
	_short("short", short.class, "java.lang.Short", Short.class),
	_int("int", int.class, "java.lang.Integer", Integer.class),
	_long("long", long.class, "java.lang.Long", Long.class),
	_float("float", float.class, "java.lang.Float", Float.class),
	_double("double", double.class, "java.lang.Double", Double.class),
	_char("char", char.class, "java.lang.Character", Character.class),
	_boolean("boolean", boolean.class, "java.lang.Boolean", Boolean.class);
	
	private String name;
	private Class primitiveClass;
	private String wrapper;
	private Class wrapperClass;
	
	private PrimitiveEnum(String name, Class primitiveClass, String wrapper, Class wrapperClass) {
		this.name = name;
		this.primitiveClass = primitiveClass;
		this.wrapper = wrapper;
		this.wrapperClass = wrapperClass;
	}
	
	public static List<String> initPrimitive0 = Arrays.asList("byte","short","int","long","float","double","char");
	public static String initPrimitiveFalse = "boolean";
	
	public static boolean checkPrimitive(String name){
		return initPrimitive0.contains(name) || initPrimitiveFalse.equals(name);
	}
	public static Class getPrimitiveClass(String name){
		return PrimitiveEnum.valueOf("_"+name).getPrimitiveClass();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getPrimitiveClass() {
		return primitiveClass;
	}

	public void setPrimitiveClass(Class primitiveClass) {
		this.primitiveClass = primitiveClass;
	}

	public String getWrapper() {
		return wrapper;
	}

	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}

	public Class getWrapperClass() {
		return wrapperClass;
	}

	public void setWrapperClass(Class wrapperClass) {
		this.wrapperClass = wrapperClass;
	}
	
}
