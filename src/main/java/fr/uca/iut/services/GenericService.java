package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.repositories.GenericRepository;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GenericService<T extends GenericEntity> {

    protected GenericRepository<T> repository;

    public void setRepository(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public T addOne(@NotNull T entity) {
        repository.persist(entity);
        return entity;
    }

    @Nullable
    public T getOneById(String id) {
        return repository.findById(id);
    }

    public List<T> getAll() {
        return repository.listAll();
    }

    public void deleteOneById(String id) {
        T entity = repository.findById(id);
        if (entity != null) {
            repository.delete(entity);
        }
    }

    @Nullable
    public abstract T updateOne(@NotNull T entity);

    /**
     * Override me and start with `super.validateOne(entity);`
     */
    public void validateOne(T entity) {
        if (entity == null) {
            throw new NonValidEntityException("entity was null");
        }
    }
}
