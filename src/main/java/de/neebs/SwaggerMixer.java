package de.neebs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neebs.swagger.Definition;
import de.neebs.swagger.OpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

@Component
public class SwaggerMixer {
    @Autowired
    private ObjectMapper objectMapper;

    public void run(String... args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("You need to pass the compose file.");
            return;
        }
        final String filename = args[0];
        Compose compose = objectMapper.readValue(getClass().getResourceAsStream(filename), Compose.class);
        final String path;
        if (filename.contains("/")) {
            path = filename.substring(0, filename.lastIndexOf("/") + 1);
        } else {
            path = "";
        }
        final String basefile;
        try (InputStream is = getClass().getResourceAsStream(path + compose.getBasefile())) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            basefile = s.hasNext() ? s.next() : "";
        }
        Map<String, Object> test = objectMapper.readValue(basefile, new TypeReference<Map<String, Object>>() {});
        if (test.get("swagger") != null || test.get("openapi") == null) {
            System.out.println("Swagger files are not supported. Please upgrade file to OpenAPI 3.x before using it here.");
            return;
        }
        OpenApi swagger = objectMapper.readValue(basefile, OpenApi.class);
        if (compose.getEnhancements() == null || compose.getEnhancements().size() == 0) {
            System.out.println("No enhancements listed in the file.");
            return;
        }
        for (Enhancement enhancement : compose.getEnhancements()) {
            try (InputStream is = getClass().getResourceAsStream(path + enhancement.getSchemaLocation())) {
                Map<String, Definition> definitionMap = objectMapper.readValue(is, new TypeReference<Map<String, Definition>>() {});
            }
        }

//        File file = new File("output.json");
//        objectMapper.writeValue(file, swagger);
        System.out.println("Hello World");
    }
}
