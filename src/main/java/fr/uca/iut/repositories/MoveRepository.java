package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
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
    public MongoCollection<Move> getCollection() {
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        return db.getCollection(Move.COLLECTION_NAME, Move.class);
    }

    @Override
    public void createIndexes() {
        getCollection().createIndex(Indexes.ascending("name"));
        getCollection().createIndex(Indexes.descending("power"));
        getCollection().createIndex(Indexes.ascending("type"));
    }
}
