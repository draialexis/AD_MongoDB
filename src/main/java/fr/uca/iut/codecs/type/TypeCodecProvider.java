package fr.uca.iut.codecs.type;

import fr.uca.iut.entities.Type;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class TypeCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Type.class)) {
            return (Codec<T>) new TypeCodec();
        }
        return null;
    }
}
