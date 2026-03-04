package io.springmcp.model;


public class ToolDefinition {

    private String name;
    private String description;

    public ToolDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}