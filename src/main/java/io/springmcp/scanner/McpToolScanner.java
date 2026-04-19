package io.springmcp.scanner;

import io.github.classgraph.*;
import io.springmcp.annotation.McpExpose;
import io.springmcp.model.ToolDefinition;
import java.lang.reflect.Field;
import java.util.HashSet;

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
                    scanResult.getClassesWithMethodAnnotation(McpExpose.class.getName())) {

                for (MethodInfo methodInfo : classInfo.getMethodInfo()) {

                    if (methodInfo.hasAnnotation(McpExpose.class.getName())) {

                        AnnotationInfo annotationInfo =
                                methodInfo.getAnnotationInfo(McpExpose.class.getName());

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

            Map<String, Object> propSchema;

            try {
                // Load actual class of parameter
                String typeName = param.getTypeSignatureOrTypeDescriptor().toString();

                // Fix JVM descriptor → Java class name
                typeName = typeName
                        .replace("/", ".")
                        .replace(";", "")
                        .replace("L", "");

                Class<?> paramClass = Class.forName(typeName);

                propSchema = generateSchemaFromClass(paramClass, new HashSet<>());

            } catch (Exception e) {
                // fallback
                propSchema = Map.of("type", "object");
            }

            properties.put(param.getName(), propSchema);
        }

        schema.put("properties", properties);

        return schema;
    }

    private Map<String, Object> generateSchemaFromClass(
            Class<?> clazz,
            Set<Class<?>> visited
    ) {

        if (visited.contains(clazz)) {
            return Map.of("type", "object"); // prevent infinite recursion
        }

        visited.add(clazz);

        // Primitive types
        if (isPrimitive(clazz)) {
            return Map.of("type", mapPrimitive(clazz));
        }

        // List handling (basic)
        if (List.class.isAssignableFrom(clazz)) {
            return Map.of(
                    "type", "array",
                    "items", Map.of("type", "object")
            );
        }

        Map<String, Object> schema = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();

        schema.put("type", "object");

        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {

            Class<?> fieldType = field.getType();
            Map<String, Object> fieldSchema;

            if (isPrimitive(fieldType)) {
                fieldSchema = Map.of("type", mapPrimitive(fieldType));
            } else {
                fieldSchema = generateSchemaFromClass(fieldType, visited);
            }

            properties.put(field.getName(), fieldSchema);
        }

        schema.put("properties", properties);

        return schema;
    }

    private boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }

    private String mapPrimitive(Class<?> type) {

        if (type == String.class || type == Character.class) return "string";

        if (type == int.class || type == Integer.class
                || type == long.class || type == Long.class) return "integer";

        if (type == double.class || type == Double.class
                || type == float.class || type == Float.class) return "number";

        if (type == boolean.class || type == Boolean.class) return "boolean";

        return "string";
    }
}