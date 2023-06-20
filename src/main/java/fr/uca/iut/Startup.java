package fr.uca.iut;

import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.repositories.GenericRepository;
import fr.uca.iut.repositories.MoveRepository;
import fr.uca.iut.repositories.PokemongRepository;
import fr.uca.iut.repositories.TrainerRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class Startup {

    @Inject
    MoveRepository moveRepository;
    @Inject
    PokemongRepository pokemongRepository;
    @Inject
    TrainerRepository trainerRepository;

    void onStart(@Observes StartupEvent ev) {
        createIndex(moveRepository);
        createIndex(pokemongRepository);
        createIndex(trainerRepository);
    }

    private void createIndex(GenericRepository<? extends GenericEntity> repository) {
        try {
            repository.createIndexes();
            printIndexes(repository);
        } catch (Exception e) {
            System.err.println("Error creating indexes for repository: " + repository.getClass());
            e.printStackTrace();
        }
    }

    private void printIndexes(GenericRepository<? extends GenericEntity> repository) {
        System.out.println("indexes for " + repository.getClass());
        for (Document index : repository.getCollection().listIndexes()) {
            System.out.println(index.toJson());
        }
    }
}
