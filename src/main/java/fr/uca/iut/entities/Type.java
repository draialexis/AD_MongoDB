package fr.uca.iut.entities;

import fr.uca.iut.utils.TypeName;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.List;
import java.util.Objects;

@MongoEntity(collection = "types")
public class Type extends PanacheMongoEntity {
    public TypeName name;
    public List<TypeName> weakAgainst;
    public List<TypeName> effectiveAgainst;

    @Override
    public int hashCode() {
        return Objects.hash(name, weakAgainst, effectiveAgainst);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return Objects.equals(name, type.name) &&
               Objects.equals(weakAgainst, type.weakAgainst) &&
               Objects.equals(effectiveAgainst, type.effectiveAgainst);
    }
}
