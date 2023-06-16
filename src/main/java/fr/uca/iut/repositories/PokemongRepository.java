package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fr.uca.iut.entities.Pokemong;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PokemongRepository extends GenericRepository<Pokemong> {

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

    public List<Pokemong> findByMove(String moveId) {
        Bson filter = Filters.elemMatch("moveSet", Filters.eq("_id", new ObjectId(moveId)));
        return getCollection().find(filter)
                .into(new ArrayList<>());
    }

    @Override
    protected MongoCollection<Pokemong> getCollection() {
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        return db.getCollection(Pokemong.COLLECTION_NAME, Pokemong.class);
    }

}
