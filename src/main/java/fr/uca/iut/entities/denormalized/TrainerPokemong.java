package fr.uca.iut.entities.denormalized;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.GenericEntity;
import fr.uca.iut.utils.enums.PokemongName;

public class TrainerPokemong extends GenericEntity {
    @Nullable
    private String nickname;

    private PokemongName species;

    public TrainerPokemong() {
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@Nullable String nickname) {
        this.nickname = nickname;
    }

    public PokemongName getSpecies() {
        return species;
    }

    public void setSpecies(PokemongName species) {
        this.species = species;
    }
}
