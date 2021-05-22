package wang.ulane.aspectj.weaver;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/***
 * 只有Aspect注解，标注给aspectweaver加载
 * Component用于spring，但无法用于非bean对象
 */
@Aspect
public class ProfilingAspect {
	
	@Around("execution(public * wang.ulane.aspectj.weaver.bean.*.*(..))")
	public Object logProfile(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		System.out.println("[ProfilingAspect]方法: 【" + joinPoint.getSignature() + "】结束，用时: " + (System.currentTimeMillis() - start));
		
		return result;
	}
}
