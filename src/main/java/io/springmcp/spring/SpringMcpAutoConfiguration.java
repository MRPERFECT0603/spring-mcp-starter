package io.springmcp.spring;

import io.springmcp.runtime.McpToolLoader;
import io.springmcp.runtime.ToolRegistry;
import io.springmcp.server.SpringMcpServer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringMcpAutoConfiguration {

    @Bean
    public SpringMcpServer springMcpServer(ApplicationContext context) throws Exception {

        ToolRegistry registry = McpToolLoader.loadTools(context);

        SpringMcpServer server = new SpringMcpServer(registry);

        new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return server;
    }
}