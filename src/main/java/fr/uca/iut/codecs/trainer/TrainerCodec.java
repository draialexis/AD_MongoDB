package fr.uca.iut.codecs.trainer;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.codecs.GenericCodec;
import fr.uca.iut.entities.Trainer;
import fr.uca.iut.entities.TrainerPokemong;
import fr.uca.iut.utils.enums.PokemongName;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TrainerCodec extends GenericCodec<Trainer> {
    private final Codec<Document> documentCodec;

    public TrainerCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry()
                                                .get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Trainer trainer, EncoderContext encoderContext) {
        Document doc = new Document();

        doc.put("_id", new ObjectId(trainer.getId()));

        doc.put("name", trainer.getName());

        LocalDate dob = trainer.getDob();
        if (dob != null) {
            doc.put("dob", Date.from(dob.atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()));
        }

        doc.put("wins", trainer.getWins());

        doc.put("losses", trainer.getLosses());

        List<ObjectId> pastOpponentsIds = trainer.getPastOpponents()
                                                 .stream()
                                                 .map(ObjectId::new)
                                                 .collect(Collectors.toList());
        doc.put("pastOpponents", pastOpponentsIds);

        List<Document> pokemongListDoc = trainer.getPokemongs()
                                                .stream()
                                                .map(pokemong -> {
                                                    Document moveDoc = new Document();
                                                    moveDoc.put("_id", new ObjectId(pokemong.getId()));
                                                    moveDoc.put("nickname", pokemong.getNickname());
                                                    moveDoc.put("species",
                                                                pokemong.getSpecies()
                                                                        .name());
                                                    return moveDoc;
                                                })
                                                .collect(Collectors.toList());
        doc.put("pokemongs", pokemongListDoc);

        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Trainer> getEncoderClass() {
        return Trainer.class;
    }

    @Override
    public Trainer decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Trainer trainer = new Trainer();

        trainer.setId(document.getObjectId("_id")
                              .toString());

        trainer.setName(document.getString("name"));

        Date dob = document.getDate("dob");
        if (dob != null) {
            trainer.setDob(dob.toInstant()
                              .atZone(ZoneId.systemDefault())
                              .toLocalDate());
        }

        trainer.setWins(document.getInteger("wins"));

        trainer.setLosses(document.getInteger("losses"));

        List<String> pastOpponentsIds = document.getList("pastOpponents", ObjectId.class)
                                                .stream()
                                                .map(ObjectId::toString)
                                                .collect(Collectors.toList());
        trainer.setPastOpponents(pastOpponentsIds);

        List<TrainerPokemong> pokemongList = document
                .getList("pokemongs", Document.class)
                .stream()
                .map(pokemongDoc -> {
                    TrainerPokemong pokemong = new TrainerPokemong();
                    pokemong.setId(((ObjectId) pokemongDoc.get("_id")).toString());
                    pokemong.setNickname(pokemongDoc.getString("nickname"));
                    pokemong.setSpecies(PokemongName.valueOf(pokemongDoc.getString("species")));
                    return pokemong;
                })
                .collect(Collectors.toList());
        trainer.setPokemongs(pokemongList);
        return trainer;
    }
}
