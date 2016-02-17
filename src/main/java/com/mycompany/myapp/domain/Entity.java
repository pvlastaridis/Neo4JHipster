package com.mycompany.myapp.domain;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
public abstract class Entity {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * FIXME:
     * This is the default mechanism for providing entity identity to the OGM
     *
     * It is required because the OGM can currently accept objects with NO
     * id value set. This is a restriction that must be changed
     *
     * @param o the object to compare, either or both may not yet be persisted.
     * @return true when nodes are the same node
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || id == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }
}
