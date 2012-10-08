package com.abelsky.complitex.tasklist.model;

import java.io.Serializable;

/**
 * POJO representing task owner / creator with registration data.
 *
 * @author andy
 */
@SuppressWarnings("UnusedDeclaration")
public class Profile implements Serializable {

    private int id;

    private String name;

    private String email;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
