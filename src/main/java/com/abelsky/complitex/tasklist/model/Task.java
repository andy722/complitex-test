package com.abelsky.complitex.tasklist.model;

import java.io.Serializable;

/**
 * POJO representing an individual task.
 *
 * @author andy
 */
@SuppressWarnings("UnusedDeclaration")
public class Task implements Serializable {

    private int id;

    /**
     * ID of the task's creator.
     */
    private int creatorId;

    /**
     * ID of the task's owner (the user which is currently assigned to it).
     */
    private int ownerId;

    /**
     * ID of the project this task belongs to.
     */
    private int projectId;

    private TaskState state;

    /**
     * Task details.
     */
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

}
