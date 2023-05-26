package fr.uca.iut.entities;

import fr.uca.iut.utils.PokemongName;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public class Pokemong extends GenericEntity {
    public static final String COLLECTION_NAME = "pokemongs";
    @BsonId
    private String id;
    private String nickname;
    private LocalDate dob;
    private Integer level;
    private Integer pokedexId;
    private Integer evoStage;
    private List<PokemongName> evoTrack;
    private Boolean isMegaEvolved;
    private ObjectId trainer;
    private List<Type> types; // TODO Bound this within [1;2] (in controller)
    private List<ObjectId> moveSet; // TODO Bound this within [1;4] (in controller) and denormalize move "name"

    public Pokemong() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
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

    public Integer getEvoStage() {
        return evoStage;
    }

    public void setEvoStage(Integer evoStage) {
        this.evoStage = evoStage;
    }

    public List<PokemongName> getEvoTrack() {
        return evoTrack;
    }

    public void setEvoTrack(List<PokemongName> evoTrack) {
        this.evoTrack = evoTrack;
    }

    public Boolean getMegaEvolved() {
        return isMegaEvolved;
    }

    public void setMegaEvolved(Boolean megaEvolved) {
        isMegaEvolved = megaEvolved;
    }

    public ObjectId getTrainer() {
        return trainer;
    }

    public void setTrainer(ObjectId trainer) {
        this.trainer = trainer;
    }

    // TODO take particular care with collections

    // TODO study the question of encapsulation when it comes to using these dependencies...
    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<ObjectId> getMoveSet() {
        return moveSet;
    }

    public void setMoveSet(List<ObjectId> moveSet) {
        this.moveSet = moveSet;
    }

}

