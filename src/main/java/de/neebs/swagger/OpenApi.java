package de.neebs.swagger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApi {
    private String openapi;
    private Map<String, Object> info;
    private List<Server> servers;
    private List<Tag> tags;
    private Map<String, Object> paths;
    private Components components;
}
