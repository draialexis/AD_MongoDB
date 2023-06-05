package fr.uca.iut.codecs.pokemong;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Pokemong;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.jetbrains.annotations.NotNull;

public class PokemongCodecProvider implements CodecProvider {
    @Nullable
    @Override
    public <T> Codec<T> get(@NotNull Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Pokemong.class)) {
            return (Codec<T>) new PokemongCodec();
        }
        return null;
    }
}