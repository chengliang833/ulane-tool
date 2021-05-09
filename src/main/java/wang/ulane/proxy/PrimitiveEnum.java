package wang.ulane.proxy;

import java.util.Arrays;
import java.util.List;

public enum PrimitiveEnum {
	_byte("byte", byte.class, "java.lang.Byte", Byte.class, "byteValue"),
	_short("short", short.class, "java.lang.Short", Short.class, "shortValue"),
	_int("int", int.class, "java.lang.Integer", Integer.class, "intValue"),
	_long("long", long.class, "java.lang.Long", Long.class, "longValue"),
	_float("float", float.class, "java.lang.Float", Float.class, "floatValue"),
	_double("double", double.class, "java.lang.Double", Double.class, "doubleValue"),
	_char("char", char.class, "java.lang.Character", Character.class, "charValue"),
	_boolean("boolean", boolean.class, "java.lang.Boolean", Boolean.class, "booleanValue");
	
	private String name;
	private Class primitiveClass;
	private String wrapper;
	private Class wrapperClass;
	private String toPrimitiveMethodName;
	
	private PrimitiveEnum(String name, Class primitiveClass, String wrapper, Class wrapperClass, String toPrimitiveMethodName) {
		this.name = name;
		this.primitiveClass = primitiveClass;
		this.wrapper = wrapper;
		this.wrapperClass = wrapperClass;
		this.toPrimitiveMethodName = toPrimitiveMethodName;
	}
	
	public static List<String> primitive = Arrays.asList("byte","short","int","long","float","double","char","boolean");
	
	public static boolean checkPrimitive(String name){
		return primitive.contains(name);
	}
	public static Class getPrimitiveClass(String name){
		return PrimitiveEnum.valueOf("_"+name).getPrimitiveClass();
	}
	public static PrimitiveEnum getEnum(String name){
		return PrimitiveEnum.valueOf("_"+name);
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
	public String getToPrimitiveMethodName() {
		return toPrimitiveMethodName;
	}
	public void setToPrimitiveMethodName(String toPrimitiveMethodName) {
		this.toPrimitiveMethodName = toPrimitiveMethodName;
	}
	
}
