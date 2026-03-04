package io.springmcp.plugin;

import io.springmcp.generator.ToolMetadataGenerator;
import io.springmcp.model.ToolDefinition;
import io.springmcp.scanner.McpToolScanner;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpringMcpPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getTasks().register("generateMcp", task -> {

            task.dependsOn("classes");
            task.doLast(t -> {

                System.out.println("Spring MCP: scanning project for @McpTool");

                File mcpDir = new File(project.getProjectDir(), "mcp");

                if (!mcpDir.exists()) {
                    mcpDir.mkdirs();
                }

                try {

                    // scan project classes
                    File classesDir = new File(
                            project.getBuildDir(),
                            "classes/java/main"
                    );

                    List<File> classpath = new ArrayList<>();

                    classpath.add(classesDir);

// add runtime dependencies
                    project.getConfigurations()
                            .getByName("runtimeClasspath")
                            .forEach(classpath::add);

                    McpToolScanner scanner = new McpToolScanner();

                    List<ToolDefinition> tools =
                            scanner.scan(classpath, "com.projects");

                    // generate tools.json
                    ToolMetadataGenerator generator =
                            new ToolMetadataGenerator();

                    generator.generate(tools, mcpDir);

                    System.out.println("MCP tools generated: " + tools.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        });

    }
}