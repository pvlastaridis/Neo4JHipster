package com.mycompany.myapp.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.io.Serializable;

/**
 * A Publication.
 */
@SuppressWarnings("serial")
@NodeEntity
public class Publication implements Serializable {

	@GraphId
    private Long id;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

   
    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", title='" + title + "'" +
                '}';
    }
}
