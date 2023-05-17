package fr.uca.iut.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "moves")
public class Move extends PanacheMongoEntity {
    public String name;
    public String category;
    public Integer power;
    public Integer accuracy;
    public Type type;
}