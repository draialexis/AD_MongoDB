package fr.uca.iut.entities;

import fr.uca.iut.entities.denormalized.TrainerPokemong;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Trainer extends GenericVersionedEntity {
    public static final String COLLECTION_NAME = "trainers";
    public static final Integer LATEST_SCHEMA_VERSION = 1;

    private String name;
    private LocalDate dob;
    private Integer wins;
    private Integer losses;
    private List<String> pastOpponents;
    private Set<TrainerPokemong> pokemongs;

    public Trainer() {
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

    public List<String> getPastOpponents() {
        return Collections.unmodifiableList(pastOpponents);
    }

    public void setPastOpponents(List<String> pastOpponents) {
        this.pastOpponents = pastOpponents;
    }

    public Set<TrainerPokemong> getPokemongs() {
        return Collections.unmodifiableSet(pokemongs);
    }

    public void setPokemongs(Set<TrainerPokemong> pokemongs) {
        this.pokemongs = pokemongs;
    }

    public void addPokemong(TrainerPokemong trainerPokemong) {
        pokemongs.add(trainerPokemong);
    }

    public void removePokemong(String id) {
        pokemongs.removeIf(trainerPokemong -> trainerPokemong.getId().equals(id));
    }
}
