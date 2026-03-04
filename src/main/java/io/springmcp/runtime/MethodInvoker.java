package io.springmcp.runtime;

import java.lang.reflect.Method;

public class MethodInvoker {

    private final Object target;
    private final Method method;

    public MethodInvoker(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object invoke(Object[] args) throws Exception {
        return method.invoke(target, args);
    }
}