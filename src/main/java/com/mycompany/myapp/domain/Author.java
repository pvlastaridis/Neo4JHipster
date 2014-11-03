package com.mycompany.myapp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.mycompany.myapp.domain.util.CustomLocalDateSerializer;

import org.joda.time.LocalDate;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Author.
 */
@NodeEntity
public class Author implements Serializable {

	@GraphId
	Long id;

    private String name;

    private int noOfBooksPublished;

    private Long weight;

    private Double height;
    
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private Long dateOfBirth;

    private Boolean married;

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

    public Integer getNoOfBooksPublished() {
        return noOfBooksPublished;
    }

    public void setNoOfBooksPublished(Integer noOfBooksPublished) {
        this.noOfBooksPublished = noOfBooksPublished;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return BigDecimal.valueOf(height);
    }

    public void setHeight(BigDecimal height) {
        this.height = height.doubleValue();
    }

    public LocalDate getDateOfBirth() {
        return new LocalDate(dateOfBirth);
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth.toDate().getTime();
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
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
        return "Author{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", noOfBooksPublished='" + noOfBooksPublished + "'" +
                ", weight='" + weight + "'" +
                ", height='" + height + "'" +
                ", dateOfBirth='" + dateOfBirth + "'" +
                ", married='" + married + "'" +
                '}';
    }
}
