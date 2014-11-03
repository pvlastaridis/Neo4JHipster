package com.mycompany.myapp.domain;


import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * An authority (a security role) used by Spring Security.
 */

@NodeEntity
public class Authority implements Serializable {
	
	@GraphId
	Long id;

    @NotNull
    @Size(min = 0, max = 50)
    @Indexed(unique=true)
    private String name;
    
    transient private Integer hash;
       
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object other) {
        if (this == other) return true;

        if (id == null) return false;

        if (! (other instanceof Authority)) return false;

        return id.equals(((Authority) other).id);
    }

    public int hashCode() {
        if (hash == null) hash = id == null ? System.identityHashCode(this) : id.hashCode();

        return hash.hashCode();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                "}";
    }
}
