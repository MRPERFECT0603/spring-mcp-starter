package io.springmcp.runtime;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

public class McpRuntimeInitializer {

    private final ToolRegistry registry;
    private final ApplicationContext context;

    public McpRuntimeInitializer(ToolRegistry registry, ApplicationContext context) {
        this.registry = registry;
        this.context = context;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {

        System.out.println("Spring MCP: loading tools");

        ToolRegistry loaded = McpToolLoader.loadTools(context);

        loaded.getAllTools().forEach((name, invoker) -> {
            System.out.println("Registered MCP tool: " + name);
            registry.register(name, invoker);
        });

    }
}