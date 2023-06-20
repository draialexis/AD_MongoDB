package fr.uca.iut.codecs;

import fr.uca.iut.entities.GenericEntity;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract codec class for encoding and decoding entities to and from BSON.
 * The specific type of entity is defined by the generic parameter T, which extends GenericEntity.
 * This class implements the CollectibleCodec interface.
 */
public abstract class GenericCodec<T extends GenericEntity> implements CollectibleCodec<T> {

    /**
     * Encodes the entity into BSON.
     * This method must be overridden by subclasses.
     *
     * @param writer         The BsonWriter to write the BSON.
     * @param entity         The entity to encode.
     * @param encoderContext The context for encoding.
     */
    @Override
    public abstract void encode(BsonWriter writer, T entity, EncoderContext encoderContext);

    /**
     * Returns the class of the entity being encoded.
     * This method must be overridden by subclasses.
     *
     * @return The class of the entity being encoded.
     */
    @Override
    public abstract Class<T> getEncoderClass();

    /**
     * Generates an ID for the document if it doesn't have one.
     *
     * @param document The document to possibly assign an ID to.
     * @return The document with an ID assigned, if it didn't have one.
     */
    @Override
    public T generateIdIfAbsentFromDocument(T document) {
        if (!documentHasId(document)) {
            document.setId(new ObjectId().toString());
        }
        return document;
    }

    /**
     * Checks if a document has an ID.
     *
     * @param document The document to check.
     * @return true if the document has an ID, false otherwise.
     */
    @Override
    public boolean documentHasId(@NotNull T document) {
        return document.getId() != null;
    }

    /**
     * Returns the ID of the document.
     *
     * @param document The document whose ID to get.
     * @return The ID of the document.
     */
    @Override
    public BsonValue getDocumentId(@NotNull T document) {
        return new BsonObjectId(new ObjectId(document.getId()));
    }


    /**
     * Decodes a BSON document into an entity.
     * This method must be overridden by subclasses.
     *
     * @param reader         The BsonReader from which to read the BSON.
     * @param decoderContext The context for decoding.
     * @return The decoded entity.
     */
    @Override
    public abstract T decode(BsonReader reader, DecoderContext decoderContext);
}
