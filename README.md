# spring-mcp-starter

A Gradle plugin that scans compiled Java classes for methods annotated with `@McpTool` and generates MCP tool metadata at `mcp/tools.json`.

## What it does

- Registers a Gradle task: `generateMcp`
- Runs after `classes`
- Scans runtime classpath for methods with `io.springmcp.annotation.McpTool`
- Writes discovered tool definitions (`name`, `description`) to `mcp/tools.json`

## Requirements

- Java 21
- Gradle (wrapper included)

## Build this plugin

```bash
./gradlew clean build
```

To publish to local Maven:

```bash
./gradlew publishToMavenLocal
```

## Use in another Gradle project

If published to local Maven, apply by plugin id:

```gradle
plugins {
    id 'io.springmcp.plugin' version '0.1.0'
}
```

Run:

```bash
./gradlew generateMcp
```

## Annotating MCP tools

Use `@McpTool` on methods:

```java
import io.springmcp.annotation.McpTool;

public class WeatherTools {

    @McpTool(name = "get_weather", description = "Get weather by city")
    public String getWeather(String city) {
        return "...";
    }
}
```

Generated output example (`mcp/tools.json`):

```json
[
  {
    "name": "get_weather",
    "description": "Get weather by city"
  }
]
```

## Current behavior and limitation

- Scanner base package is currently hardcoded to `com.projects` in `SpringMcpPlugin`.
- Only methods in classes under that package are discovered.

If your code is in a different package, update the scanner base package in:

- `src/main/java/io/springmcp/plugin/SpringMcpPlugin.java`
