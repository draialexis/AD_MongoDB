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
    public void deleteOneById(String id) {
        super.deleteOneById(id);
        List<Pokemong> pokemongs = pokemongService.findByMove(id);
        for (Pokemong pokemong : pokemongs) {
            pokemong.removeMove(id);
            pokemongService.updateOne(pokemong);
        }
    }

    @Override
    @Nullable
    public Move updateOne(@NotNull Move move) {
        Move existingMove = moveRepository.findById(move.getId());
        if (existingMove != null) {
            if (!existingMove.getName()
                             .equals(move.getName()))
            {
                existingMove.setName(move.getName());
                List<Pokemong> pokemongs = pokemongService.findByMove(move.getId());
                for (Pokemong pokemong : pokemongs) {
                    pokemong.updateMove(move.getId(), move.getName());
                    pokemongService.updateOne(pokemong);
                }
            }

            existingMove.setPower(move.getPower());
            existingMove.setCategory(move.getCategory());
            existingMove.setAccuracy(move.getAccuracy());
            existingMove.setType(move.getType());
            moveRepository.persistOrUpdate(existingMove);
        }
        return existingMove;
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

        if (!errors.isEmpty()) {
            throw new NonValidEntityException("Validation errors: " + String.join(", ", errors));
        }
    }

    public boolean existsById(String moveId) {
        return moveRepository.existsById(moveId);
    }
}
