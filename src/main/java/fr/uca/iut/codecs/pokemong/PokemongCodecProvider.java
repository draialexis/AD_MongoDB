package fr.uca.iut.codecs.pokemong;

import fr.uca.iut.entities.Pokemong;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class PokemongCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Pokemong.class)) {
            return (Codec<T>) new PokemongCodec();
        }
        return null;
    }
}