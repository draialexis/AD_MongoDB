package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.entities.Trainer;
import fr.uca.iut.entities.denormalized.TrainerPokemong;
import fr.uca.iut.repositories.TrainerRepository;
import fr.uca.iut.utils.StringUtils;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

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

        // If this trainer gained pokemongs, that pokemong's ex-trainer if any needs to lose said pokemongs
        transferNewlyArrivedTrainerPokemongs(new HashSet<>(), persistedTrainer.getPokemongs());
        // all owned pokemongs gain this trainer's reference
        pokemongService.batchUpdatePokemongTrainers(trainer.getPokemongs(), trainer.getId());

        return persistedTrainer;
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
        } else {
            for (String trainerId : pastOpponents) {
                if (StringUtils.isBlankStringOrNull(trainerId)) {
                    errors.add("trainer past opponents collection contained an invalid id: " + trainerId);
                }
            }
        }

        Set<TrainerPokemong> pokemongs = trainer.getPokemongs();

        if (pokemongs == null) {
            errors.add("trainer pokemongs collection was null or invalid");
        } else {
            for (TrainerPokemong pokemong : pokemongs) {
                String pokemongId = pokemong.getId();
                if (StringUtils.isBlankStringOrNull(pokemongId) || !pokemongService.existsById(pokemongId)) {
                    errors.add("pokemong with id " + pokemongId + " does not exist");
                } else {
                    if (!pokemongService.isEvoValid(pokemongId, pokemong.getSpecies())) {
                        errors.add("pokemong with id " + pokemongId + " cannot be a " +
                                pokemong.getSpecies());
                    }
                    Pokemong pokemongBehind = pokemongService.getOneById(pokemongId);
                    if (pokemong.getNickname() != null
                            && pokemongBehind != null
                            && !pokemong.getNickname()
                            .equals(pokemongBehind.getNickname())) {
                        errors.add("pokemong with id " + pokemongId + " already has a nickname");
                    }
                }
            }
        }

        if (trainer.getSchemaVersion() == null ||
                !Objects.equals(trainer.getSchemaVersion(), Trainer.LATEST_SCHEMA_VERSION)) {
            errors.add("trainer schema version was null or not the latest version: " + Trainer.LATEST_SCHEMA_VERSION);
        }

        if (!errors.isEmpty()) {
            throw new NonValidEntityException("Validation errors: " + String.join(", ", errors));
        }
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
        super.updateOne(trainer);
        Trainer existingTrainer = trainerRepository.findById(trainer.getId());
        if (existingTrainer != null) {
            Set<TrainerPokemong> oldPokemongs = existingTrainer.getPokemongs();

            existingTrainer.setName(trainer.getName());
            existingTrainer.setDob(trainer.getDob());
            existingTrainer.setWins(trainer.getLosses());
            existingTrainer.setLosses(trainer.getLosses());
            existingTrainer.setPastOpponents(trainer.getPastOpponents());
            existingTrainer.setPokemongs(trainer.getPokemongs());
            trainerRepository.persistOrUpdate(existingTrainer);

            Set<TrainerPokemong> newPokemongs = trainer.getPokemongs();

            // all old pokemongs who are not there anymore lose their trainer reference
            pokemongService.batchUpdatePokemongTrainers(
                    oldPokemongs.stream()
                            .filter(tp -> !newPokemongs.contains(tp))
                            .collect(Collectors.toSet()),
                    null);
            // If this trainer gained a pokemong, that pokemong's ex-trainer if any needs to lose said pokemong
            transferNewlyArrivedTrainerPokemongs(oldPokemongs, newPokemongs);
            // all new pokemongs who were not there before gain this trainer's reference
            pokemongService.batchUpdatePokemongTrainers(
                    newPokemongs.stream()
                            .filter(tp -> !oldPokemongs.contains(tp))
                            .collect(Collectors.toSet()),
                    existingTrainer.getId());
        }
        return existingTrainer;
    }

    private void transferNewlyArrivedTrainerPokemongs(
            @NotNull Set<TrainerPokemong> oldPokemongs,
            @NotNull Set<TrainerPokemong> newPokemongs
    ) {
        List<Trainer> trainersToUpdate = new ArrayList<>();

        for (TrainerPokemong newTrainerPokemong : newPokemongs) {
            if (oldPokemongs.isEmpty() || !oldPokemongs.contains(newTrainerPokemong)) {
                Pokemong pokemong = pokemongService.getOneById(newTrainerPokemong.getId());
                if (pokemong != null) {
                    String oldTrainerId = pokemong.getTrainer();
                    // If the pokemong already had a trainer, remove it from the old trainer's pokemongs list
                    if (oldTrainerId != null) {
                        Trainer oldTrainer = getOneById(oldTrainerId);
                        if (oldTrainer != null) {
                            oldTrainer.removePokemong(newTrainerPokemong.getId());
                            trainersToUpdate.add(oldTrainer);
                        }
                    }
                }
            }
        }
        updateAll(trainersToUpdate);
    }
}
