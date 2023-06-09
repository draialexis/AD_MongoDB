package fr.uca.iut.entities;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Objects;

/**
 * Abstract entity class that provides common properties and methods for all entities in the system.
 * Entities extending this class will automatically have an ID field, along with associated getter and setter,
 * and overridden hashCode and equals methods based on the ID field.
 */
public abstract class GenericEntity {

    @BsonId
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericEntity entity = (GenericEntity) o;
        return Objects.equals(id, entity.id);
    }
}
