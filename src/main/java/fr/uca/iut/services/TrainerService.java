package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.entities.Trainer;
import fr.uca.iut.entities.TrainerPokemong;
import fr.uca.iut.repositories.TrainerRepository;
import fr.uca.iut.utils.StringUtils;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TrainerService extends GenericService<Trainer> {

    @Inject
    TrainerRepository trainerRepository;

    @Inject
    PokemongService pokemongService;

    @PostConstruct
    public void init() {
        setRepository(trainerRepository);
    }

    @Override
    public Trainer addOne(@NotNull Trainer trainer) {
        Trainer persistedTrainer = super.addOne(trainer);

        pokemongService.batchUpdatePokemongTrainers(trainer.getPokemongs(), trainer.getId());

        return persistedTrainer;
    }

    @Override
    public void deleteOneById(String id) {
        Trainer trainer = getOneById(id);

        if (trainer != null) {
            pokemongService.batchUpdatePokemongTrainers(trainer.getPokemongs(), null);
        }

        super.deleteOneById(id);
    }

    @Nullable
    @Override
    public Trainer updateOne(@NotNull Trainer trainer) {
        Trainer existingTrainer = trainerRepository.findById(trainer.getId());
        if (existingTrainer != null) {
            existingTrainer.setName(trainer.getName());
            existingTrainer.setDob(trainer.getDob());
            existingTrainer.setWins(trainer.getLosses());
            existingTrainer.setLosses(trainer.getLosses());
            existingTrainer.setPastOpponents(trainer.getPastOpponents());
            existingTrainer.setPokemongs(trainer.getPokemongs());
            trainerRepository.persistOrUpdate(existingTrainer);
        }
        return existingTrainer;
    }

    @Override
    public void validateOne(Trainer trainer) {

        super.validateOne(trainer);

        List<String> errors = new ArrayList<>();

        if (StringUtils.isBlankStringOrNull(trainer.getName())) {
            errors.add("trainer name was null, blank or empty");
        }

        if (trainer.getDob() == null) {
            errors.add("trainer date of birth was null or invalid");
        }

        if (trainer.getLosses() == null || trainer.getLosses() < 0) {
            errors.add("trainer losses was null or negative");
        }

        if (trainer.getWins() == null || trainer.getWins() < 0) {
            errors.add("trainer wins was null or negative");
        }

        List<String> pastOpponents = trainer.getPastOpponents();

        if (pastOpponents == null) {
            errors.add("trainer past opponents collection was null");
        }
        else {
            for (String trainerId : pastOpponents) {
                if (StringUtils.isBlankStringOrNull(trainerId) || !trainerRepository.existsById(trainerId)) {
                    errors.add("trainer past opponents collection contained an invalid or unknown id");
                }
            }
        }

        List<TrainerPokemong> pokemongs = trainer.getPokemongs();

        if (pokemongs == null) {
            errors.add("trainer pokemongs collection was null or invalid");
        }
        else {
            for (TrainerPokemong pokemong : pokemongs) {
                String pokemongId = pokemong.getId();
                if (StringUtils.isBlankStringOrNull(pokemongId) || !pokemongService.existsById(pokemongId)) {
                    errors.add("pokemong with id " + pokemongId + " does not exist");
                }
                else {
                    if (!pokemongService.isEvoValid(pokemongId, pokemong.getSpecies())) {
                        errors.add("pokemong with id " + pokemongId + " cannot be a " +
                                   pokemong.getSpecies());
                    }
                    Pokemong pokemongBehind = pokemongService.getOneById(pokemongId);
                    if (pokemong.getNickname() != null
                        && pokemongBehind != null
                        && !pokemong.getNickname()
                                    .equals(pokemongBehind.getNickname()))
                    {
                        errors.add("pokemong with id " + pokemongId + " already has a nickname");
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new NonValidEntityException("Validation errors: " + String.join(", ", errors));
        }
    }
}
