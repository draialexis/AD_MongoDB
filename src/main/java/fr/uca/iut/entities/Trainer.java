package fr.uca.iut.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "trainers")
public class Trainer extends PanacheMongoEntity {
    public String name;
    public LocalDate dob;
    public Integer wins;
    public Integer losses;
    public List<ObjectId> pastOpponents;
    public List<ObjectId> pokemongs;
}
