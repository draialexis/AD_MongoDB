package fr.uca.iut.controllers;

import fr.uca.iut.entities.Trainer;
import fr.uca.iut.services.TrainerService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/trainer")
@Produces(MediaType.APPLICATION_JSON)
public class TrainerController extends GenericController<Trainer> {

    @Inject
    TrainerService trainerService;

    @PostConstruct
    public void init() {
        setService(trainerService);
    }
}
