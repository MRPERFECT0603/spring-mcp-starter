package io.springmcp.spring;

import io.springmcp.runtime.McpRuntimeInitializer;
import io.springmcp.runtime.ToolRegistry;
import io.springmcp.server.McpHttpController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringMcpAutoConfiguration {

    @Bean
    public ToolRegistry toolRegistry() {
        return new ToolRegistry();
    }

    @Bean
    public McpRuntimeInitializer runtimeInitializer(
            ToolRegistry registry,
            ApplicationContext context
    ) {
        return new McpRuntimeInitializer(registry, context);
    }

    @Bean
    public McpHttpController mcpHttpController(ToolRegistry registry) {
        return new McpHttpController(registry);
    }
}