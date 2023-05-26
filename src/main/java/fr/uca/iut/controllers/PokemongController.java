package fr.uca.iut.controllers;

import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.services.PokemongService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pokemong")
@Produces(MediaType.APPLICATION_JSON)
public class PokemongController {

    @Inject
    PokemongService pokemongService;

    @GET
    @Path("/{id}")
    public Response getPokemong(@PathParam("id") String id) {
        try {
            Pokemong pokemong = pokemongService.getPokemong(id);
            if (pokemong != null) {
                return Response.ok(pokemong)
                               .build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Pokemong not found for id: " + id)
                               .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid id format: " + id)
                           .build();
        }
    }

    @GET
    public Response getAllPokemongs() {
        return Response.ok(pokemongService.getAllPokemongs())
                       .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPokemong(Pokemong pokemong) {

        if (pokemongService.isNotMature(pokemong)) {
            pokemong.setMegaEvolved(null);
        }

        Pokemong newPokemong = pokemongService.addPokemong(pokemong);

        return Response.status(Response.Status.CREATED)
                       .entity(newPokemong)
                       .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePokemong(@PathParam("id") String id, Pokemong pokemong) {
        try {
            if (pokemongService.isNotMature(pokemong)) {
                pokemong.setMegaEvolved(null);
            }

            pokemong.setId(id);
            Pokemong updatedPokemong = pokemongService.updatePokemong(pokemong);

            if (updatedPokemong != null) {
                return Response.status(Response.Status.OK)
                               .entity(updatedPokemong)
                               .build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Pokemong not found for id: " + id)
                               .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid id format: " + id)
                           .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePokemong(@PathParam("id") String id) {
        try {
            pokemongService.deletePokemong(id);
            return Response.ok()
                           .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid id format: " + id)
                           .build();
        }
    }
}
