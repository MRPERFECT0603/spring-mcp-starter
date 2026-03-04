package io.springmcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.springmcp.runtime.MethodInvoker;
import io.springmcp.runtime.ToolRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpringMcpServer {

    private final ToolRegistry registry;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpringMcpServer(ToolRegistry registry) {
        this.registry = registry;
    }

    public void start() throws Exception {

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        String line;

        while ((line = reader.readLine()) != null) {

            System.out.println("MCP request: " + line);

            // simple protocol example
        }
    }

}