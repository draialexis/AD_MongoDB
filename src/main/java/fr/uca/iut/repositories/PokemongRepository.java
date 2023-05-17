package fr.uca.iut.repositories;

import fr.uca.iut.entities.Pokemong;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PokemongRepository implements PanacheMongoRepository<Pokemong> {
}
