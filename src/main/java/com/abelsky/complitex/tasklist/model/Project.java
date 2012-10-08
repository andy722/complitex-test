package com.abelsky.complitex.tasklist.model;

import java.io.Serializable;

/**
 * POJO representing a project.
 *
 * @author andy
 */
@SuppressWarnings("UnusedDeclaration")
public class Project implements Serializable {

    private int id;

    /**
     * Name of the project, mandatory.
     */
    private String name;

    /**
     * Optional description.
     */
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
