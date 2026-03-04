package io.springmcp.runtime;

import java.util.HashMap;
import java.util.Map;

public class ToolRegistry {

    private final Map<String, MethodInvoker> tools = new HashMap<>();

    public void register(String name, MethodInvoker invoker) {
        tools.put(name, invoker);
    }

    public MethodInvoker get(String name) {
        return tools.get(name);
    }

    public Map<String, MethodInvoker> getAllTools() {
        return tools;
    }
}