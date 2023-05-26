package fr.uca.iut.codecs.pokemong;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.codecs.GenericCodec;
import fr.uca.iut.entities.Pokemong;
import fr.uca.iut.entities.Type;
import fr.uca.iut.utils.PokemongName;
import fr.uca.iut.utils.TypeName;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.util.*;
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
        List<String> evoTrack = Optional.ofNullable(pokemong.getEvoTrack())
                                        .orElse(Collections.emptyList())
                                        .stream()
                                        .map(Enum::name)
                                        .collect(Collectors.toList());

        doc.put("evoTrack", evoTrack);
        doc.put("isMegaEvolved", pokemong.getMegaEvolved());
        doc.put("trainer", pokemong.getTrainer());
        List<Document> types = Optional.ofNullable(pokemong.getTypes())
                                       .orElse(Collections.emptyList())
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
        doc.put("moveSet", pokemong.getMoveSet());
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
        pokemong.setId(document.getObjectId("_id").toString());
        pokemong.setNickname(document.getString("nickname"));
        Date dob = document.getDate("dob");
        if (dob != null) {
            pokemong.setDob(dob.toInstant()
                               .atZone(ZoneId.systemDefault())
                               .toLocalDate());
        }
        pokemong.setPokedexId(document.getInteger("pokedexId"));
        pokemong.setEvoStage(document.getInteger("evoStage"));
        List<PokemongName> evoTrack = Optional.ofNullable((List<String>) document.get("evoTrack"))
                                              .orElse(Collections.emptyList())
                                              .stream()
                                              .map(PokemongName::valueOf)
                                              .collect(Collectors.toList());
        pokemong.setEvoTrack(evoTrack);
        pokemong.setMegaEvolved(document.getBoolean("isMegaEvolved"));
        pokemong.setTrainer(document.getObjectId("trainer"));
        List<Type> types = Optional.ofNullable((List<Document>) document.get("types"))
                                   .orElse(Collections.emptyList())
                                   .stream()
                                   .map(typeDoc -> {
                                       Type type = new Type();
                                       type.setName(TypeName.valueOf(typeDoc.getString("name")));
                                       List<TypeName> weakAgainst = Optional
                                               .ofNullable((List<String>) typeDoc.get("weakAgainst"))
                                               .orElse(Collections.emptyList())
                                               .stream()
                                               .map(TypeName::valueOf)
                                               .collect(Collectors.toList());
                                       type.setWeakAgainst(weakAgainst);
                                       List<TypeName> effectiveAgainst = Optional
                                               .ofNullable((List<String>) typeDoc.get("effectiveAgainst"))
                                               .orElse(Collections.emptyList())
                                               .stream()
                                               .map(TypeName::valueOf)
                                               .collect(Collectors.toList());
                                       type.setEffectiveAgainst(effectiveAgainst);
                                       return type;
                                   })
                                   .collect(Collectors.toList());
        pokemong.setTypes(types);
        pokemong.setMoveSet(Optional.ofNullable(document.getList("moveSet", ObjectId.class))
                                    .orElse(Collections.emptyList()));
        return pokemong;
    }
}
