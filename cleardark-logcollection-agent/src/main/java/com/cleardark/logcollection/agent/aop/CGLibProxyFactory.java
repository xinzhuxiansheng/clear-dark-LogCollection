package com.cleardark.logcollection.agent.aop;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGLibProxyFactory implements MethodInterceptor{
	private Object object;
	
	public Object createLogger(Object object) {
		this.object = object;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(object.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		String str = (String)obj;
		System.out.println("进入方法");
		methodProxy.invoke(str, args);
        Object result=null;
		return result;
	}

}
