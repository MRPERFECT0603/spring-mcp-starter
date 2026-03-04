package io.springmcp.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.springmcp.model.ToolDefinition;

import java.io.File;
import java.util.List;

public class ToolMetadataGenerator {

    public void generate(List<ToolDefinition> tools, File outputDir) throws Exception {

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        ObjectMapper mapper = new ObjectMapper();

        File file = new File(outputDir, "tools.json");

        mapper.writeValue(file, tools);

    }
}