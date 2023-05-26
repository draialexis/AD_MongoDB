package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import fr.uca.iut.entities.Pokemong;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class PokemongRepository {

    // FIXME? or suppress warning: "Unsatisfied dependency: no bean matches the injection point"
    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String DB_NAME;

    private MongoCollection<Pokemong> getCollection() {
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        return db.getCollection(Pokemong.COLLECTION_NAME, Pokemong.class);
    }

    public Pokemong findById(String id) {
        return getCollection().find(eq("_id", new ObjectId(id)))
                              .first();
    }

    public void persist(Pokemong pokemong) {
        getCollection().insertOne(pokemong);
    }

    public List<Pokemong> listAll() {
        return getCollection().find()
                              .into(new ArrayList<>());
    }

    public void delete(Pokemong pokemong) {
        getCollection().deleteOne(eq("_id", new ObjectId(pokemong.getId())));
    }

    public void persistOrUpdate(Pokemong pokemong) {
        getCollection().replaceOne(
                eq("_id", new ObjectId(pokemong.getId())),
                pokemong,
                new ReplaceOptions().upsert(true)
        );
    }
}
