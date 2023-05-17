package fr.uca.iut.repositories;

import fr.uca.iut.entities.Move;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MoveRepository implements PanacheMongoRepository<Move> {
}
