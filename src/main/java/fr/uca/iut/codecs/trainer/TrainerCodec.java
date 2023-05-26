package fr.uca.iut.codecs.trainer;

import com.mongodb.MongoClientSettings;
import fr.uca.iut.codecs.GenericCodec;
import fr.uca.iut.entities.Trainer;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.util.Date;

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
        doc.put("dob", Date.from(trainer.getDob().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        doc.put("wins", trainer.getWins());
        doc.put("losses", trainer.getLosses());
        doc.put("pastOpponents", trainer.getPastOpponents());
        doc.put("pokemongs", trainer.getPokemongs());
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
        trainer.setId(document.getObjectId("_id").toString());
        trainer.setName(document.getString("name"));
        Date dob = document.getDate("dob");
        if (dob != null) {
            trainer.setDob(dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        trainer.setWins(document.getInteger("wins", 0));
        trainer.setLosses(document.getInteger("losses", 0));
        trainer.setPastOpponents(document.getList("pastOpponents", ObjectId.class));
        trainer.setPokemongs(document.getList("pokemongs", ObjectId.class));
        return trainer;
    }
}
