package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import fr.uca.iut.entities.Pokemong;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        MongoDatabase db = getMongoDatabase();
        return db.getCollection(Pokemong.COLLECTION_NAME, Pokemong.class);
    }

    @NotNull
    private MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(DB_NAME);
    }

    /**
     * Fetches the list of Pokemong entities that have a nickname matching the provided nickname.
     * The match is case-insensitive and ignores leading and trailing spaces.
     * If the nickname is null or empty, an empty list is returned.
     *
     * @param nickname the nickname to search for in the database. Can be a partial nickname.
     * @return List of Pokemong entities with a nickname matching the provided nickname. If no match is found, an empty list is returned.
     */
    public List<Pokemong> findByNickname(String nickname) {
        if (nickname != null) {
            Bson filter = Filters.regex("nickname", nickname.trim(), "i");
            return getCollection().find(filter)
                    .into(new ArrayList<>());
        }
        return new ArrayList<>();
    }

    /**
     * Returns a list of Pokemongs born within the specified date interval.
     *
     * @param startDate the start of the date interval, inclusive
     * @param endDate   the end of the date interval, inclusive
     * @return a list of Pokemongs born within the specified date interval
     */
    public List<Pokemong> findByDateOfBirthInterval(LocalDate startDate, LocalDate endDate) {
        Bson filter = Filters.and(
                Filters.gte("dob", Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())),
                Filters.lte("dob", Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        );
        return getCollection().find(filter)
                .into(new ArrayList<>());
    }

    /**
     * Counts the number of Pokemongs grouped by their evolution stage.
     *
     * @return A list of MongoDB Documents, each containing the evolution stage
     * ('evoStage') and the count of Pokemongs at that stage.
     */
    public List<Document> countPokemongsByEvoStage() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$evoStage", Accumulators.sum("count", 1)),
                // "evoStage" becomes "_id" here
                Aggregates.project(Projections.fields(
                        Projections.excludeId(),
                        // we discard that "_id" field
                        Projections.computed("evoStage", "$_id"),
                        // but we add it back in after renaming it "evoStage"
                        Projections.include("count")
                        // and of course we still want to keep the count
                ))
        );

        MongoCollection<Document> collection = getMongoDatabase().getCollection(getCollection().getNamespace().getCollectionName());
        return collection.aggregate(pipeline, Document.class).into(new ArrayList<>());
    }

}
