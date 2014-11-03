package com.mycompany.myapp.domain;


import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */

public abstract class AbstractAuditingEntity {

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Long createdDate = DateTime.now().getMillis();

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Long lastModifiedDate = DateTime.now().getMillis();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
        return new DateTime(createdDate);
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate.getMillis();
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DateTime getLastModifiedDate() {
        return new DateTime(lastModifiedDate);
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate.getMillis();
    }
}
