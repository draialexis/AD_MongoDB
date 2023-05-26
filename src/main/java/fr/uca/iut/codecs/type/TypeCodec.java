package fr.uca.iut.codecs.type;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.entities.Type;
import fr.uca.iut.utils.TypeName;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TypeCodec implements Codec<Type> {
    private final Codec<Document> documentCodec;

    public TypeCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry()
                                                .get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Type type, EncoderContext encoderContext) {
        Document doc = new Document();
        Optional.ofNullable(type.getName())
                .map(Enum::name)
                .ifPresent(name -> doc.put("name", name));

        Optional.ofNullable(type.getWeakAgainst())
                .map(weakAgainst -> weakAgainst.stream().map(Enum::name).collect(Collectors.toList()))
                .ifPresent(weakAgainst -> doc.put("weakAgainst", weakAgainst));

        Optional.ofNullable(type.getEffectiveAgainst())
                .map(effectiveAgainst -> effectiveAgainst.stream().map(Enum::name).collect(Collectors.toList()))
                .ifPresent(effectiveAgainst -> doc.put("effectiveAgainst", effectiveAgainst));

        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Type> getEncoderClass() {
        return Type.class;
    }

    @Override
    public Type decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Type type = new Type();

        Optional.ofNullable(document.getString("name"))
                .map(TypeName::valueOf)
                .ifPresent(type::setName);

        Optional.ofNullable(document.get("weakAgainst"))
                .filter(obj -> obj instanceof List<?>)
                .map(obj -> ((List<String>) obj).stream().map(TypeName::valueOf).collect(Collectors.toList()))
                .ifPresent(type::setWeakAgainst);

        Optional.ofNullable(document.get("effectiveAgainst"))
                .filter(obj -> obj instanceof List<?>)
                .map(obj -> ((List<String>) obj).stream().map(TypeName::valueOf).collect(Collectors.toList()))
                .ifPresent(type::setEffectiveAgainst);

        return type;
    }
}
