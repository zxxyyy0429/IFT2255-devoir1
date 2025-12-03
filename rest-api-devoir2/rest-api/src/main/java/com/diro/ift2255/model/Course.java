package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;
    private String name;
    private String description;

    public Course() {}

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String id, String name, String desc) {
        this(id, name);
        this.description = desc;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description;}
}
