package fr.uca.iut.entities;

import fr.uca.iut.utils.PokemongName;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@MongoEntity(collection = "pokemongs")
public class Pokemong extends PanacheMongoEntity {
    public String nickname;
    public LocalDate dob;
    public Integer level;
    public Integer pokedexId;
    public Integer evoStage;
    public List<PokemongName> evoTrack;
    public Boolean isMegaEvolved;
    public ObjectId trainer;
    public Set<Type> types; // TODO Bound this within [1;2] (in controller)
    public List<ObjectId> moveSet; // TODO Bound this within [1;4] (in controller) and denormalize move "name"
}

