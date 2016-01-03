package com.mycompany.myapp.domain;

/**
 * Used to represent a Map Property
 * Created by Panos on 01-Jan-16.
 */
public class PersistentAuditEventData extends Entity{

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
