package io.springmcp.model;

import java.util.Map;

public class ToolDefinition {

    private String name;
    private String description;

    private String className;
    private String methodName;

    private boolean enabled = true;

    private Map<String, Object> inputSchema;

    public ToolDefinition() {}

    public ToolDefinition(
            String name,
            String description,
            String className,
            String methodName,
            Map<String, Object> inputSchema
    ) {
        this.name = name;
        this.description = description;
        this.className = className;
        this.methodName = methodName;
        this.inputSchema = inputSchema;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getClassName() { return className; }
    public String getMethodName() { return methodName; }
    public Map<String, Object> getInputSchema() { return inputSchema; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}