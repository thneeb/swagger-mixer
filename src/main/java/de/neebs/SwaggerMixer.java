package de.neebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neebs.swagger.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

@Component
public class SwaggerMixer {
    @Autowired
    private ObjectMapper objectMapper;

    public void run(String... args) throws IOException {
        System.out.println("###" + Arrays.toString(args) + "###");
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
        InputStream is = getClass().getResourceAsStream(path + compose.getBasefile());
        Swagger swagger = objectMapper.readValue(is, Swagger.class);

        File file = new File("output.json");
        objectMapper.writeValue(file, swagger);
        System.out.println("Hello World");
    }
}
