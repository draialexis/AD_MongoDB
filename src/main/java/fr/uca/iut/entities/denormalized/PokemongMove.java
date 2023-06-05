package fr.uca.iut.entities.denormalized;

import fr.uca.iut.entities.GenericEntity;

public class PokemongMove extends GenericEntity {

    private String name;

    public PokemongMove() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
