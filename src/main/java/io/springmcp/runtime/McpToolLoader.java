package io.springmcp.runtime;

import io.springmcp.annotation.McpExpose;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class McpToolLoader {

    public static ToolRegistry loadTools(ApplicationContext context) {

        ToolRegistry registry = new ToolRegistry();

        String[] beans = context.getBeanDefinitionNames();

        for (String beanName : beans) {

            Object bean = context.getBean(beanName);

            Method[] methods = bean.getClass().getDeclaredMethods();

            for (Method method : methods) {

                if (method.isAnnotationPresent(McpExpose.class)) {

                    McpExpose annotation = method.getAnnotation(McpExpose.class);

                    registry.register(
                            annotation.name(),
                            new MethodInvoker(bean, method)
                    );
                }
            }
        }

        return registry;
    }
}