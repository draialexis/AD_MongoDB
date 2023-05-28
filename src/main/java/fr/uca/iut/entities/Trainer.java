package fr.uca.iut.entities;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Trainer extends GenericEntity {
    public static final String COLLECTION_NAME = "trainers";

    private String name;
    private LocalDate dob;
    private Integer wins;
    private Integer losses;
    private List<String> pastOpponents;
    private List<TrainerPokemong> pokemongs;

    public Trainer() {}

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

    public List<String> getPastOpponents() {
        return Collections.unmodifiableList(pastOpponents);
    }

    public void setPastOpponents(List<String> pastOpponents) {
        this.pastOpponents = pastOpponents;
    }

    public List<TrainerPokemong> getPokemongs() {
        return Collections.unmodifiableList(pokemongs);
    }

    public void setPokemongs(List<TrainerPokemong> pokemongs) {
        this.pokemongs = pokemongs;
    }
}
