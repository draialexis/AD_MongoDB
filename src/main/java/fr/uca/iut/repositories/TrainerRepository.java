package fr.uca.iut.repositories;

import fr.uca.iut.entities.Trainer;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainerRepository implements PanacheMongoRepository<Trainer> {
}
