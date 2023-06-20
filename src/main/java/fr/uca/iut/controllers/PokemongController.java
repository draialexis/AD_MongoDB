package fr.uca.iut.controllers;

import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.services.PokemongService;
import fr.uca.iut.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;

@Path("/pokemong")
@Produces(MediaType.APPLICATION_JSON)
public class PokemongController extends GenericController<Pokemong> {

    @Inject
    PokemongService pokemongService;

    @PostConstruct
    public void init() {
        setService(pokemongService);
    }

    @GET
    @Path("/nickname/{nickname}")
    public Response findByName(@PathParam("nickname") String nickname) {
        if (StringUtils.isBlankStringOrNull(nickname)) {
            return Response.ok(new ArrayList<>())
                    .build();
        }
        return Response.ok(pokemongService.findByNickname(nickname.trim()))
                .build();
    }

    @GET
    @Path("/dob/{start-date}/{end-date}")
    public Response findByDateOfBirthInterval(
            @PathParam("start-date") String startDate,
            @PathParam("end-date") String endDate
    ) {
        if (StringUtils.isBlankStringOrNull(startDate) || StringUtils.isBlankStringOrNull(endDate)) {
            return Response.ok(new ArrayList<>())
                    .build();
        }
        try {
            return Response
                    .ok(pokemongService.findByDateOfBirthInterval(LocalDate.parse(startDate), LocalDate.parse(endDate)))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/count-by-evo-stage")
    public Response countPokemongsByEvoStage() {
        return Response.ok(pokemongService.countPokemongsByEvoStage())
                .build();
    }
}
