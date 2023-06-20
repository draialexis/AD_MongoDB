package fr.uca.iut.controllers;

import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.services.GenericService;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * An abstract controller class providing CRUD operations for entities.
 * The specific type of entity is defined by the generic parameter T, which extends GenericEntity.
 */
public abstract class GenericController<T extends GenericEntity> {

    protected GenericService<T> service;

    /**
     * Sets the service to be used by this controller.
     *
     * @param service The service to be used by this controller.
     */
    public void setService(GenericService<T> service) {
        this.service = service;
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return A Response object containing the entity, or an error message if the entity does not exist.
     */
    @GET
    @Path("/{id}")
    public Response getOneById(@PathParam("id") String id) {
        try {
            T entity = service.getOneById(id);
            if (entity != null) {
                return Response.ok(entity)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Entity not found for id: " + id)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return A Response object containing a list of all entities.
     */
    @GET
    public Response getAll() {
        return Response.ok(service.getAll())
                .build();
    }

    /**
     * Creates a new entity.
     *
     * @param entity The entity to create.
     * @return A Response object containing the created entity, or an error message if the entity is not valid.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOne(T entity) {

        try {
            T newEntity = service.addOne(entity);

            return Response.status(Response.Status.CREATED)
                    .entity(newEntity)
                    .build();

        } catch (NonValidEntityException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param id     The ID of the entity to update.
     * @param entity The updated entity.
     * @return A Response object containing the updated entity, or an error message if the entity is not found or not valid.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOne(@PathParam("id") String id, T entity) {
        try {
            entity.setId(id);
            T updatedEntity = service.updateOne(entity);

            if (updatedEntity != null) {
                return Response.status(Response.Status.OK)
                        .entity(updatedEntity)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Entity not found for id: " + id)
                        .build();
            }
        } catch (IllegalArgumentException | NonValidEntityException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return A Response object indicating success, or an error message if the entity is not found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOneById(@PathParam("id") String id) {
        try {
            service.deleteOneById(id);
            return Response.ok()
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
