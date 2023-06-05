package fr.uca.iut.services;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Move;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.repositories.MoveRepository;
import fr.uca.iut.utils.StringUtils;
import fr.uca.iut.utils.exceptions.NonValidEntityException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MoveService extends GenericService<Move> {

    @Inject
    MoveRepository moveRepository;
    @Inject
    PokemongService pokemongService;

    @PostConstruct
    public void init() {
        setRepository(moveRepository);
    }

    @Override
    public void validateOne(Move move) {

        super.validateOne(move);

        List<String> errors = new ArrayList<>();

        if (StringUtils.isBlankStringOrNull(move.getName())) {
            errors.add("move name was null, blank or empty");
        }

        if (move.getPower() == null || move.getPower() < 0) {
            errors.add("move power was null or negative");
        }

        if (move.getCategory() == null) {
            errors.add("move category was null or invalid");
        }

        if (move.getAccuracy() == null || move.getAccuracy() < 0) {
            errors.add("move accuracy was null or negative");
        }

        if (move.getType() == null) {
            errors.add("move type was null or invalid");
        }

        if (move.getSchemaVersion() == null || !Objects.equals(move.getSchemaVersion(), Move.LATEST_SCHEMA_VERSION)) {
            errors.add("move schema version was null or not the latest version: " + Move.LATEST_SCHEMA_VERSION);
        }

        if (!errors.isEmpty()) {
            throw new NonValidEntityException("Validation errors: " + String.join(", ", errors));
        }
    }

    @Nullable
    @Override
    public Move getOneById(String id) {
        return migrateToV2(super.getOneById(id));
    }

    @Override
    public List<Move> getAll() {
        return super.getAll()
                .stream()
                .map(this::migrateToV2)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOneById(String id) {
        List<Pokemong> pokemongs = pokemongService.findByMove(id);
        List<Pokemong> pokemongsToUpdate = new ArrayList<>();
        for (Pokemong pokemong : pokemongs) {
            pokemong.removeMove(id);
            pokemongsToUpdate.add(pokemong);
        }
        pokemongService.updateAll(pokemongsToUpdate);
        super.deleteOneById(id);
    }

    @Override
    @Nullable
    public Move updateOne(@NotNull Move move) {
        super.updateOne(move);
        Move existingMove = moveRepository.findById(move.getId());
        if (existingMove != null) {
            if (!existingMove.getName().equals(move.getName())) {
                existingMove.setName(move.getName());
                batchUpdatePokemongTrainers(move);
            }

            existingMove.setPower(move.getPower());
            existingMove.setCategory(move.getCategory());
            existingMove.setAccuracy(move.getAccuracy());
            existingMove.setType(move.getType());
            moveRepository.persistOrUpdate(existingMove);
        }
        return existingMove;
    }

    private void batchUpdatePokemongTrainers(@NotNull Move move) {
        List<Pokemong> pokemongs = pokemongService.findByMove(move.getId());
        List<Pokemong> pokemongsToUpdate = new ArrayList<>();
        for (Pokemong pokemong : pokemongs) {
            pokemong.updateMove(move.getId(), move.getName());
            pokemongsToUpdate.add(pokemong);
        }
        pokemongService.updateAll(pokemongsToUpdate);
    }

    /**
     * We want to migrate the documents incrementally, so we upgrade the
     * schema version if it is less than the current schema version,
     * and then save the updated document back to the database.
     *
     * @param move the Move found by the repository
     * @return the Move(V2) based on the Move from the repository
     */
    private Move migrateToV2(Move move) {
        if (move != null && move.getSchemaVersion() < 2) {
            move.setSchemaVersion(2);
            moveRepository.persistOrUpdate(move);
        }
        return move;
    }

    public boolean existsById(String moveId) {
        return moveRepository.existsById(moveId);
    }
}
