package fr.uca.iut.entities.embedded;

import fr.uca.iut.utils.enums.TypeName;

import java.util.List;
import java.util.Objects;

public class Type {

    private TypeName name;
    private List<TypeName> weakAgainst;
    private List<TypeName> effectiveAgainst;

    public Type() {
    }

    public TypeName getName() {
        return name;
    }

    public void setName(TypeName name) {
        this.name = name;
    }

    public List<TypeName> getWeakAgainst() {
        return weakAgainst;
    }

    public void setWeakAgainst(List<TypeName> weakAgainst) {
        this.weakAgainst = weakAgainst;
    }

    public List<TypeName> getEffectiveAgainst() {
        return effectiveAgainst;
    }

    public void setEffectiveAgainst(List<TypeName> effectiveAgainst) {
        this.effectiveAgainst = effectiveAgainst;
    }

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