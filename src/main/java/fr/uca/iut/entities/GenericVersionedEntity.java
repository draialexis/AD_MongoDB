package fr.uca.iut.entities;

/**
 * The strategy for incrementing the schema version number is simple.
 * <br><br>
 * `schemaVersion` will have to start at 1, and need to be incremented by one at each schema change.
 * <br><br>
 * Every change to the schema needs to involve the schema version number being incremented.
 */
public abstract class GenericVersionedEntity extends GenericEntity {

    private Integer schemaVersion;

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
