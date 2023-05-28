package fr.uca.iut.controllers;

import fr.uca.iut.entities.Move;
import fr.uca.iut.services.MoveService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/move")
@Produces(MediaType.APPLICATION_JSON)
public class MoveController extends GenericController<Move> {

    @Inject
    MoveService moveService;

    @PostConstruct
    public void init() {
        setService(moveService);
    }
}
