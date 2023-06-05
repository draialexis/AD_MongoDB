package fr.uca.iut.controllers;

import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.services.GenericService;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public abstract class GenericController<T extends GenericEntity> {

    protected GenericService<T> service;

    public void setService(GenericService<T> service) {
        this.service = service;
    }

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

    @GET
    public Response getAll() {
        return Response.ok(service.getAll())
                .build();
    }

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
