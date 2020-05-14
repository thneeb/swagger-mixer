package de.neebs.swagger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Swagger {
    private String swagger;
    private Map<String, String> info;
    private String host;
    private String basePath;
    private List<String> consumes;
    private List<String> produces;
    private List<Tag> tags;
    private Map<String, Object> paths;
    private Map<String, Definition> definitions;
}
