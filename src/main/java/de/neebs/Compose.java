package de.neebs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Compose {
    private String basefile;
    private List<Enhancement> enhancements;
}
