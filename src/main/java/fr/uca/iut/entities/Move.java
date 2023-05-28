package fr.uca.iut.entities;

import fr.uca.iut.utils.enums.MoveCategoryName;

public class Move extends GenericEntity {
    public static final String COLLECTION_NAME = "moves";

    private String name;
    private MoveCategoryName category;
    private Integer power;
    private Integer accuracy;
    private Type type;

    public Move() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MoveCategoryName getCategory() {
        return category;
    }

    public void setCategory(MoveCategoryName category) {
        this.category = category;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}