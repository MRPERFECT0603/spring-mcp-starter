package io.springmcp.server;

import io.springmcp.runtime.MethodInvoker;
import io.springmcp.runtime.ToolRegistry;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpHttpController {

    private final ToolRegistry registry;

    public McpHttpController(ToolRegistry registry) {
        this.registry = registry;
    }

    @PostMapping
    public Object handleRpc(@RequestBody Map<String, Object> body) throws Exception {

        String method = (String) body.get("method");
        Object id = body.get("id");

        // INITIALIZE
        if ("initialize".equals(method)) {

            Map<String, Object> result = new HashMap<>();

            result.put("protocolVersion", "2024-11-05");

            result.put("capabilities", Map.of(
                    "tools", Map.of()
            ));

            result.put("serverInfo", Map.of(
                    "name", "spring-mcp-server",
                    "version", "0.1.0"
            ));

            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);
            response.put("result", result);

            return response;
        }

        // INITIALIZED NOTIFICATION
        if ("notifications/initialized".equals(method)) {

            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);
            response.put("result", Map.of());

            return response;
        }


        // LIST TOOLS
        if ("tools/list".equals(method) || "mcp:list-tools".equals(method)) {

            List<Map<String, Object>> tools = new ArrayList<>();

            registry.getAllTools().forEach((name, invoker) -> {

                Map<String, Object> tool = new HashMap<>();

                tool.put("name", name);
                tool.put("description", "Spring MCP tool");

                Map<String, Object> schema = new HashMap<>();
                schema.put("type", "object");
                schema.put("properties", new HashMap<>());

                tool.put("inputSchema", schema);

                tools.add(tool);
            });

            Map<String, Object> result = new HashMap<>();
            result.put("tools", tools);

            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);
            response.put("result", result);

            return response;
        }

        // CALL TOOL
        if ("tools/call".equals(method) || "mcp:call-tool".equals(method)) {

            Map<String, Object> params =
                    (Map<String, Object>) body.getOrDefault("params", new HashMap<>());

            String name = (String) params.get("name");

            Map<String, Object> arguments =
                    (Map<String, Object>) params.getOrDefault("arguments", new HashMap<>());

            MethodInvoker invoker = registry.get(name);

            if (invoker == null) {
                throw new RuntimeException("Tool not found: " + name);
            }

            Object toolResult = invoker.invoke(arguments);

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            ObjectMapper mapper = new ObjectMapper();
            textContent.put("text", mapper.writeValueAsString(toolResult));

            List<Map<String, Object>> contentList = new ArrayList<>();
            contentList.add(textContent);

            Map<String, Object> result = new HashMap<>();
            result.put("content", contentList);

            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);
            response.put("result", result);

            return response;
        }

        Map<String,Object> error = new HashMap<>();
        error.put("code",-32601);
        error.put("message","Unknown method: "+method);

        Map<String,Object> response = new HashMap<>();
        response.put("jsonrpc","2.0");
        response.put("id",id);
        response.put("error",error);

        return response;
    }
}