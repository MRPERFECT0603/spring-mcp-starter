package io.springmcp.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class MethodInvoker {

    private final Object target;
    private final Method method;

    private final ObjectMapper mapper = new ObjectMapper();

    public MethodInvoker(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object invoke(Map<String, Object> arguments) throws Exception {

        Parameter[] parameters = method.getParameters();

        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            Parameter param = parameters[i];

            Object value = arguments.get(param.getName());

            if (value != null) {
                args[i] = mapper.convertValue(value, param.getType());
            } else {
                args[i] = null;
            }
        }

        return method.invoke(target, args);
    }
}