package fr.uca.iut.codecs.trainer;

import fr.uca.iut.entities.Trainer;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class TrainerCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Trainer.class)) {
            return (Codec<T>) new TrainerCodec();
        }
        return null;
    }
}
