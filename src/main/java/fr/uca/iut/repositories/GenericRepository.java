package fr.uca.iut.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
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

public abstract class GenericRepository<T extends GenericEntity> {
    protected MongoClient mongoClient;
    @ConfigProperty(name = "quarkus.mongodb.database")
    String DB_NAME;

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Nullable
    public T findById(String id) {
        return getCollection().find(eq("_id", new ObjectId(id)))
                .first();
    }

    protected abstract MongoCollection<T> getCollection();

    public void persist(@NotNull T entity) {
        getCollection().insertOne(entity);
    }

    public List<T> listAll() {
        return getCollection().find()
                .into(new ArrayList<>());
    }

    public void persistOrUpdate(@NotNull T entity) {
        getCollection().replaceOne(
                eq("_id", new ObjectId(entity.getId())),
                entity,
                new ReplaceOptions().upsert(true)
        );
    }

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

    public void delete(@NotNull T entity) {
        getCollection().deleteOne(eq("_id", new ObjectId(entity.getId())));
    }

    public boolean existsById(String id) {
        Document query = new Document("_id", new ObjectId(id));
        return getCollection().countDocuments(query) > 0;
    }
}
