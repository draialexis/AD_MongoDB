package fr.uca.iut.services;

import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.repositories.PokemongRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class PokemongService {

    @Inject
    PokemongRepository pokemongRepository;

    public Pokemong addPokemong(Pokemong pokemong) {
        pokemongRepository.persist(pokemong);
        return pokemong;
    }

    public Pokemong getPokemong(ObjectId id) {
        return pokemongRepository.findById(id);
    }

    public List<Pokemong> getAllPokemongs() {
        return pokemongRepository.listAll();
    }

    public void deletePokemong(ObjectId id) {
        Pokemong pokemong = pokemongRepository.findById(id);
        if (pokemong != null) {
            pokemongRepository.delete(pokemong);
        }
    }

    public Pokemong updatePokemong(Pokemong pokemong) {
        Pokemong existingPokemong = pokemongRepository.findById(pokemong.id);
        if (existingPokemong != null) {
            existingPokemong.nickname = pokemong.nickname;
            existingPokemong.dob = pokemong.dob;
            existingPokemong.level = pokemong.level;
            existingPokemong.pokedexId = pokemong.pokedexId;
            existingPokemong.evoStage = pokemong.evoStage;
            existingPokemong.evoTrack = pokemong.evoTrack;
            existingPokemong.isMegaEvolved = pokemong.isMegaEvolved;
            existingPokemong.trainer = pokemong.trainer;
            existingPokemong.types = pokemong.types;
            existingPokemong.moveSet = pokemong.moveSet;
            pokemongRepository.persistOrUpdate(existingPokemong);
        }
        return existingPokemong;
    }

    public boolean isNotMature(Pokemong pokemong) {
        return pokemong == null
               || pokemong.evoStage == null
               || pokemong.evoTrack == null
               || pokemong.evoTrack.isEmpty()
               || (pokemong.evoStage != pokemong.evoTrack.size() - 1);
    }

    // TODO PATCH ?
}
