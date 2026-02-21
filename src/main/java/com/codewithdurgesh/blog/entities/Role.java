package com.codewithdurgesh.blog.entities;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    private Integer id;

    private String name;

    public Role() {}

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}