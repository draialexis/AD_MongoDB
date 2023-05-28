package fr.uca.iut.codecs.move;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Move;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class MoveCodecProvider implements CodecProvider {
    @Nullable
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Move.class)) {
            return (Codec<T>) new MoveCodec();
        }
        return null;
    }
}
