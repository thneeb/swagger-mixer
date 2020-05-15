package de.neebs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neebs.swagger.Definition;
import de.neebs.swagger.Discriminator;
import de.neebs.swagger.OpenApi;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class SwaggerMixer {
    public void run(ObjectMapper objectMapper, String... args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("You need to pass the compose file.");
            return;
        }
        final String filename = args[0];
        Compose compose = objectMapper.readValue(new File(filename), Compose.class);
        final String path;
        if (filename.contains("/")) {
            path = filename.substring(0, filename.lastIndexOf("/") + 1);
        } else {
            path = "";
        }
        final String basefile;
        try (InputStream is = new FileInputStream(path + compose.getBasefile())) {
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
            Definition definition = swagger.getComponents().getSchemas().get(enhancement.getEntity());
            Discriminator discriminator = definition.getDiscriminator();
            if (discriminator == null) {
                definition.setDiscriminator(new Discriminator(enhancement.getAttribute()));
            } else if (!discriminator.getPropertyName().equals(enhancement.getAttribute())) {
                throw new IllegalStateException("Discriminator of " + enhancement.getEntity() + " has wrong propertyName: " + discriminator.getPropertyName() + "vs " + enhancement.getAttribute());
            }
            Map<String, Definition> definitionMap = objectMapper.readValue(new File(path + enhancement.getSchemaLocation()), new TypeReference<Map<String, Definition>>() {});
            final Definition derivedEntity;
            if (enhancement.getDerivedEntity() == null && definitionMap.size() == 1) {
                derivedEntity = definitionMap.values().iterator().next();
            } else if (enhancement.getDerivedEntity() != null) {
                derivedEntity = definitionMap.get(enhancement.getDerivedEntity());
            } else {
                throw new IllegalStateException("Cannot identify the derived entity. Please specify property 'derivedEntity'.");
            }
            if (derivedEntity == null) {
                throw new IllegalStateException("Derived entity not found");
            }
            if (derivedEntity.getAllOf() == null) {
                derivedEntity.setAllOf(new ArrayList<>());
            }
            if (derivedEntity.getAllOf().size() == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("$ref", "#/components/schemas/" + enhancement.getEntity());
                derivedEntity.getAllOf().add(map);
            }
            swagger.getComponents().getSchemas().putAll(definitionMap);
        }

        final String outputFile;
        if (args.length > 1) {
            outputFile = args[1];
        } else {
            outputFile = "output.json";
        }
        File file = new File(outputFile);
        objectMapper.writeValue(file, swagger);
        System.out.println("Hello World");
    }
}
