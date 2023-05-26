package fr.uca.iut.entities;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public class Trainer extends GenericEntity {
    public static final String COLLECTION_NAME = "trainers";

    @BsonId
    private String id;
    private String name;
    private LocalDate dob;
    private Integer wins;
    private Integer losses;
    private List<ObjectId> pastOpponents;
    private List<ObjectId> pokemongs;

    public Trainer() {}

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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public List<ObjectId> getPastOpponents() {
        return pastOpponents;
    }

    public void setPastOpponents(List<ObjectId> pastOpponents) {
        this.pastOpponents = pastOpponents;
    }

    public List<ObjectId> getPokemongs() {
        return pokemongs;
    }

    public void setPokemongs(List<ObjectId> pokemongs) {
        this.pokemongs = pokemongs;
    }
}
