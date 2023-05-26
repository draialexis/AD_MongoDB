package fr.uca.iut.codecs;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.entities.GenericEntity;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public abstract class GenericCodec<T extends GenericEntity> implements CollectibleCodec<T> {
    private final Codec<Document> documentCodec;
    protected GenericCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry()
                                                .get(Document.class);
    }

    public Codec<Document> getDocumentCodec() {
        return documentCodec;
    }
    @Override
    public abstract void encode(BsonWriter writer, T entity, EncoderContext encoderContext);

    @Override
    public abstract Class<T> getEncoderClass();

    @Override
    public T generateIdIfAbsentFromDocument(T document) {
        if (!documentHasId(document)) {
            document.setId(new ObjectId().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(T document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(T document) {
        return new BsonObjectId(new ObjectId(document.getId()));
    }

    @Override
    public abstract T decode(BsonReader reader, DecoderContext decoderContext);
}
