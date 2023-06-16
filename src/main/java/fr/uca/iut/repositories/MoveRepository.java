package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.uca.iut.entities.Move;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MoveRepository extends GenericRepository<Move> {

    /**
     * Warns that "Unsatisfied dependency: no bean matches the injection point"
     * but the app works
     */
    @Inject
    MongoClient mongoClient;

    @PostConstruct
    public void init() {
        setMongoClient(mongoClient);
    }

    @Override
    protected MongoCollection<Move> getCollection() {
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        return db.getCollection(Move.COLLECTION_NAME, Move.class);
    }
}
