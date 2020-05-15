package de.neebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author XNEEBT
 * @version 1.0
 * @since 31.01.2017.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private SwaggerMixer swaggerMixer;

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Override
    public void run(String... args) throws Exception {
        swaggerMixer.run(objectMapper, args);
    }
}
