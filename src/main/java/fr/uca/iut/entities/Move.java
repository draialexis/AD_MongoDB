package fr.uca.iut.entities;

import org.bson.codecs.pojo.annotations.BsonId;

public class Move extends GenericEntity {
    public static final String COLLECTION_NAME = "moves";

    @BsonId
    private String id;
    private String name;
    private String category;
    private Integer power;
    private Integer accuracy;
    private Type type;

    public Move() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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