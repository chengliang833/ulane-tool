package wang.ulane.api.jdkproxy;

import java.lang.reflect.Proxy;

public class InstanceOne<T> {
	
	private Class<T> interfaceClass;
	
	public InstanceOne(Class<T> interfaceClass) {
		super();
		this.interfaceClass = interfaceClass;
	}

	@SuppressWarnings("unchecked")
	public T newInstance(){
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args1)->{
			return null;
		});
	}
	
}
