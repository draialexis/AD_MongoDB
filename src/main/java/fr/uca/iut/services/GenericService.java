package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.repositories.GenericRepository;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract service class for performing common operations on entities of type T,
 * where T extends the GenericEntity class. This class is designed to be extended by other service classes,
 * providing common functionality such as add, validate, get, delete, and update operations.
 *
 * @param <T> The type of entity this service will work with. Must extend GenericEntity.
 */
public abstract class GenericService<T extends GenericEntity> {

    protected GenericRepository<T> repository;

    /**
     * Sets the repository to be used by this service.
     *
     * @param repository The repository to be used for database operations.
     */
    public void setRepository(GenericRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Adds an entity to the repository.
     * The entity is validated before it is persisted.
     *
     * @param entity The entity to add. Must not be null.
     * @return The added entity.
     * @throws NonValidEntityException If the entity is not valid.
     */
    public T addOne(@NotNull T entity) {
        validateOne(entity);
        repository.persist(entity);
        return entity;
    }

    /**
     * Validates an entity. Throws NonValidEntityException if the entity is null.
     * This method is designed to be overridden by subclasses to provide additional validation logic.
     *
     * @param entity The entity to validate.
     * @throws NonValidEntityException If the entity is not valid.
     */
    public void validateOne(T entity) throws NonValidEntityException {
        if (entity == null) {
            throw new NonValidEntityException("entity was null");
        }
    }

    /**
     * Retrieves an entity by its ID from the repository.
     *
     * @param id The ID of the entity to retrieve.
     * @return The entity if found, or null if no entity with the given ID was found.
     */
    @Nullable
    public T getOneById(String id) {
        return repository.findById(id);
    }

    /**
     * Retrieves all entities from the repository.
     *
     * @return A list of all entities in the repository.
     */
    public List<T> getAll() {
        return repository.listAll();
    }

    /**
     * Deletes an entity by its ID from the repository.
     *
     * @param id The ID of the entity to delete.
     */
    public void deleteOneById(String id) {
        T entity = repository.findById(id);
        if (entity != null) {
            repository.delete(entity);
        }
    }

    /**
     * Updates an entity in the repository.
     * This method is designed to be overridden by subclasses to provide specific update logic.
     *
     * @param entity The entity to update. Must not be null.
     * @return The updated entity, or null if the update was not successful.
     * @throws NonValidEntityException If the entity is not valid.
     */
    @Nullable
    public T updateOne(@NotNull T entity) {
        validateOne(entity);
        return entity;
    }

    /**
     * Validates and updates a list of entities in the repository.
     * If the list of entities is not empty, each entity is validated before the list is updated in the repository.
     *
     * @param entities The list of entities to update.
     */
    public void updateAll(List<T> entities) {
        if (!entities.isEmpty()) {
            for (T entity : entities) {
                validateOne(entity);
            }
            repository.updateAll(entities);
        }
    }
}
