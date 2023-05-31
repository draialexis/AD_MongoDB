package fr.uca.iut.codecs.pokemong;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.codecs.GenericCodec;
import fr.uca.iut.codecs.type.TypeCodecUtil;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.entities.PokemongMove;
import fr.uca.iut.entities.Type;
import fr.uca.iut.utils.enums.PokemongName;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PokemongCodec extends GenericCodec<Pokemong> {
    private final Codec<Document> documentCodec;

    public PokemongCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry()
                                                .get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Pokemong pokemong, EncoderContext encoderContext) {
        Document doc = new Document();

        doc.put("_id", new ObjectId(pokemong.getId()));

        doc.put("nickname", pokemong.getNickname());

        doc.put("dob",
                Date.from(pokemong.getDob()
                                  .atStartOfDay(ZoneId.systemDefault())
                                  .toInstant()));

        doc.put("level", pokemong.getLevel());

        doc.put("pokedexId", pokemong.getPokedexId());

        doc.put("evoStage", pokemong.getEvoStage());

        List<String> evoTrack = pokemong.getEvoTrack()
                                        .stream()
                                        .map(Enum::name)
                                        .collect(Collectors.toList());
        doc.put("evoTrack", evoTrack);

        if (pokemong.getTrainer() != null) {
            doc.put("trainer", new ObjectId(pokemong.getTrainer()));
        }

        List<Document> types = pokemong.getTypes()
                                       .stream()
                                       .map(type -> {
                                           Document typeDoc = new Document();
                                           typeDoc.put("name",
                                                       type.getName()
                                                           .name());
                                           List<String> weakAgainst = type.getWeakAgainst()
                                                                          .stream()
                                                                          .map(Enum::name)
                                                                          .collect(Collectors.toList());
                                           typeDoc.put("weakAgainst", weakAgainst);
                                           List<String> effectiveAgainst = type.getEffectiveAgainst()
                                                                               .stream()
                                                                               .map(Enum::name)
                                                                               .collect(Collectors.toList());
                                           typeDoc.put("effectiveAgainst", effectiveAgainst);
                                           return typeDoc;
                                       })
                                       .collect(Collectors.toList());
        doc.put("types", types);

        List<Document> moveSetDocs = pokemong.getMoveSet()
                                             .stream()
                                             .map(move -> {
                                                 Document moveDoc = new Document();
                                                 moveDoc.put("_id", new ObjectId(move.getId()));
                                                 moveDoc.put("name", move.getName());
                                                 return moveDoc;
                                             })
                                             .collect(Collectors.toList());
        doc.put("moveSet", moveSetDocs);

        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Pokemong> getEncoderClass() {
        return Pokemong.class;
    }

    @Override
    public Pokemong decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Pokemong pokemong = new Pokemong();

        pokemong.setId(document.getObjectId("_id")
                               .toString());

        pokemong.setNickname(document.getString("nickname"));

        Date dob = document.getDate("dob");
        if (dob != null) {
            pokemong.setDob(dob.toInstant()
                               .atZone(ZoneId.systemDefault())
                               .toLocalDate());
        }

        pokemong.setLevel(document.getInteger("level"));

        pokemong.setPokedexId(document.getInteger("pokedexId"));

        pokemong.setEvoStage(document.getInteger("evoStage"));

        List<PokemongName> evoTrack = document.getList("evoTrack", String.class)
                                              .stream()
                                              .map(PokemongName::valueOf)
                                              .collect(Collectors.toList());
        pokemong.setEvoTrack(evoTrack);

        ObjectId trainerId = document.getObjectId("trainer");
        if (trainerId != null) {
            pokemong.setTrainer(trainerId.toString());
        }

        List<Type> types = document.getList("types", Document.class)
                                   .stream()
                                   .map(TypeCodecUtil::extractType)
                                   .collect(Collectors.toList());
        pokemong.setTypes(types);

        Set<PokemongMove> moveSet = document.getList("moveSet", Document.class)
                                            .stream()
                                            .map(pokemongMoveDoc -> {
                                                PokemongMove move = new PokemongMove();
                                                move.setId(((ObjectId) pokemongMoveDoc.get("_id")).toString());
                                                move.setName(pokemongMoveDoc.getString("name"));
                                                return move;
                                            })
                                            .collect(Collectors.toSet());
        pokemong.setMoveSet(moveSet);

        return pokemong;
    }
}
