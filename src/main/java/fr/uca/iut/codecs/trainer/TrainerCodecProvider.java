package fr.uca.iut.codecs.trainer;

import com.mongodb.lang.Nullable;
import fr.uca.iut.entities.Trainer;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.jetbrains.annotations.NotNull;

public class TrainerCodecProvider implements CodecProvider {
    @Nullable
    @Override
    public <T> Codec<T> get(@NotNull Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Trainer.class)) {
            return (Codec<T>) new TrainerCodec();
        }
        return null;
    }
}
