package io.springmcp.runtime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.springmcp.model.ToolDefinition;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolMetadataStore {

    private static final Map<String, ToolDefinition> tools = new HashMap<>();

    public static void setEnabled(String name, boolean enabled) {
        ToolDefinition t = tools.get(name);
        if (t != null) {
            t.setEnabled(enabled);
        }
    }

    public static void load(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            List<ToolDefinition> list = mapper.readValue(
                    new File(path),
                    new TypeReference<List<ToolDefinition>>() {}
            );

            for (ToolDefinition t : list) {
                tools.put(t.getName(), t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ToolDefinition get(String name) {
        return tools.get(name);
    }
}