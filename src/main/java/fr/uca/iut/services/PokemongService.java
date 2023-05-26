package fr.uca.iut.services;

import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.repositories.PokemongRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PokemongService {

    @Inject
    PokemongRepository pokemongRepository;

    public Pokemong addPokemong(Pokemong pokemong) {
        pokemongRepository.persist(pokemong);
        return pokemong;
    }

    public Pokemong getPokemong(String id) {
        return pokemongRepository.findById(id);
    }

    public List<Pokemong> getAllPokemongs() {
        return pokemongRepository.listAll();
    }

    public void deletePokemong(String id) {
        Pokemong pokemong = pokemongRepository.findById(id);
        if (pokemong != null) {
            pokemongRepository.delete(pokemong);
        }
    }

    public Pokemong updatePokemong(Pokemong pokemong) {
        Pokemong existingPokemong = pokemongRepository.findById(pokemong.getId());
        if (existingPokemong != null) {
            existingPokemong.setNickname(pokemong.getNickname());
            existingPokemong.setDob(pokemong.getDob());
            existingPokemong.setLevel(pokemong.getLevel());
            existingPokemong.setPokedexId(pokemong.getPokedexId());
            existingPokemong.setEvoStage(pokemong.getEvoStage());
            existingPokemong.setEvoTrack(pokemong.getEvoTrack());
            existingPokemong.setMegaEvolved(pokemong.getMegaEvolved());
            existingPokemong.setTrainer(pokemong.getTrainer());
            existingPokemong.setTypes(pokemong.getTypes());
            existingPokemong.setMoveSet(pokemong.getMoveSet());
            pokemongRepository.persistOrUpdate(existingPokemong);
        }
        return existingPokemong;
    }

    public boolean isNotMature(Pokemong pokemong) {
        return pokemong == null
               || pokemong.getEvoStage() == null
               || pokemong.getEvoTrack() == null
               || pokemong.getEvoTrack()
                          .isEmpty()
               || (pokemong.getEvoStage() != pokemong.getEvoTrack()
                                                     .size() - 1);
    }

    // TODO PATCH ?
}
