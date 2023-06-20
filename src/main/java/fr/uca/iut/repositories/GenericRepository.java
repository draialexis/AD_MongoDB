package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.GenericEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Abstract repository class for managing database operations for entities of type T,
 * where T extends the GenericEntity class. This class should be extended by other repository classes
 * that implement specific operations for certain types of entities.
 *
 * @param <T> The type of entity this repository manages. Must extend GenericEntity.
 */
public abstract class GenericRepository<T extends GenericEntity> {
    protected MongoClient mongoClient;
    @ConfigProperty(name = "quarkus.mongodb.database")
    String DB_NAME;

    /**
     * Sets the MongoDB client instance to be used by this repository.
     *
     * @param mongoClient The MongoDB client instance.
     */
    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return The found entity, or null if no entity with the given ID exists.
     */
    @Nullable
    public T findById(String id) {
        return getCollection().find(eq("_id", new ObjectId(id)))
                .first();
    }

    /**
     * Returns the MongoDB collection associated with this repository.
     * The specific collection depends on the type T of the entities managed by the repository.
     *
     * @return The MongoDB collection of entities of type T.
     */
    public abstract MongoCollection<T> getCollection();

    /**
     * Inserts an entity into the collection.
     *
     * @param entity The entity to insert. Must not be null.
     */
    public void persist(@NotNull T entity) {
        getCollection().insertOne(entity);
    }

    /**
     * Finds all entities in the collection.
     *
     * @return A list of all entities in the collection.
     */
    public List<T> listAll() {
        return getCollection().find()
                .into(new ArrayList<>());
    }

    /**
     * Replaces an existing entity in the collection with the provided entity.
     * If the entity does not exist, it is inserted.
     *
     * @param entity The entity to replace or insert. Must not be null.
     */
    public void persistOrUpdate(@NotNull T entity) {
        getCollection().replaceOne(
                eq("_id", new ObjectId(entity.getId())),
                entity,
                new ReplaceOptions().upsert(true)
        );
    }

    /**
     * Updates all entities in the provided list.
     * Each entity in the list replaces an existing entity in the collection with the same ID.
     * If an entity does not exist, it is inserted.
     *
     * @param entities The list of entities to update.
     */
    public void updateAll(@NotNull List<T> entities) {
        List<WriteModel<T>> updates = new ArrayList<>();
        for (T entity : entities) {
            updates.add(
                    new ReplaceOneModel<>(
                            eq("_id", new ObjectId(entity.getId())),
                            entity,
                            new ReplaceOptions().upsert(true)
                    )
            );
        }

        getCollection().bulkWrite(updates);
    }

    /**
     * Deletes an entity from the collection.
     *
     * @param entity The entity to delete. Must not be null.
     */
    public void delete(@NotNull T entity) {
        getCollection().deleteOne(eq("_id", new ObjectId(entity.getId())));
    }

    /**
     * Checks if an entity with the given ID exists in the collection.
     *
     * @param id The ID of the entity.
     * @return true if an entity with the given ID exists, false otherwise.
     */
    public boolean existsById(String id) {
        Document query = new Document("_id", new ObjectId(id));
        return getCollection().countDocuments(query) > 0;
    }

    /**
     * @return the MongoDB database
     */
    @NotNull
    public MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(DB_NAME);
    }

    public abstract void createIndexes();
}
