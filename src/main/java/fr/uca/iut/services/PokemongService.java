package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.entities.Trainer;
import fr.uca.iut.entities.denormalized.PokemongMove;
import fr.uca.iut.entities.denormalized.TrainerPokemong;
import fr.uca.iut.entities.embedded.Type;
import fr.uca.iut.repositories.PokemongRepository;
import fr.uca.iut.utils.StringUtils;
import fr.uca.iut.utils.enums.PokemongName;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class PokemongService extends GenericService<Pokemong> {

    @Inject
    PokemongRepository pokemongRepository;

    @Inject
    MoveService moveService;

    @Inject
    TrainerService trainerService;

    @PostConstruct
    public void init() {
        setRepository(pokemongRepository);
    }

    @Override
    public Pokemong addOne(@NotNull Pokemong pokemong) {
        Pokemong persistedPokemong = super.addOne(pokemong);
        String trainerId = pokemong.getTrainer();
        if (trainerId != null) {
            Trainer trainer = trainerService.getOneById(pokemong.getTrainer());
            if (trainer != null) {
                TrainerPokemong trainerPokemong = new TrainerPokemong();
                trainerPokemong.setId(pokemong.getId());
                trainerPokemong.setNickname(pokemong.getNickname());
                trainerPokemong.setSpecies(pokemong.getSpecies());
                trainer.addPokemong(trainerPokemong);
                trainerService.updateOne(trainer);
            }
        }
        return persistedPokemong;
    }

    @Override
    public void validateOne(Pokemong pokemong) {

        super.validateOne(pokemong);

        List<String> errors = new ArrayList<>();

        if (pokemong.getDob() == null) {
            errors.add("pokemong date of birth was null or invalid");
        }

        if (pokemong.getLevel() == null || pokemong.getLevel() < 1) {
            errors.add("pokemong level was null or less than 1");
        }

        if (pokemong.getPokedexId() == null || pokemong.getPokedexId() < 1) {
            errors.add("pokemong pokedex id was null or less than 1");
        }

        if (pokemong.getEvoStage() == null || pokemong.getEvoStage() < 0) {
            errors.add("pokemong evo stage was null or negative");
        }

        if (pokemong.getEvoTrack() == null) {
            errors.add("pokemong evo track was null or invalid");
        }

        Set<Type> types = pokemong.getTypes();
        if (types == null
                || types.size() == 0
                || types.size() > 2) {
            errors.add("pokemong types was null or empty or had more than 2 types");
        }

        Set<PokemongMove> moveSet = pokemong.getMoveSet();
        if (moveSet == null) {
            errors.add("pokemong move set was null");
        } else {
            if (moveSet.size() == 0 || moveSet.size() > 4) {
                errors.add("pokemong move set was empty or had more than 4 moves");
            }
            for (PokemongMove move : moveSet) {
                String moveId = move.getId();
                String moveName = move.getName();
                if (StringUtils.isBlankStringOrNull(moveId) || !moveService.existsById(moveId)) {
                    errors.add("move with id " + moveId + " does not exist");
                }
                if (StringUtils.isBlankStringOrNull(moveName)) {
                    errors.add("move name was null, blank or empty");
                }
                // We don't check whether the move name is consistent with the original -- trainers can rename moves
                // locally in a pokemong. If once in a while a Move has its name updated, the change will be reflected
                // in all the PokemongMoves, and the local aliases will be lost
            }
        }

        if (pokemong.getSchemaVersion() == null ||
                !Objects.equals(pokemong.getSchemaVersion(), Pokemong.LATEST_SCHEMA_VERSION)) {
            errors.add(
                    "pokemong schema version was null or not the latest version: " + Pokemong.LATEST_SCHEMA_VERSION);
        }

        if (!errors.isEmpty()) {
            throw new NonValidEntityException("Validation errors: " + String.join(", ", errors));
        }
    }

    @Override
    public void deleteOneById(String id) {
        Pokemong pokemong = getOneById(id);
        if (pokemong != null && pokemong.getTrainer() != null) {
            Trainer trainer = trainerService.getOneById(pokemong.getTrainer());
            if (trainer != null) {
                trainer.removePokemong(id);
                trainerService.updateOne(trainer);
            }
        }
        super.deleteOneById(id);
    }

    @Override
    @Nullable
    public Pokemong updateOne(@NotNull Pokemong pokemong) {
        super.updateOne(pokemong);
        Pokemong existingPokemong = pokemongRepository.findById(pokemong.getId());
        if (existingPokemong != null) {
            boolean nicknameChanged = !Objects.equals(existingPokemong.getNickname(), pokemong.getNickname());
            boolean evoStageChanged = !Objects.equals(existingPokemong.getEvoStage(), pokemong.getEvoStage());
            boolean evoTrackChanged = !Objects.equals(existingPokemong.getEvoTrack(), pokemong.getEvoTrack());

            existingPokemong.setNickname(pokemong.getNickname());
            existingPokemong.setDob(pokemong.getDob());
            existingPokemong.setLevel(pokemong.getLevel());
            existingPokemong.setPokedexId(pokemong.getPokedexId());
            existingPokemong.setEvoStage(pokemong.getEvoStage());
            existingPokemong.setEvoTrack(pokemong.getEvoTrack());
            existingPokemong.setTrainer(pokemong.getTrainer());
            existingPokemong.setTypes(pokemong.getTypes());
            existingPokemong.setMoveSet(pokemong.getMoveSet());

            pokemongRepository.persistOrUpdate(existingPokemong);

            if (nicknameChanged || evoStageChanged || evoTrackChanged) {
                updateTrainerPokemong(existingPokemong, nicknameChanged, evoStageChanged, evoTrackChanged);
            }
        }
        return existingPokemong;
    }

    private void updateTrainerPokemong(
            @NotNull Pokemong existingPokemong,
            boolean nicknameChanged,
            boolean evoStageChanged,
            boolean evoTrackChanged
    ) {
        Trainer trainer = trainerService.getOneById(existingPokemong.getTrainer());
        if (trainer != null) {
            TrainerPokemong trainerPokemong =
                    trainer.getPokemongs()
                            .stream()
                            .filter(tp -> tp.getId()
                                    .equals(existingPokemong.getId()))
                            .findFirst()
                            .orElse(null);

            if (trainerPokemong != null) {
                if (nicknameChanged) {
                    trainerPokemong.setNickname(existingPokemong.getNickname());
                }

                if (evoStageChanged || evoTrackChanged) {
                    trainerPokemong.setSpecies(existingPokemong.getSpecies());
                }
                trainerService.updateOne(trainer);
            }
        }
    }

    public List<Pokemong> findByMove(String id) {
        return pokemongRepository.findByMove(id);
    }

    public boolean isEvoValid(String id, PokemongName species) {
        Pokemong pokemong = pokemongRepository.findById(id);

        return pokemong != null && pokemong.getSpecies() == species;
    }

    public boolean existsById(String pokemongId) {
        return repository.existsById(pokemongId);
    }

    public void batchUpdatePokemongTrainers(@NotNull Set<TrainerPokemong> trainerPokemongs,
                                            @Nullable String trainerId) {
        List<Pokemong> pokemongsToUpdate = new ArrayList<>();
        for (TrainerPokemong trainerPokemong : trainerPokemongs) {
            Pokemong pokemong = getOneById(trainerPokemong.getId());
            if (pokemong != null && !Objects.equals(pokemong.getTrainer(), trainerId)) {
                pokemong.setTrainer(trainerId);
                pokemongsToUpdate.add(pokemong);
            }
        }
        updateAll(pokemongsToUpdate);
    }
}
