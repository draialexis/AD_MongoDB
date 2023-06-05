package fr.uca.iut.entities;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.denormalized.PokemongMove;
import fr.uca.iut.entities.embedded.Type;
import fr.uca.iut.utils.enums.PokemongName;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Pokemong extends GenericVersionedEntity {
    public static final String COLLECTION_NAME = "pokemongs";

    public static final Integer LATEST_SCHEMA_VERSION = 1;

    @Nullable
    private String nickname;
    private LocalDate dob;
    private Integer level;
    private Integer pokedexId;
    private Integer evoStage;
    private List<PokemongName> evoTrack;
    @Nullable
    private String trainer;
    private Set<Type> types;

    /**
     * pokemong.moveSet: [{_id: ObjectId, name: String}]
     */
    private Set<PokemongMove> moveSet;

    public Pokemong() {
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@Nullable String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(Integer pokedexId) {
        this.pokedexId = pokedexId;
    }

    @Nullable
    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(@Nullable String trainer) {
        this.trainer = trainer;
    }

    public Set<Type> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    public Set<PokemongMove> getMoveSet() {
        return Collections.unmodifiableSet(moveSet);
    }

    public void setMoveSet(Set<PokemongMove> moveSet) {
        this.moveSet = moveSet;
    }

    public void removeMove(String id) {
        PokemongMove pokemongMove = new PokemongMove();
        pokemongMove.setId(id);
        moveSet.remove(pokemongMove);
    }

    public void updateMove(String id, String name) {
        for (PokemongMove move : moveSet) {
            if (move.getId()
                    .equals(id)) {
                move.setName(name);
                break;
            }
        }
    }

    public PokemongName getSpecies() {
        return getEvoTrack().get(getEvoStage());
    }

    public List<PokemongName> getEvoTrack() {
        return Collections.unmodifiableList(evoTrack);
    }

    public void setEvoTrack(List<PokemongName> evoTrack) {
        this.evoTrack = evoTrack;
    }

    public Integer getEvoStage() {
        return evoStage;
    }

    public void setEvoStage(Integer evoStage) {
        this.evoStage = evoStage;
    }
}

