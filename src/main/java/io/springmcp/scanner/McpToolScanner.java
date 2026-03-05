package io.springmcp.scanner;

import io.github.classgraph.*;
import io.springmcp.annotation.McpTool;
import io.springmcp.model.ToolDefinition;

import java.io.File;
import java.util.*;

public class McpToolScanner {

    public List<ToolDefinition> scan(List<File> classpath, String basePackage) {

        List<ToolDefinition> tools = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .overrideClasspath(classpath)
                .enableAllInfo()
                .acceptPackages(basePackage)
                .scan()) {

            for (ClassInfo classInfo :
                    scanResult.getClassesWithMethodAnnotation(McpTool.class.getName())) {

                for (MethodInfo methodInfo : classInfo.getMethodInfo()) {

                    if (methodInfo.hasAnnotation(McpTool.class.getName())) {

                        AnnotationInfo annotationInfo =
                                methodInfo.getAnnotationInfo(McpTool.class.getName());

                        String name = annotationInfo
                                .getParameterValues()
                                .getValue("name")
                                .toString();

                        String description = "";

                        Object descVal =
                                annotationInfo.getParameterValues()
                                        .getValue("description");

                        if (descVal != null) {
                            description = descVal.toString();
                        }

                        Map<String, Object> schema =
                                generateInputSchema(methodInfo);

                        tools.add(
                                new ToolDefinition(
                                        name,
                                        description,
                                        classInfo.getName(),
                                        methodInfo.getName(),
                                        schema
                                )
                        );
                    }
                }
            }
        }

        return tools;
    }

    private Map<String, Object> generateInputSchema(MethodInfo methodInfo) {

        Map<String, Object> schema = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();

        schema.put("type", "object");

        for (MethodParameterInfo param : methodInfo.getParameterInfo()) {

            Map<String, Object> prop = new HashMap<>();

            prop.put("type", "object");

            properties.put(param.getName(), prop);
        }

        schema.put("properties", properties);

        return schema;
    }
}